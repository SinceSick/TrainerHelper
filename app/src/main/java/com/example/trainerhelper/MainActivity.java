package com.example.trainerhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CardView trainingCardView,gameCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViewElements(); //Элементы
        initialiseListners(); //Слушатели
    }

    private void initialiseViewElements() {
        trainingCardView = findViewById(R.id.training_cardView);
        gameCardView = findViewById(R.id.game_cardView);
    }

    private void initialiseListners(){
        trainingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupListActivity.class);
                startActivity(intent);
            }
        });

        gameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameListActivity.class);
                startActivity(intent);
            }
        });
    }
}
