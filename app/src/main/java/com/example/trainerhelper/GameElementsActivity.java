package com.example.trainerhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameElementsActivity extends AppCompatActivity {
    CardView hits,smash,serveIn,serve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_elements);
        initViewElements();
        initViewListeners();
    }

    private void initViewElements(){
        hits = findViewById(R.id.hits);
        smash = findViewById(R.id.smash);
        serveIn = findViewById(R.id.serveIn);
        serve = findViewById(R.id.serve);
    }

    private void initViewListeners(){
        hits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditHitsActivity.class);
                //intent.putExtra("Element", "hit");
                startActivity(intent);
            }
        });

        smash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingElementsActivity.class);
                intent.putExtra("Element","smash");
                startActivity(intent);
            }
        });

        serveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingElementsActivity.class);
                intent.putExtra("Element","serve_in");
                startActivity(intent);
            }
        });

        serve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingElementsActivity.class);
                intent.putExtra("Element","serve");
                startActivity(intent);
            }
        });
    }
}
