package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PlayerInfoActivity extends AppCompatActivity {
    private TextView name,surname,patronymic,birthday,fon,fv,bon,bv,smash,serveIn,serve,run,chelnok,bt,jump,flex,choseDate;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    private List<String> dayList;
    private int playerIdInDb;
    private int currentDate;
    private ImageView next,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        initView();
        getInfo();
        initListeners();

    }

    private void initView(){
        choseDate = findViewById(R.id.choseDate);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        patronymic = findViewById(R.id.patronymic);
        birthday = findViewById(R.id.birthday);
        fon = findViewById(R.id.forehandOnGround);
        fv = findViewById(R.id.forehandOnVolley);
        bon = findViewById(R.id.backhandOnGround);
        bv = findViewById(R.id.backhandOnVolley);
        smash = findViewById(R.id.smash);
        serveIn = findViewById(R.id.serveIn);
        serve = findViewById(R.id.serve);
        run = findViewById(R.id.run_30m);
        chelnok = findViewById(R.id.chelnok);
        bt = findViewById(R.id.ball_throw);
        jump = findViewById(R.id.jump);
        flex = findViewById(R.id.flexibility);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        initToolbar();
    }

    private void initListeners(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentDate+1 < dayList.size()){
                    currentDate++;
                    choseDate.setText(dayList.get(currentDate));
                    getDbInfo();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentDate-1 >= 0){
                    currentDate--;
                    choseDate.setText(dayList.get(currentDate));
                    getDbInfo();
                }
            }
        });
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Данные игрока");
    }

    private void getInfo(){
        Intent intent = getIntent();
        currentDate = 0;
        playerIdInDb = intent.getIntExtra("PlayerIdInDb",0);
        name.setText(intent.getStringExtra("Name"));
        surname.setText(intent.getStringExtra("Surname"));
        patronymic.setText(intent.getStringExtra("Patronymic"));
        birthday.setText(intent.getStringExtra("Birthday"));
        getDateInDb();
        if(dayList.isEmpty()){
            Toast.makeText(getApplicationContext(),"Нет информации о данном игроке",Toast.LENGTH_SHORT).show();
        }
        else{
            getDbInfo();
        }
    }

    private void getDbInfo(){
        showTable();
        database = dbHelper.getWritableDatabase();
        choseDate.setText(dayList.get(currentDate));
        Cursor cursor = database.query(DbHelper.TABLE_TRAIN_RESULTS, null, DbHelper.KEY_PLAYER_ID + " =? " + " AND " + DbHelper.KEY_DATE + " =? ", new String[]{Integer.toString(playerIdInDb),dayList.get(currentDate)}, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
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
            Log.d("MyLog", "ТАБЛИЦА РЕЗУЛЬТАТЫ ТРЕНИРОВОК");
            do {
                fon.setText(cursor.getString(fonIndex));
                fv.setText(cursor.getString(fvIndex));
                bon.setText(cursor.getString(bonIndex));
                bv.setText(cursor.getString(bvIndex));
                smash.setText(cursor.getString(smashIndex));
                serveIn.setText(cursor.getString(serveInIndex));
                serve.setText(cursor.getString(serveIndex));
                String runResult = cursor.getInt(runIndex) + " сек";
                run.setText(runResult);
                String chelnokResult = cursor.getInt(chelnokIndex) + " сек";
                chelnok.setText(chelnokResult);
                String btResult = cursor.getInt(btIndex) + " м";
                bt.setText(btResult);
                String jumpResult = cursor.getInt(jumpIndex) + " см";
                jump.setText(jumpResult);
                String flexResult = cursor.getInt(flexIndex) + " см";
                flex.setText(flexResult);
                Log.d("MyLog", " id = " + cursor.getInt(idIndex) + " PlayerId = " + cursor.getInt(playerIndex) + " Date = " + cursor.getString(dateIndex) + " Fon = " + cursor.getString(fonIndex) + ", Fv = " + cursor.getString(fvIndex) + " Bon = " + cursor.getString(bonIndex) + " Bv = " + cursor.getString(bvIndex) + " Smash = " + cursor.getString(smashIndex)+ ", ServeIn = " + cursor.getString(serveInIndex) + " Serve = " + cursor.getString(serveIndex) + " Run = " + cursor.getInt(runIndex) + " Chelnok = " + cursor.getInt(chelnokIndex) + " Ball Trow = " + cursor.getInt(btIndex) + " Jump = " + cursor.getInt(jumpIndex) + " Flex = " + cursor.getInt(flexIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        dbHelper.close();
        cursor.close();
    }

    private void showTable(){
        database = dbHelper.getWritableDatabase();
        choseDate.setText(dayList.get(currentDate));
        Cursor cursor = database.query(DbHelper.TABLE_TRAIN_RESULTS, null, null, null,null,null,null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
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
            Log.d("MyLog", "ТАБЛИЦА РЕЗУЛЬТАТЫ ТРЕНИРОВОК");
            do {
                Log.d("MyLog", " id = " + cursor.getInt(idIndex) + " PlayerId = " + cursor.getInt(playerIndex) + " Date = " + cursor.getString(dateIndex) + " Fon = " + cursor.getString(fonIndex) + ", Fv = " + cursor.getString(fvIndex) + " Bon = " + cursor.getString(bonIndex) + " Bv = " + cursor.getString(bvIndex) + " Smash = " + cursor.getString(smashIndex)+ ", ServeIn = " + cursor.getString(serveInIndex) + " Serve = " + cursor.getString(serveIndex) + " Run = " + cursor.getInt(runIndex) + " Chelnok = " + cursor.getInt(chelnokIndex) + " Ball Trow = " + cursor.getInt(btIndex) + " Jump = " + cursor.getInt(jumpIndex) + " Flex = " + cursor.getInt(flexIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        dbHelper.close();
        cursor.close();
    }


    private void getDateInDb(){
        dbHelper = new DbHelper(this);
        dayList = new ArrayList<>();
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_TRAIN_RESULTS, null, DbHelper.KEY_PLAYER_ID + " =? ", new String[]{Integer.toString(playerIdInDb)}, null, null, null);
        if(cursor.moveToFirst()){
            int dateIndex = cursor.getColumnIndex(DbHelper.KEY_DATE);
            do{
                dayList.add(cursor.getString(dateIndex));
            } while (cursor.moveToNext());
        }
        else {
            Log.d("MyLog", "0 rows");
        }
        cursor.close();
        dbHelper.close();
    }
}
