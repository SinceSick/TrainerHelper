package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GameEditActivity extends AppCompatActivity {

    private EditText firstPlayer,secondPlayer,etGameName;
    private String firstName,secondName,gameName;
    private LinearLayout startGame;
    private int fpId,spId;
    private SharedPreferences mData;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_edit);
        gameName = "";
        firstName = "";
        secondName = "";
        fpId = 0;
        spId = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        getSharedInfo();
        initListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mData.edit().remove("FirstPlayerName").apply();
        mData.edit().remove("FirstPlayerId").apply();
        mData.edit().remove("SecondPlayerName").apply();
        mData.edit().remove("SecondPlayerId").apply();
    }

    private void initView(){
        firstPlayer = findViewById(R.id.chooseYourPlayer);
        secondPlayer = findViewById(R.id.choosePlayer);
        startGame = findViewById(R.id.startGame);
        etGameName = findViewById(R.id.gameName);
        initToolbar();
    }
    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Выбор соперников");
    }

    private void getSharedInfo(){
        mData = getSharedPreferences("DataList", Context.MODE_PRIVATE);
        if(mData.contains("FirstPlayerName")){
            firstName = mData.getString("FirstPlayerName","");
            fpId = mData.getInt("FirstPlayerId",0);
            firstPlayer.setText(firstName);
        }
        if(mData.contains("SecondPlayerName")){
            secondName = mData.getString("SecondPlayerName","");
            spId = mData.getInt("SecondPlayerId",0);
            secondPlayer.setText(secondName);
        }
    }

    private void initListeners(){

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameName = etGameName.getText().toString();
                firstName = firstPlayer.getText().toString();
                secondName = secondPlayer.getText().toString();
                if(!gameName.isEmpty() && !firstName.isEmpty() && !secondName.isEmpty() && fpId != 0 && spId != 0){
                    Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                    intent.putExtra("FirstPlayerName",firstName);
                    intent.putExtra("SecondPlayerName",secondName);
                    intent.putExtra("GameName",gameName);
                    intent.putExtra("FpId",fpId);
                    intent.putExtra("SpId",spId);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Зполните все поля",Toast.LENGTH_SHORT).show();
                }
            }
        });

        firstPlayer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Intent intent = new Intent(getApplicationContext(),PlayerListActivity.class);
                    intent.putExtra("Game", 1);
                    mData = getSharedPreferences("DataList", Context.MODE_PRIVATE);
                    editor = mData.edit();
                    editor.putInt("Position",1);
                    editor.apply();
                    startActivity(intent);
                }
            }
        });

        secondPlayer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Intent intent = new Intent(getApplicationContext(),PlayerListActivity.class);
                    intent.putExtra("Game", 1);
                    mData = getSharedPreferences("DataList", Context.MODE_PRIVATE);
                    editor = mData.edit();
                    editor.putInt("Position",2);
                    editor.apply();
                    startActivity(intent);
                }
            }
        });



    }
}
