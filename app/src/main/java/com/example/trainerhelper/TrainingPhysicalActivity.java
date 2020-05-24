package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainerhelper.adapter.TrainingPhysicalAdapter;
import com.example.trainerhelper.pojo.PlayerElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingPhysicalActivity extends AppCompatActivity implements TrainingPhysicalAdapter.TrainingPlayersListener {

    private SharedPreferences mData;
    private SharedPreferences.Editor editor;
    private int idInDb,groupIdInDb;
    private String name,surname,patronymic,birthday,element,dialogText,toolbarTitle;
    private int result;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    private ImageView saveImage;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private List<PlayerElement> playerElements;
    private Dialog resultDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_physical);
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSharedInfo();
        initView();
        initData();
        initAdapter();
    }

    private void initView(){
        saveImage = findViewById(R.id.saveImage);
        initToolbar(toolbar);
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putInDb();
                finish();
                Toast.makeText(getApplicationContext(),"Данные успешно сохранены", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar(Toolbar toolbar){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(toolbarTitle);
    }

    private void getSharedInfo(){
        mData = getSharedPreferences("DataList", Context.MODE_PRIVATE);
        if(mData.contains("groupIdInDb")) {
            groupIdInDb = mData.getInt("groupIdInDb",0);
        }
        Intent intent = getIntent();
        switch (intent.getStringExtra("Element")){
            case "run_30m":
                element = "run_30m";
                dialogText = "Введите время:";
                toolbarTitle = "Бег 30м";
                break;
            case "chelnok":
                element = "chelnok";
                dialogText = "Введите время:";
                toolbarTitle = "Челночный бег";
                break;
            case "ball_throw":
                element = "ball_throw";
                dialogText = "Введите длину:";
                toolbarTitle = "Бросок мяча";
                break;
            case "jump":
                element = "jump";
                dialogText = "Введите длину:";
                toolbarTitle = "Прыжок с места";
                break;
            case "flexibility":
                element = "flexibility";
                dialogText = "Введите длину:";
                toolbarTitle = "Гибкость";
                break;
        }
    }

    private void initData(){
        dbHelper = new DbHelper(this);
        playerElements = new ArrayList<>();
        getOnDb();
        setPlayerResult();
    }

    private void getOnDb(){
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_PLAYERS, null, DbHelper.KEY_GROUP + " =? ", new String[]{Integer.toString(groupIdInDb)}, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DbHelper.KEY_SURNAME);
            int patronymicIndex = cursor.getColumnIndex(DbHelper.KEY_PATRONYMIC);
            int birthdayIndex = cursor.getColumnIndex(DbHelper.KEY_BIRTHDAY);
            do {
                idInDb = cursor.getInt(idIndex);
                name = cursor.getString(nameIndex);
                surname = cursor.getString(surnameIndex);
                patronymic = cursor.getString(patronymicIndex);
                birthday = cursor.getString(birthdayIndex);
                playerElements.add(new PlayerElement(idInDb, name, surname, patronymic,birthday));
            } while (cursor.moveToNext());
        } else
        cursor.close();
        dbHelper.close();
    }

    private void setPlayerResult(){
        for(int i = 0; i < playerElements.size(); i++){
            playerElements.get(i).setResult(result);
        }
    }

    private void putInDb(){
        database = dbHelper.getWritableDatabase();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        for(int i = 0; i<playerElements.size(); i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.KEY_PLAYER_ID, playerElements.get(i).getIdInDb());
            contentValues.put(element,playerElements.get(i).getResult());
            contentValues.put(DbHelper.KEY_DATE, date);
            Cursor cursor = database.query(DbHelper.TABLE_TRAIN_RESULTS,null, DbHelper.KEY_PLAYER_ID + " = ? " + " AND " + DbHelper.KEY_DATE + " = ? ", new String[]{Integer.toString(playerElements.get(i).getIdInDb()), date},null,null,null,null);
            if(cursor.getCount() == 0){
                database.insert(DbHelper.TABLE_TRAIN_RESULTS,null,contentValues);
            }
            else {
                database.update(DbHelper.TABLE_TRAIN_RESULTS,contentValues,DbHelper.KEY_PLAYER_ID + " = ? " + " AND " + DbHelper.KEY_DATE + " = ? ", new String[]{Integer.toString(playerElements.get(i).getIdInDb()), date});
            }
        }
        dbHelper.close();
        showTrainingTable();
    }

    private void showTrainingTable(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_TRAIN_RESULTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int playerIndex = cursor.getColumnIndex(DbHelper.KEY_PLAYER_ID);
            int dateIndex = cursor.getColumnIndex(DbHelper.KEY_DATE);
            int fonIndex = cursor.getColumnIndex(DbHelper.KEY_FOREHAND_ON_GROUND);
            int fvIndex = cursor.getColumnIndex(DbHelper.KEY_FOREHAND_VOLLEY);
            int bonIndex = cursor.getColumnIndex(DbHelper.KEY_BACKHAND_ON_GROUND);
            int bvIndex = cursor.getColumnIndex(DbHelper.KEY_BACKHAND_VOLLEY);
            int smashIndex = cursor.getColumnIndex(DbHelper.KEY_SMASH);
            int serveInIndex = cursor.getColumnIndex(DbHelper.KEY_SERVE_IN);
            int serveIndex = cursor.getColumnIndex(DbHelper.KEY_SERVE);
            int runIndex = cursor.getColumnIndex(DbHelper.KEY_RUN_30M);
            int chelnokIndex = cursor.getColumnIndex(DbHelper.KEY_CHELNOK);
            int btIndex = cursor.getColumnIndex(DbHelper.KEY_BALL_THROW);
            int jumpIndex = cursor.getColumnIndex(DbHelper.KEY_JUMP);
            int flexIndex = cursor.getColumnIndex(DbHelper.KEY_FLEXIBILITY);

            do {
                Log.d("MyLog", "SHOW TRAIN Player = " + cursor.getInt(playerIndex) + ", Date = " + cursor.getString(dateIndex) + ", Fon = " + cursor.getString(fonIndex) + ", Fv = " + cursor.getString(fvIndex) + ", Bon = " + cursor.getString(bonIndex) + ", Bv = " + cursor.getString(bvIndex) + ", Smash = " + cursor.getString(smashIndex)+ ", ServeIn = " + cursor.getString(serveInIndex) + ", Serve = " + cursor.getString(serveIndex) + " Run = " + cursor.getInt(runIndex) + " Chelnok = " + cursor.getInt(chelnokIndex) + " Ball Trow = " + cursor.getInt(btIndex) + " Jump = " + cursor.getInt(jumpIndex) + " Flex = " + cursor.getInt(flexIndex));            } while (cursor.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        cursor.close();
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void initAdapter(){
        TrainingPhysicalAdapter adapter = new TrainingPhysicalAdapter(playerElements,this);
        recyclerView.setAdapter(adapter);
    }

    private void showResultDialog(final int position){
        resultDialog = new Dialog(TrainingPhysicalActivity.this);
        resultDialog.setContentView(R.layout.dialog_result_view);
        resultDialog.show();
        TextView dialogTextView = resultDialog.findViewById(R.id.dialogText);
        dialogTextView.setText(dialogText);
        final EditText etResult = resultDialog.findViewById(R.id.result);
        Button saveInDialog = resultDialog.findViewById(R.id.saveInDialog);

        saveInDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etResult.getText().toString().isEmpty()){
                    result = Integer.parseInt(etResult.getText().toString());
                    playerElements.get(position).setResult(result);
                    resultDialog.dismiss();
                    initAdapter();
                }
            }
        });
    }

    @Override
    public void onPlayerClick(int position) {
        showResultDialog(position);
    }
}
