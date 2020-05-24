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
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainerhelper.adapter.GroupListAdapter;
import com.example.trainerhelper.pojo.GroupElement;
import com.example.trainerhelper.pojo.SelectDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends AppCompatActivity implements GroupListAdapter.GroupListener {

    private RecyclerView recyclerView;
    private List<GroupElement> groupElements;
    private int idInDb;
    private String name,time,days;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    private FloatingActionButton mAddGroupBtn;
    private SharedPreferences mData;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        initView();
        initRecyclerView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initAdapter();
    }


    private void initView(){

        mAddGroupBtn = findViewById(R.id.addGroupButton);

        initToolbar(toolbar);

        mAddGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initToolbar(Toolbar toolbar){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Список групп");
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void initData(){
        dbHelper = new DbHelper(this);
        DbHelper.initList();
        days = "";
        groupElements = new ArrayList<>();
        getOnDb();
    }

    private void getOnDb(){
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_GROUPS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_GROUP_NAME);
            do {
                idInDb = cursor.getInt(idIndex);
                name = cursor.getString(nameIndex);
                String cTime = "";
                List<String> dayList = new ArrayList<>();
                for(int i = 0; i < DbHelper.groupTable.size(); i++){
                    int columnIndex = cursor.getColumnIndex(DbHelper.groupTable.get(i));
                    cTime = cursor.getString(columnIndex);
                    if(!cTime.isEmpty()){
                        time = cTime;
                        dayList.add(DbHelper.groupTable.get(i));
                    }
                }
                groupElements.add(new GroupElement(idInDb,name,time,dayList));
                days = "";
                Log.d("MyLog","GET ON DB " + idInDb + " " + name + " " + time + " " + days);
            } while (cursor.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        cursor.close();
        dbHelper.close();
    }

    private boolean removeFromDb(int position){
        database = dbHelper.getWritableDatabase();
        database.execSQL("DELETE FROM " + DbHelper.TABLE_GROUPS + " WHERE " + DbHelper.TABLE_GROUPS +"."+ DbHelper.KEY_ID + " = " + "'" + groupElements.get(position).getIdInDb() + "'");

        Cursor cursor = database.query(DbHelper.TABLE_PLAYERS,new String[]{DbHelper.KEY_ID},DbHelper.KEY_GROUP + " =? ", new String[]{Integer.toString(groupElements.get(position).getIdInDb())},null,null,null);

        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            do{
                database.execSQL("DELETE FROM " + DbHelper.TABLE_PLAYERS + " WHERE " + DbHelper.TABLE_PLAYERS +"."+ DbHelper.KEY_ID + " = " + "'" + cursor.getInt(idIndex) + "'");
                database.execSQL("DELETE FROM " + DbHelper.TABLE_TRAIN_RESULTS + " WHERE " + DbHelper.TABLE_TRAIN_RESULTS +"."+ DbHelper.KEY_PLAYER_ID + " = " + "'" + cursor.getInt(idIndex) + "'");
            } while (cursor.moveToNext());
        }

        return true;
    }

    private void initAdapter(){
        GroupListAdapter adapter = new GroupListAdapter(groupElements,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(getApplicationContext(), AddGroupActivity.class);
        intent.putExtra("Edit", "true");
        intent.putExtra("IdInDb", groupElements.get(position).getIdInDb());
        intent.putExtra("Name", groupElements.get(position).getName());
        intent.putExtra("Time", groupElements.get(position).getTime());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        if(removeFromDb(position)){
            recyclerView.getAdapter().notifyItemRemoved(position);
            groupElements.remove(position);
        }
    }

    @Override
    public void onGroupClick(int position) {
        Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
        intent.putExtra("IdInDb",groupElements.get(position).getIdInDb());
        mData = getSharedPreferences("DataList", Context.MODE_PRIVATE);
        editor = mData.edit();
        editor.putInt("groupIdInDb",groupElements.get(position).getIdInDb());
        editor.apply();
        Log.d("MyLog","POST groupIdInDb" + mData.getInt("groupIdInDb",0));
        startActivity(intent);
    }
}
