package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.trainerhelper.adapter.PlayerListAdapter;
import com.example.trainerhelper.pojo.PlayerElement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PlayerListActivity extends AppCompatActivity implements PlayerListAdapter.PlayerListener {
    private RecyclerView recyclerView;
    private List<PlayerElement> playerElements;
    private int idInDb,groupIdInDb;
    private int gameIntent,playerPosition;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    private FloatingActionButton mAddPlayerBtn;
    private Intent intent;
    private SharedPreferences mData;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        initView();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSharedInfo();
        initData();
        initAdapter();
    }

    private void getSharedInfo(){
        intent = getIntent();
        gameIntent = intent.getIntExtra("Game",0);
        mData = getSharedPreferences("DataList", Context.MODE_PRIVATE);
        if(gameIntent == 1){
            playerPosition = mData.getInt("Position", 0);
            Log.d("MyLog", "Position = " + playerPosition);
            Toast.makeText(getApplicationContext(),"Если нужного игрока нет, то его нужно добавить", Toast.LENGTH_LONG).show();
        }
        if(gameIntent != 1){
            if (mData.contains("groupIdInDb")) {
                groupIdInDb = mData.getInt("groupIdInDb", 0);
                Log.d("MyLog", groupIdInDb + " GET");
            }
        }
    }

    private void initView(){
        mAddPlayerBtn = findViewById(R.id.addPlayerButton);
        initToolbar(toolbar);
        mAddPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyLog", groupIdInDb + "");
                Intent intent = new Intent(getApplicationContext(), AddPlayerActivity.class);
                intent.putExtra("IdInDb", idInDb);
                if(gameIntent == 1){
                    intent.putExtra("groupIdInDb", 0);
                }
                else {
                    intent.putExtra("groupIdInDb", groupIdInDb);
                }
                Log.d("MyLog", groupIdInDb + " intent SEND");
                startActivity(intent);
            }
        });
    }

    private void initToolbar(Toolbar toolbar){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Список игроков");
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void initData(){
        dbHelper = new DbHelper(this);
        playerElements = new ArrayList<>();
        //Log.d("IDID", groupIdInDb + "");
        getOnDb();
        if(gameIntent == 1){
            for(int i = 0; i < playerElements.size();i++){
                playerElements.get(i).setGame(1);
            }
        }
    }

    private void getOnDb(){
        database = dbHelper.getWritableDatabase();
        Cursor cursor;
        if(gameIntent != 1){
            cursor = database.query(DbHelper.TABLE_PLAYERS, null, DbHelper.KEY_GROUP + " =? ", new String[]{Integer.toString(groupIdInDb)}, null, null, null);
        }
        else {
            cursor = database.query(DbHelper.TABLE_PLAYERS, null, null, null, null, null, null);

        }
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DbHelper.KEY_SURNAME);
            int patronymicIndex = cursor.getColumnIndex(DbHelper.KEY_PATRONYMIC);
            int birthdayIndex = cursor.getColumnIndex(DbHelper.KEY_BIRTHDAY);
            //int cGroupIdInDbIndex = cursor.getColumnIndex(DbHelper.KEY_GROUP);
            do {
                idInDb = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String surname = cursor.getString(surnameIndex);
                String patronymic = cursor.getString(patronymicIndex);
                String birthday = cursor.getString(birthdayIndex);
                //int cGroupId = cursor.getInt(cGroupIdInDbIndex);

                //if(cGroupId == groupIdInDb) {
                    Log.d("readOnDbLog", idInDb + " " + name + " " + surname + " " + patronymic);
                    playerElements.add(new PlayerElement(idInDb, name, surname, patronymic,birthday));
                //}
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();
        dbHelper.close();
    }

    private void initAdapter(){
        PlayerListAdapter adapter = new PlayerListAdapter(playerElements,this);
        recyclerView.setAdapter(adapter);
    }

    private boolean removeFromDb(int position){
        database = dbHelper.getWritableDatabase();
        database.execSQL("DELETE FROM " + DbHelper.TABLE_PLAYERS + " WHERE " + DbHelper.TABLE_PLAYERS +"."+ DbHelper.KEY_ID + " = " + "'" + playerElements.get(position).getIdInDb() + "'");
        return true;
    }


    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(getApplicationContext(), AddPlayerActivity.class);
        intent.putExtra("Edit", "true");
        intent.putExtra("IdInDb", playerElements.get(position).getIdInDb());
        intent.putExtra("groupIdInDb", groupIdInDb);
        Log.d("PlayerList", groupIdInDb + "intent SEND");
        intent.putExtra("Name", playerElements.get(position).getName());
        intent.putExtra("Surname", playerElements.get(position).getSurname());
        intent.putExtra("Patronymic", playerElements.get(position).getPatronymic());
        intent.putExtra("Birthday", playerElements.get(position).getBirthday());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        if(removeFromDb(position)){
            recyclerView.getAdapter().notifyItemRemoved(position);
            playerElements.remove(position);
        }
    }

    @Override
    public void onPlayerClick(int position) {
        if(gameIntent != 1){
            Intent intent = new Intent(getApplicationContext(), PlayerInfoActivity.class);
            intent.putExtra("Name",playerElements.get(position).getName());
            intent.putExtra("PlayerIdInDb",playerElements.get(position).getIdInDb());
            intent.putExtra("Surname",playerElements.get(position).getSurname());
            intent.putExtra("Patronymic",playerElements.get(position).getPatronymic());
            intent.putExtra("Birthday",playerElements.get(position).getBirthday());
            startActivity(intent);
        }
        else {
            editor = mData.edit();
            Intent intent = new Intent(getApplicationContext(), GameEditActivity.class);
            if(playerPosition == 1){
                editor.putString("FirstPlayerName",playerElements.get(position).getAbr());
                editor.putInt("FirstPlayerId",playerElements.get(position).getIdInDb());
            }
            else {
                editor.putString("SecondPlayerName",playerElements.get(position).getAbr());
                editor.putInt("SecondPlayerId",playerElements.get(position).getIdInDb());
            }
            editor.apply();
            startActivity(intent);
        }
    }
}
