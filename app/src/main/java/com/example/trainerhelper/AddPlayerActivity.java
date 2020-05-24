package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.Calendar;



public class AddPlayerActivity extends AppCompatActivity {
   private ImageView saveImage;
   private EditText etName, etSurname, etPatronymic, etBirthday;
   private CheckBox cMale,cFemale;
   private DbHelper dbHelper;
   private String name,surname,patronymic,birthday,gender;
   private DatePickerDialog.OnDateSetListener mDateSetListener;
   private Toolbar toolbar;
   private ActionBar actionBar;
   private SQLiteDatabase database;
   private ContentValues contentValues;
   private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        initViewElements(); //Элементы
        initListeners(); //Слушатели
        initDb();//SQLite
    }

    private void initViewElements() {
        etName = findViewById(R.id.name);
        etSurname = findViewById(R.id.surname);
        etPatronymic = findViewById(R.id.patronymic);
        etBirthday = findViewById(R.id.birthday);
        cMale = findViewById(R.id.genderMaleCheckBox);
        cFemale = findViewById(R.id.genderFemaleCheckBox);
        saveImage = findViewById(R.id.saveImage);
        initToolBar(toolbar);
    }
    private void initToolBar(Toolbar toolbar){
        intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(intent.hasExtra("Edit")){
            setViewElements();
            actionBar.setTitle("Изменить");
        }
        else {
            actionBar.setTitle("Добавить игрока");
        }

    }

    private void setViewElements(){
        actionBar.setTitle("Изменить данные");
        etName.setText(intent.getStringExtra("Name"));
        etSurname.setText(intent.getStringExtra("Surname"));
        etPatronymic.setText(intent.getStringExtra("Patronymic"));
        etBirthday.setText(intent.getStringExtra("Birthday"));
    }


    private boolean getValues(){
            name = etName.getText().toString();
            surname = etSurname.getText().toString();
            patronymic = etPatronymic.getText().toString();
            birthday = etBirthday.getText().toString();
            gender = "";
            if(cMale.isChecked()) gender = "Мужской";
            if(cFemale.isChecked()) gender = "Женский";
        return (!name.isEmpty() && !surname.isEmpty() && !patronymic.isEmpty() && !birthday.isEmpty() &&  !gender.isEmpty());
    }



    private void initDb(){
        dbHelper = new DbHelper(this);
    }

    private void putOnDb(int IdInDb){
        if(getValues()){
            database = dbHelper.getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put(DbHelper.KEY_NAME, name);
            contentValues.put(DbHelper.KEY_SURNAME, surname);
            contentValues.put(DbHelper.KEY_PATRONYMIC, patronymic);
            contentValues.put(DbHelper.KEY_BIRTHDAY, birthday);
            contentValues.put(DbHelper.KEY_GENDER, gender);
            contentValues.put(DbHelper.KEY_GROUP, intent.getIntExtra("groupIdInDb",0));
            if(intent.hasExtra("Edit")){
                database.update(DbHelper.TABLE_PLAYERS, contentValues, DbHelper.KEY_ID + " = ?",new String[]{Integer.toString(IdInDb)});
            }
            else {
                database.insert(DbHelper.TABLE_PLAYERS, null, contentValues);
            }
            Toast.makeText(getApplicationContext(),"Данные успешно сохранены", Toast.LENGTH_SHORT).show();
            dbHelper.close();
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),"Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    private void showTable(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_PLAYERS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DbHelper.KEY_SURNAME);
            int patronymicIndex = cursor.getColumnIndex(DbHelper.KEY_PATRONYMIC);
            int dateIndex = cursor.getColumnIndex(DbHelper.KEY_BIRTHDAY);
            int groupIndex = cursor.getColumnIndex(DbHelper.KEY_GROUP);
            int genderIndex = cursor.getColumnIndex(DbHelper.KEY_GENDER);
            Log.d("MyLog","ТАБЛИЦА ИГРОКИ");
            do {
                Log.d("MyLog", "ID = " + cursor.getInt(idIndex) + " name = " + cursor.getString(nameIndex) + " surname = " + cursor.getString(surnameIndex) + " patronymic = " + cursor.getString(patronymicIndex) + " birthday = " + cursor.getString(dateIndex) +  " groupId = " + cursor.getInt(groupIndex) + " gender = " + cursor.getString(genderIndex) );
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();
    }

    private void initListeners() {
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putOnDb(intent.getIntExtra("IdInDb", 0));
                showTable();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                birthday = DbHelper.parseDate(year,month,dayOfMonth);
                etBirthday.setText(birthday);
            }
        };

        etBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dateDialog = new DatePickerDialog(
                            AddPlayerActivity.this,
                            mDateSetListener,
                            day,month,year);
                    dateDialog.getDatePicker().setMinDate(cal.getTimeInMillis() - 18 * 1000L * 60 *60 *24*365);
                    dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dateDialog.show();
                    etBirthday.clearFocus();

                }
            }
        });



        cMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cMale.isChecked()){
                    cFemale.setChecked(false);
                    gender = "Мужской";
                }
            }
        });
        cFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cFemale.isChecked()){
                    cMale.setChecked(false);
                    gender = "Женский";
                }
            }
        });


    }
}
