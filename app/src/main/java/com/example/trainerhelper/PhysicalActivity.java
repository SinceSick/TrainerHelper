package com.example.trainerhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PhysicalActivity extends AppCompatActivity {
    CardView run,chelnok,ballThrow,jump,flex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);
        initViewElements();
        initViewListeners();
    }
    private void initViewElements(){
        run = findViewById(R.id.run_30m);
        chelnok = findViewById(R.id.chelnok);
        ballThrow = findViewById(R.id.ball_throw);
        jump = findViewById(R.id.jump);
        flex = findViewById(R.id.flexibility);
    }

    private void initViewListeners(){
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingPhysicalActivity.class);
                intent.putExtra("Element", "run_30m");
                startActivity(intent);
            }
        });

        chelnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingPhysicalActivity.class);
                intent.putExtra("Element","chelnok");
                startActivity(intent);
            }
        });

        ballThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingPhysicalActivity.class);
                intent.putExtra("Element","ball_throw");
                startActivity(intent);
            }
        });

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingPhysicalActivity.class);
                intent.putExtra("Element","jump");
                startActivity(intent);
            }
        });

        flex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrainingPhysicalActivity.class);
                intent.putExtra("Element","flexibility");
                startActivity(intent);
            }
        });
    }

}
