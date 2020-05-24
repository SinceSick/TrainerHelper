package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.trainerhelper.adapter.AddGroupAdapter;
import com.example.trainerhelper.pojo.SelectDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddGroupActivity extends AppCompatActivity implements AddGroupAdapter.AddGroupListener {

    private RecyclerView recyclerView;
    private List<SelectDay> selectDays;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private String time,name;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ContentValues contentValues;
    private EditText etGroupName, etTime;
    private ImageView saveImage;
    private Intent intent;
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        initView();
        initRecyclerView();
        initData();
        initAdapter();
        initDb();
    }
////////////////////////////////////////////////////VIEW
    private void initView(){
        saveImage = findViewById(R.id.saveImage);
        etGroupName = findViewById(R.id.groupName);
        etTime = findViewById(R.id.groupTime);
        initToolBar(toolbar);
        initListeners();
    }

    private void initToolBar(Toolbar toolbar){
        intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(intent.hasExtra("Edit")){
            actionBar.setTitle("Изменить группу");
            etGroupName.setText(intent.getStringExtra("Name"));
            etTime.setText(intent.getStringExtra("Time"));
        }
        else {
            actionBar.setTitle("Добавить группу");
        }


    }

    private void initListeners(){
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    if(intent.hasExtra("Edit")){
                        time = String.valueOf(etTime.getText());
                        int id = intent.getIntExtra("IdInDb", 0 );
                        editInDb(id);
                        showTable();
                        finish();
                        Toast.makeText(getApplicationContext(),"Данные успешно сохранены", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(putInDb() > 0){
                            finish();
                            Toast.makeText(getApplicationContext(),"Данные успешно сохранены",Toast.LENGTH_SHORT).show();
                        }
                        showTable();
                    }
                    clearFields();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Заполните все поля",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = DbHelper.parseTime(hourOfDay,minute);
                etTime.setText(time);
            }
        };
        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar cal = Calendar.getInstance();
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            AddGroupActivity.this,
                            mOnTimeSetListener,
                            hour,minute,true);
                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    timePickerDialog.show();
                    etTime.clearFocus();
                }
            }
        });
    }

//////////////////////////////////////////////DATABASE
    private void initDb(){
        dbHelper = new DbHelper(this);
    }

    private long putInDb(){
        database = dbHelper.getWritableDatabase();
        DbHelper.initList();
        contentValues = new ContentValues();
        contentValues.put(DbHelper.KEY_GROUP_NAME,String.valueOf(etGroupName.getText()));
        for(int i = 0; i < selectDays.size(); i ++){
            if(selectDays.get(i).isChosen()){
                contentValues.put(DbHelper.groupTable.get(i),time);
                setNoChosen(i);
            }
            else {
                contentValues.put(DbHelper.groupTable.get(i),"");
            }
        }
        long result = database.insert(DbHelper.TABLE_GROUPS, null, contentValues);
        dbHelper.close();
        return result;
    }

    private void editInDb(int idInDb){
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(DbHelper.KEY_GROUP_NAME,String.valueOf(etGroupName.getText()));
        for(int i = 0; i < selectDays.size(); i ++){
            if(selectDays.get(i).isChosen()){
                contentValues.put(DbHelper.groupTable.get(i),time);
                setNoChosen(i);
            }
            else {
                contentValues.put(DbHelper.groupTable.get(i),"");
            }
        }
        database.update(DbHelper.TABLE_GROUPS, contentValues, DbHelper.KEY_ID + " = ?",new String[]{Integer.toString(idInDb)});
        dbHelper.close();
    }

    private void showTable(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_GROUPS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_GROUP_NAME);
            int monIndex = cursor.getColumnIndex(DbHelper.KEY_MON);
            int tueIndex = cursor.getColumnIndex(DbHelper.KEY_TUE);
            int wedIndex = cursor.getColumnIndex(DbHelper.KEY_WED);
            int thuIndex = cursor.getColumnIndex(DbHelper.KEY_THU);
            int friIndex = cursor.getColumnIndex(DbHelper.KEY_FRI);
            int satIndex = cursor.getColumnIndex(DbHelper.KEY_SAT);
            int sunIndex = cursor.getColumnIndex(DbHelper.KEY_SUN);
            Log.d("MyLog","ТАБЛИЦА ГРУППЫ");
            do {
                Log.d("MyLog", " id = " + cursor.getInt(idIndex) + " GroupName = " + cursor.getString(nameIndex) + ", Mon = " + cursor.getString(monIndex) + ", Tue = " + cursor.getString(tueIndex) + ", Wed = " + cursor.getString(wedIndex) + ", Thu = " + cursor.getString(thuIndex) + ", Fri = " + cursor.getString(friIndex) + ", Sat = " + cursor.getString(satIndex)+ ", Sun = " + cursor.getString(sunIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        cursor.close();

    }

    private void clearFields(){
        etGroupName.setText("");
        etTime.setText("");
    }

    private boolean checkFields(){
        boolean isDay = false;
        for(int i = 0; i < selectDays.size(); i++){
            if(selectDays.get(i).isChosen()){
                isDay = true;
            }
        }
        name = String.valueOf(etGroupName.getText());
        time = String.valueOf(etTime.getText());
        return (!time.isEmpty() && !name.isEmpty() && isDay);
    }

    /////////////////////////////////////////////////RECYCLERVIEW
    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void initData(){
        selectDays = new ArrayList<>();
        selectDays.add(new SelectDay("Понедельник"));
        selectDays.add(new SelectDay("Вторник"));
        selectDays.add(new SelectDay("Среда"));
        selectDays.add(new SelectDay("Четверг"));
        selectDays.add(new SelectDay("Пятница"));
        selectDays.add(new SelectDay("Суббота"));
        selectDays.add(new SelectDay("Воскресенье"));
    }

    private void initAdapter(){
        AddGroupAdapter adapter = new AddGroupAdapter(selectDays,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDayClick(int position) {
        if(selectDays.get(position).isChosen()){
            setNoChosen(position);
        }
        else {
            setChosen(position);
        }
    }

    private void setChosen(int position){
        selectDays.get(position).setFontColor(getResources().getColor(R.color.colorWhite));
        selectDays.get(position).setChosen(true);
        selectDays.get(position).setBackground(getResources().getColor(R.color.colorPrimary));
        recyclerView.getAdapter().notifyItemChanged(position);
    }

    public void setNoChosen(int position){
        selectDays.get(position).setFontColor(Color.GRAY);
        selectDays.get(position).setChosen(false);
        selectDays.get(position).setBackground(getResources().getColor(R.color.colorWhite));
        recyclerView.getAdapter().notifyItemChanged(position);
    }










}

