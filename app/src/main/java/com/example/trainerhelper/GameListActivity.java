package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trainerhelper.adapter.GameListAdapter;
import com.example.trainerhelper.adapter.PlayerListAdapter;
import com.example.trainerhelper.pojo.GameElement;
import com.example.trainerhelper.pojo.PlayerElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GameListActivity extends AppCompatActivity implements GameListAdapter.GameListener {

    private RecyclerView recyclerView;
    private List<GameElement> gameElements;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    private LinearLayout start;
    private Dialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        initView();
        initRecyclerView();
        initData();
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initAdapter();
    }

    private void initView(){
        start = findViewById(R.id.startGame);
        initToolbar(toolbar);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GameEditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initToolbar(Toolbar toolbar){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Список игр");
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void initData(){
        dbHelper = new DbHelper(this);
        gameElements = new ArrayList<>();
        getOnDb();
    }

    private void getOnDb(){
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_GAMES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int fpIndex = cursor.getColumnIndex(DbHelper.KEY_FIRST_PLAYER_ID);
            int spIndex = cursor.getColumnIndex(DbHelper.KEY_SECOND_PLAYER_ID);
            int dateIndex = cursor.getColumnIndex(DbHelper.KEY_GAME_DATE);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_GAME_NAME);
            int fpFSIndex = cursor.getColumnIndex(DbHelper.KEY_FP_FIRST_SET);
            int spFSIndex = cursor.getColumnIndex(DbHelper.KEY_SP_FIRST_SET);
            int fpSSIndex = cursor.getColumnIndex(DbHelper.KEY_FP_SECOND_SET);
            int spSSIndex = cursor.getColumnIndex(DbHelper.KEY_SP_SECOND_SET);
            int fpTSIndex = cursor.getColumnIndex(DbHelper.KEY_FP_THIRD_SET);
            int spTSIndex = cursor.getColumnIndex(DbHelper.KEY_SP_THIRD_SET);
            do {
                int idInDb = cursor.getInt(idIndex);
                int fp = cursor.getInt(fpIndex);
                int sp = cursor.getInt(spIndex);
                String fpName = getAbrName(fp);
                String spName = getAbrName(sp);
                String date = cursor.getString(dateIndex);
                String name = cursor.getString(nameIndex);
                int fpFirstSet = cursor.getInt(fpFSIndex);
                int spFirstSet = cursor.getInt(spFSIndex);
                int fpSecondSet = cursor.getInt(fpSSIndex);
                int spSecondSet = cursor.getInt(spSSIndex);
                int fpThirdSet = cursor.getInt(fpTSIndex);
                int spThirdSet = cursor.getInt(spTSIndex);
                gameElements.add(new GameElement(idInDb, fpName, spName, date,name,fpFirstSet,spFirstSet,fpSecondSet,spSecondSet,fpThirdSet,spThirdSet));
            } while (cursor.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        cursor.close();
        dbHelper.close();
    }

    private String getAbrName(int idInDb){
        Cursor abrCursor = database.query(DbHelper.TABLE_PLAYERS,null,DbHelper.KEY_ID + " =? ",new String[]{Integer.toString(idInDb)},null,null,null);
        if (abrCursor.moveToFirst()) {
            int nameIndex = abrCursor.getColumnIndex(DbHelper.KEY_NAME);
            int surnameIndex = abrCursor.getColumnIndex(DbHelper.KEY_SURNAME);
            int patronymicIndex = abrCursor.getColumnIndex(DbHelper.KEY_PATRONYMIC);
            String name = abrCursor.getString(nameIndex);
            String surname = abrCursor.getString(surnameIndex);
            String patronymic = abrCursor.getString(patronymicIndex);
            abrCursor.close();
            return surname + " " + name.substring(0,1) + ". " + patronymic.substring(0,1) + ".";
        }
        else {
            abrCursor.close();
            return "Игрок удален";
        }
    }

    private void initAdapter(){
        GameListAdapter adapter = new GameListAdapter(gameElements,this);
        recyclerView.setAdapter(adapter);
    }

    private void showInfoDialog(final int position){
        infoDialog = new Dialog(GameListActivity.this);
        infoDialog.setContentView(R.layout.dialog_game_info);
        int width = (int)(getResources().getDisplayMetrics().widthPixels);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.85);
        infoDialog.getWindow().setLayout(width,height);
        infoDialog.show();

        TextView dFpName = infoDialog.findViewById(R.id.fpName);
        TextView dSpName = infoDialog.findViewById(R.id.spName);
        TextView dFpFirstSet = infoDialog.findViewById(R.id.fpFirstSet);
        TextView dFpSecondSet = infoDialog.findViewById(R.id.fpSecondSet);
        TextView dFpThirdSet = infoDialog.findViewById(R.id.fpThirdSet);
        TextView dSpFirstSet = infoDialog.findViewById(R.id.spFirstSet);
        TextView dSpSecondSet = infoDialog.findViewById(R.id.spSecondSet);
        TextView dSpThirdSet = infoDialog.findViewById(R.id.spThirdSet);

        TextView dFpTotalPointsWon = infoDialog.findViewById(R.id.fpTotalPointsWon);
        TextView dSpTotalPointsWon = infoDialog.findViewById(R.id.spTotalPointsWon);
        TextView dFpFirstServeIn = infoDialog.findViewById(R.id.fpFirstServeIn);
        TextView dSpFirstServeIn = infoDialog.findViewById(R.id.spFirstServeIn);
        TextView dFpFirstServePointsWon = infoDialog.findViewById(R.id.fpFirstServePointsWon);
        TextView dSpFirstServePointsWon = infoDialog.findViewById(R.id.spFirstServePointsWon);
        TextView dFpSecondServeIn = infoDialog.findViewById(R.id.fpSecondServeIn);
        TextView dSpSecondServeIn = infoDialog.findViewById(R.id.spSecondServeIn);
        TextView dFpSecondServePointsWon = infoDialog.findViewById(R.id.fpSecondServePointsWon);
        TextView dSpSecondServePointsWon = infoDialog.findViewById(R.id.spSecondServePointsWon);
        TextView dFpAce = infoDialog.findViewById(R.id.fpAce);
        TextView dSpAce = infoDialog.findViewById(R.id.spAce);
        TextView dFpDoubleFaults = infoDialog.findViewById(R.id.fpDoubleFaults);
        TextView dSpDoubleFaults = infoDialog.findViewById(R.id.spDoubleFaults);
        TextView dFpWinners = infoDialog.findViewById(R.id.fpWinners);
        TextView dSpWinners = infoDialog.findViewById(R.id.spWinners);
        TextView dFpUnforcedErrors = infoDialog.findViewById(R.id.fpUnforcedErrors);
        TextView dSpUnforcedErrors = infoDialog.findViewById(R.id.spUnforcedErrors);


        dFpName.setText(gameElements.get(position).getFpName());
        dSpName.setText(gameElements.get(position).getSpName());
        dFpFirstSet.setText(String.valueOf(gameElements.get(position).getFpFirstSet()));
        dSpFirstSet.setText(String.valueOf(gameElements.get(position).getSpFirstSet()));
        dFpSecondSet.setText(String.valueOf(gameElements.get(position).getFpSecondSet()));
        dSpSecondSet.setText(String.valueOf(gameElements.get(position).getSpSecondSet()));
        dFpThirdSet.setText(String.valueOf(gameElements.get(position).getFpThirdSet()));
        dSpThirdSet.setText(String.valueOf(gameElements.get(position).getSpThirdSet()));

        int fpId = 0;
        int spId = 0;
        database = dbHelper.getWritableDatabase();
        Cursor cFindFpId = database.query(DbHelper.TABLE_GAMES,new String[]{DbHelper.KEY_FIRST_PLAYER_STATISTIC_ID},DbHelper.KEY_ID + " =? ",new String[]{Integer.toString(gameElements.get(position).getIdInDb())},null,null,null);
        if(cFindFpId.moveToFirst()){
            int fpIdIndex = cFindFpId.getColumnIndex(DbHelper.KEY_FIRST_PLAYER_STATISTIC_ID);
            do{
               fpId = cFindFpId.getInt(fpIdIndex);
            } while (cFindFpId.moveToNext());
        }else {
            Log.d("MyLog","0 rows");
        }
        cFindFpId.close();

        Cursor cFindSpId = database.query(DbHelper.TABLE_GAMES,new String[]{DbHelper.KEY_SECOND_PLAYER_STATISTIC_ID},DbHelper.KEY_ID + " =? ",new String[]{Integer.toString(gameElements.get(position).getIdInDb())},null,null,null);
        if(cFindSpId.moveToFirst()){
            int spIdIndex = cFindSpId.getColumnIndex(DbHelper.KEY_SECOND_PLAYER_STATISTIC_ID);
            do{
                spId = cFindSpId.getInt(spIdIndex);
            } while (cFindSpId.moveToNext());
        }else {
            Log.d("MyLog","0 rows");
        }
        cFindSpId.close();

        Cursor cursor1 = database.query(DbHelper.TABLE_GAME_STATISTIC,null,DbHelper.KEY_ID + " =? ",new String[]{Integer.toString(fpId)},null,null,null);
        if(cursor1.moveToFirst()){
            int totalPointsWonIdIndex = cursor1.getColumnIndex(DbHelper.KEY_TOTAL_POINTS_WON);
            int fsInIndex = cursor1.getColumnIndex(DbHelper.KEY_FIRST_SERVE_IN);
            int fsPointWonIndex = cursor1.getColumnIndex(DbHelper.KEY_FIRST_SERVE_POINTS_WON);
            int ssInIndex = cursor1.getColumnIndex(DbHelper.KEY_SECOND_SERVE_IN);
            int ssPointWonIndex = cursor1.getColumnIndex(DbHelper.KEY_SECOND_SERVE_POINTS_WON);
            int acesIndex = cursor1.getColumnIndex(DbHelper.KEY_ACES);
            int dfIndex = cursor1.getColumnIndex(DbHelper.KEY_DOUBLE_FAULTS);
            int winnersIndex = cursor1.getColumnIndex(DbHelper.KEY_WINNERS);
            int ueIndex = cursor1.getColumnIndex(DbHelper.KEY_UNFORCED_ERRORS);
            do{
                dFpTotalPointsWon.setText(cursor1.getString(totalPointsWonIdIndex));
                dFpFirstServeIn.setText(cursor1.getString(fsInIndex));
                dFpFirstServePointsWon.setText(cursor1.getString(fsPointWonIndex));
                dFpSecondServeIn.setText(cursor1.getString(ssInIndex));
                dFpSecondServePointsWon.setText(cursor1.getString(ssPointWonIndex));
                dFpAce.setText(String.valueOf(cursor1.getInt(acesIndex)));
                dFpDoubleFaults.setText(String.valueOf(cursor1.getInt(dfIndex)));
                dFpWinners.setText(String.valueOf(cursor1.getInt(winnersIndex)));
                dFpUnforcedErrors.setText(String.valueOf(cursor1.getInt(ueIndex)));
            } while (cursor1.moveToNext());
        }else {
            Log.d("MyLog","0 rows");
        }
        cursor1.close();

        Cursor cursor2 = database.query(DbHelper.TABLE_GAME_STATISTIC,null,DbHelper.KEY_ID + " =? ",new String[]{Integer.toString(spId)},null,null,null);
        if(cursor2.moveToFirst()){
            int totalPointsWonIdIndex = cursor2.getColumnIndex(DbHelper.KEY_TOTAL_POINTS_WON);
            int fsInIndex = cursor2.getColumnIndex(DbHelper.KEY_FIRST_SERVE_IN);
            int fsPointWonIndex = cursor2.getColumnIndex(DbHelper.KEY_FIRST_SERVE_POINTS_WON);
            int ssInIndex = cursor2.getColumnIndex(DbHelper.KEY_SECOND_SERVE_IN);
            int ssPointWonIndex = cursor2.getColumnIndex(DbHelper.KEY_SECOND_SERVE_POINTS_WON);
            int acesIndex = cursor2.getColumnIndex(DbHelper.KEY_ACES);
            int dfIndex = cursor2.getColumnIndex(DbHelper.KEY_DOUBLE_FAULTS);
            int winnersIndex = cursor2.getColumnIndex(DbHelper.KEY_WINNERS);
            int ueIndex = cursor2.getColumnIndex(DbHelper.KEY_UNFORCED_ERRORS);
            do{
                dSpTotalPointsWon.setText(cursor2.getString(totalPointsWonIdIndex));
                dSpFirstServeIn.setText(cursor2.getString(fsInIndex));
                dSpFirstServePointsWon.setText(cursor2.getString(fsPointWonIndex));
                dSpSecondServeIn.setText(cursor2.getString(ssInIndex));
                dSpSecondServePointsWon.setText(cursor2.getString(ssPointWonIndex));
                dSpAce.setText(String.valueOf(cursor2.getInt(acesIndex)));
                dSpDoubleFaults.setText(String.valueOf(cursor2.getInt(dfIndex)));
                dSpWinners.setText(String.valueOf(cursor2.getInt(winnersIndex)));
                dSpUnforcedErrors.setText(String.valueOf(cursor2.getInt(ueIndex)));
            } while (cursor2.moveToNext());
        }else {
            Log.d("MyLog","0 rows");
        }
        cursor2.close();
        dbHelper.close();
    }

    @Override
    public void onGameClick(int position) {
        showInfoDialog(position);
    }
}
