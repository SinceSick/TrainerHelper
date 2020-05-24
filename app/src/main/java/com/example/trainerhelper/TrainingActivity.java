package com.example.trainerhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TrainingActivity extends AppCompatActivity {
    private CardView playersCardView,gameElementsCardView,physicalCardView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        initViewElements(); //Элементы
        initListeners(); //Слушатели
    }
    private void initViewElements() {
        playersCardView = findViewById(R.id.players);
        gameElementsCardView = findViewById(R.id.gameElement);
        physicalCardView = findViewById(R.id.physical);
    }

    private void initListeners(){
        playersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getIntent();
                int idInDb = intent.getIntExtra("IdInDb", 0 );
                Intent intent = new Intent(getApplicationContext(), PlayerListActivity.class);
                startActivity(intent);
            }
        });

        gameElementsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameElementsActivity.class);
                startActivity(intent);
            }
        });

        physicalCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhysicalActivity.class);
                startActivity(intent);
            }
        });

    }
}
