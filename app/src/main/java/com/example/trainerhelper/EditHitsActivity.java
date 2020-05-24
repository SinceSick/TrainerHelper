package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditHitsActivity extends AppCompatActivity {
    private CheckBox ground,volley,forehand,backhand;
    private FloatingActionButton next;
    private EditText etCount;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private String kind,type;
    private int hitsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hits);
        initViewElements();
        initViewListeners();
    }

    private void initViewElements(){
        ground = findViewById(R.id.onGround);
        volley = findViewById(R.id.volley);
        forehand = findViewById(R.id.forehand);
        backhand = findViewById(R.id.backhand);
        etCount = findViewById(R.id.hitsCount);
        next = findViewById(R.id.next);
        kind = "";
        type = "";
        initToolbar();
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Настройка");
    }

    private boolean checkFields(){
        hitsCount = 0;
        hitsCount = getHitsCount();
        return (!kind.isEmpty() && !type.isEmpty() && hitsCount != 0);
    }

    private void initViewListeners(){

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    Intent intent = new Intent(getApplicationContext(),TrainingElementsActivity.class);
                    intent.putExtra("Element","hit");
                    intent.putExtra("Kind", kind);
                    intent.putExtra("Type", type);
                    intent.putExtra("Count", hitsCount);
                    startActivity(intent);
                }
                else Toast.makeText(getApplicationContext(),"Укажите все параметры заного",Toast.LENGTH_SHORT).show();
            }
        });


        ground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ground.isChecked()){
                    volley.setChecked(false);
                    kind = "on_ground";
                }
            }
        });
        volley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volley.isChecked()){
                    ground.setChecked(false);
                    kind = "volley";
                }
            }
        });


        forehand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(forehand.isChecked()){
                    backhand.setChecked(false);
                    type = "forehand";
                }
            }
        });
        backhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(backhand.isChecked()){
                    forehand.setChecked(false);
                    type = "backhand";
                }
            }
        });


    }

    private int getHitsCount(){
        return Integer.parseInt(String.valueOf(etCount.getText()));
    }
}
