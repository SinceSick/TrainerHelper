package com.example.trainerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

public class GameActivity extends AppCompatActivity {

    private TextView fpNameOnSetCount,spNameOnSetCount,fpFirstSet,fpSecondSet,fpThirdSet,spFirstSet,spSecondSet,spThirdSet;
    private String firstPlayerName,secondPlayerName,gameName,firstServeInTieBreak;
    private int fpFs,fpSs,fpTs, spFs,spSs,spTs,currentSet,fpWinSets,spWinSets,currentServe;
    private boolean isTieBreak;

    private TextView fpNameOnGameCount,spNameOnGameCount,inGameCountTextView;
    private String inGameCount,fpInGameCountString,spInGameCountString;
    private int fpInGameCount,spInGameCount;

    private LinearLayout currentActionLinearLayout,winnerLinearLayout;
    private TextView currentActionTextView,winnerTextView;
    private String currentAction;

    private LinearLayout forChooseServe;
    private CardView fpServeFirst,spServeFirst;
    private String currentServePlayer;

    private LinearLayout forServe,fpLayoutForServe,spLayoutForServe;
    private CardView fpAceCardView,fpServeInCardView,fpFaultCardView,spAceCardView,spServeInCardView,spFaultCardView;
    private TextView fpNameServeFirst,spNameServeFirst;
    private int serveFault;

    private LinearLayout forServeIn;
    private CardView fpWinnersCardView,fpUnforcedErrorCardView,spWinnersCardView,spUnforcedErrorCardView;

    private String winner;
    private int fpId,spId,winnerId,fpStatId,spStatId;
    private int totalPoints;
    private int fpTotalPointsWon,spTotalPointsWon,fpFirstServeTotal,fpFirstServeCount,fpFirstServePointsCount,spFirstServeTotal,spFirstServeCount,spFirstServePointsCount;
    private int fpSecondServeTotal,fpSecondServeCount,fpSecondServePointsCount,spSecondServeTotal,spSecondServeCount,spSecondServePointsCount;
    private int fpAces,spAces,fpDoubleFaults,spDoubleFaults,fpWinners,spWinners,fpUnforcedError,spUnforcedError;

    private Intent intent;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    private Toolbar toolbar;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViewElements();
        initCountVariables();
        getIntentInfo();
        setViewElements();
        initListeners();
        //initToolbar(toolbar);
    }

    private void initToolbar(Toolbar toolbar){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Игра окончена");
    }

    private void initViewElements(){
        /////////////////////////////////////////////SET COUNT
        fpNameOnSetCount = findViewById(R.id.fpName);
        spNameOnSetCount = findViewById(R.id.spName);
        fpFirstSet = findViewById(R.id.fpFirstSet);
        fpSecondSet = findViewById(R.id.fpSecondSet);
        fpThirdSet = findViewById(R.id.fpThirdSet);
        spFirstSet = findViewById(R.id.spFirstSet);
        spSecondSet = findViewById(R.id.spSecondSet);
        spThirdSet = findViewById(R.id.spThirdSet);
        /////////////////////////////////////////////GAME COUNT
        fpNameOnGameCount = findViewById(R.id.fpNameOnCount);
        spNameOnGameCount = findViewById(R.id.spNameOnCount);
        inGameCountTextView = findViewById(R.id.gameCount);
        ////////////////////////////////////////////CURRENT ACTION TEXT VIEW
        currentActionLinearLayout = findViewById(R.id.currentActionLinearLayout);
        currentActionTextView = findViewById(R.id.currentActionTextView);
        winnerLinearLayout = findViewById(R.id.winnerLayout);
        winnerTextView = findViewById(R.id.winnerTextView);
        ////////////////////////////////////////////CHOOSE SERVE
        forChooseServe = findViewById(R.id.layoutForChooseServe);
        fpNameServeFirst = findViewById(R.id.fpNameServeFirst);
        spNameServeFirst = findViewById(R.id.spNameServeFirst);
        fpServeFirst = findViewById(R.id.fpServeFirst);
        spServeFirst = findViewById(R.id.spServeFirst);
        ////////////////////////////////////////////SERVE
        forServe = findViewById(R.id.layoutForServe);
        fpLayoutForServe = findViewById(R.id.fpLayoutForServe);
        spLayoutForServe = findViewById(R.id.spLayoutForServe);
        fpAceCardView = findViewById(R.id.fpAce);
        spAceCardView = findViewById(R.id.spAce);
        fpServeInCardView = findViewById(R.id.fpServeIn);
        spServeInCardView = findViewById(R.id.spServeIn);
        fpFaultCardView = findViewById(R.id.fpFault);
        spFaultCardView = findViewById(R.id.spFault);
        /////////////////////////////////////////////BALL IN GAME
        forServeIn = findViewById(R.id.layoutForServeIn);
        fpWinnersCardView = findViewById(R.id.fpWinners);
        spWinnersCardView = findViewById(R.id.spWinners);
        fpUnforcedErrorCardView = findViewById(R.id.fpUnforcedError);
        spUnforcedErrorCardView = findViewById(R.id.spUnforcedError);
    }

    private void initCountVariables(){
        initOnSetCount();
        initString();
        initStatisticVariables();
    }

    private void initStatisticVariables(){
        fpStatId = 0;
        spStatId = 0;
        totalPoints = 0;
        fpTotalPointsWon = 0;
        spTotalPointsWon = 0;
        fpFirstServeTotal = 0;
        spFirstServeTotal = 0;
        fpFirstServeCount = 0;
        spFirstServeCount = 0;
        fpFirstServePointsCount = 0;
        spFirstServePointsCount = 0;
        fpSecondServeTotal = 0;
        spSecondServeTotal = 0;
        fpSecondServeCount = 0;
        spSecondServeCount = 0;
        fpSecondServePointsCount = 0;
        spSecondServePointsCount = 0;
        fpAces = 0;
        spAces = 0;
        fpDoubleFaults = 0;
        spDoubleFaults = 0;
        fpWinners = 0;
        spWinners = 0;
        fpUnforcedError = 0;
        spUnforcedError = 0;
    }

    private void initOnSetCount(){
        isTieBreak = false;
        currentServe = 1;
        fpFs = 0;
        fpSs = 0;
        fpTs = 0;
        spFs = 0;
        spSs = 0;
        spTs = 0;
        fpInGameCount = 0;
        spInGameCount = 0;
        serveFault = 0;
        currentSet = 1;
        fpWinSets = 0;
        spWinSets = 0;

    }

    private void initString(){
        winner = "";
        fpInGameCountString = "00";
        spInGameCountString = "00";
        inGameCount = "00:00";
        currentAction = "Выбор подающего";
        currentServePlayer = "";
    }

    private void getIntentInfo(){
        intent = getIntent();
        firstPlayerName = intent.getStringExtra("FirstPlayerName");
        secondPlayerName = intent.getStringExtra("SecondPlayerName");
        gameName = intent.getStringExtra("GameName");
        fpId = intent.getIntExtra("FpId",0);
        spId = intent.getIntExtra("SpId",0);
    }

    private void setViewElements(){
        setNameView();
        setSetCountView();
        setGameCountView();
        setCurrentActionView();
    }

    private void setNameView(){
        fpNameOnSetCount.setText(firstPlayerName);
        spNameOnSetCount.setText(secondPlayerName);
        fpNameOnGameCount.setText(firstPlayerName);
        spNameOnGameCount.setText(secondPlayerName);
        fpNameServeFirst.setText(firstPlayerName);
        spNameServeFirst.setText(secondPlayerName);
    }

    private void setSetCountView(){
        fpFirstSet.setText(String.valueOf(fpFs));
        spFirstSet.setText(String.valueOf(spFs));
        fpSecondSet.setText(String.valueOf(fpSs));
        spSecondSet.setText(String.valueOf(spSs));
        fpThirdSet.setText(String.valueOf(fpTs));
        spThirdSet.setText(String.valueOf(spTs));
    }

    private void setGameCountView(){
        inGameCount = fpInGameCountString + ":" + spInGameCountString;
        inGameCountTextView.setText(inGameCount);
    }

    private void setCurrentActionView(){
        currentActionTextView.setText(currentAction);
    }

    private void fpWinBall(){
        fpInGameCount++;
        if(isTieBreak == false){
            switch (fpInGameCount) {
                case 0:
                    fpInGameCountString = "00";
                    break;
                case 1:
                    fpInGameCountString = "15";
                    break;
                case 2:
                    fpInGameCountString = "30";
                    break;
                case 3:
                    fpInGameCountString = "40";
                    break;
                case 4:
                    if (spInGameCount == 3) {
                        fpInGameCountString = "add";
                        spInGameCountString = "  ";
                    } else if (spInGameCount < 3) {
                        fpWinGame();
                    }
            }
            if(fpInGameCount >= 3 && spInGameCount >=3 && fpInGameCount == spInGameCount){
                fpInGameCountString = "40";
                spInGameCountString = "40";
            }
            if(fpInGameCount >= 3 && spInGameCount >=3 && fpInGameCount == spInGameCount+1){
                fpInGameCountString = "add";
                spInGameCountString = "  ";
            }
            if(fpInGameCount >=3 && spInGameCount >= 3 && fpInGameCount == spInGameCount+2){
                fpWinGame();
            }


        } else {///////////////////////////TIE BREAK
            if(fpInGameCount + spInGameCount == 1){
                changeServePlayer();
            }
            if(fpInGameCount + spInGameCount != 1 && (fpInGameCount + spInGameCount) % 2 == 1 ){
                changeServePlayer();
            }
            if(fpInGameCount > 5 && spInGameCount > 5 && fpInGameCount == spInGameCount+2){
                fpWinSet();
            }else if(fpInGameCount == 7 && spInGameCount < 6){
                fpWinSet();
            }
            fpInGameCountString = String.valueOf(fpInGameCount);
        }
        setGameCountView();
    }

    private void fpWinGame(){
        switch (currentSet){
            case 1:
                fpFs++;
                if(fpFs == 7 && spFs == 5){
                    fpWinSet();
                }else if(fpFs == 6 && spFs<5){
                    fpWinSet();
                }else if(fpFs == 6 && spFs == 6){
                    tieBreak();
                }
                break;
            case 2:
                fpSs++;
                if(fpSs == 7 && spSs == 5){
                    fpWinSet();
                }else if(fpSs == 6 && spSs<5){
                    fpWinSet();
                }
                break;
            case 3:
                fpTs++;
                if(fpTs == 7 && spTs == 5){
                    fpWinSet();
                }else if(fpTs == 6 && spTs<5){
                    fpWinSet();
                }
                break;
        }
        nullVariables();
        setSetCountView();
        setGameCountView();
        changeServePlayer();
    }

    private void fpWinSet(){
        fpWinSets++;
        if(isTieBreak == true){
            isTieBreak = false;
            if(currentServePlayer.equals(firstServeInTieBreak)){
                changeServePlayer();
            }
            switch (currentSet){
                case 1:
                    fpFs = 7;
                    spFs = 6;
                    break;
                case 2:
                    fpSs = 7;
                    spSs = 6;
                    break;
                case 3:
                    fpTs = 7;
                    spTs = 6;
                    break;
            }
        }
        setSetCountView();
        if(fpWinSets == 2){
            winner = firstPlayerName;
            currentActionLinearLayout.setVisibility(View.GONE);
            winnerLinearLayout.setVisibility(View.VISIBLE);
            winnerTextView.setText(winner);
            winnerId = fpId;
            putInDb();
        }else {
            nullVariables();
            setGameCountView();
            returnToServe();
            currentSet++;
        }

    }

    private void spWinBall(){
        spInGameCount++;
        if(isTieBreak == false){
            switch (spInGameCount) {
                case 0:
                    spInGameCountString = "00";
                    break;
                case 1:
                    spInGameCountString = "15";
                    break;
                case 2:
                    spInGameCountString = "30";
                    break;
                case 3:
                    spInGameCountString = "40";
                    break;
                case 4:
                    if (fpInGameCount == 3) {
                        spInGameCountString = "add";
                        fpInGameCountString = "  ";
                    } else if (fpInGameCount < 3) {
                        spWinGame();
                    }
            }
            if(spInGameCount >= 3 && fpInGameCount >=3 && spInGameCount == fpInGameCount){
                fpInGameCountString = "40";
                spInGameCountString = "40";
            }
            if(spInGameCount >= 3 && fpInGameCount >=3 && spInGameCount == fpInGameCount+1){
                spInGameCountString = "add";
                fpInGameCountString = "  ";
            }
            if(spInGameCount >=3 && fpInGameCount >= 3 && spInGameCount == fpInGameCount+2){
                spWinGame();
            }

        } else {///////////////////////////TIE BREAK
            if(fpInGameCount + spInGameCount == 1){
                changeServePlayer();
            }
            if(fpInGameCount + spInGameCount != 1 && (fpInGameCount + spInGameCount) % 2 == 1 ){
                changeServePlayer();
            }
            if(spInGameCount > 5 && fpInGameCount > 5 && spInGameCount == fpInGameCount+2){
                spWinSet();
            }else if(spInGameCount == 7 && fpInGameCount < 6){
                spWinSet();
            }
            spInGameCountString = String.valueOf(spInGameCount);
        }

        setGameCountView();
    }

    private void spWinGame(){
        switch (currentSet){
            case 1:
                spFs++;
                if(spFs == 7 && fpFs == 5){
                    spWinSet();
                }else if(spFs == 6 && fpFs<5){
                    spWinSet();
                }else if(spFs == 6 && fpFs == 6){
                    tieBreak();
                }
                break;
            case 2:
                spSs++;
                if(spSs == 7 && fpSs == 5){
                    spWinSet();
                }else if(spSs == 6 && fpSs<5){
                    spWinSet();
                }
                break;
            case 3:
                spTs++;
                if(spTs == 7 && fpTs == 5){
                    spWinSet();
                }else if(spTs == 6 && fpTs<5){
                    spWinSet();
                }
                break;
        }

        nullVariables();
        setSetCountView();
        setGameCountView();
        changeServePlayer();
    }

    private void spWinSet(){
        spWinSets++;
        if(isTieBreak == true){
            isTieBreak = false;
            if(currentServePlayer.equals(firstServeInTieBreak)){
                changeServePlayer();
            }
            switch (currentSet){
                case 1:
                    spFs = 7;
                    fpFs = 6;
                    break;
                case 2:
                    spSs = 7;
                    fpSs = 6;
                    break;
                case 3:
                    spTs = 7;
                    fpTs = 6;
                    break;
            }
        }
        setSetCountView();
        if(spWinSets == 2){
            winner = secondPlayerName;
            currentActionLinearLayout.setVisibility(View.GONE);
            winnerLinearLayout.setVisibility(View.VISIBLE);
            winnerTextView.setText(winner);
            winnerId = spId;
            putInDb();
        }else {
            nullVariables();
            setGameCountView();
            returnToServe();
            currentSet++;
        }
    }

    private void nullVariables(){
        fpInGameCountString = "00";
        spInGameCountString = "00";
        fpInGameCount = 0;
        spInGameCount = 0;
    }

    private void changeServePlayer(){
        if(currentServePlayer.equals(firstPlayerName)){
            currentServePlayer = secondPlayerName;
            forServe.setVisibility(View.VISIBLE);
            fpLayoutForServe.setVisibility(View.INVISIBLE);
            spLayoutForServe.setVisibility(View.VISIBLE);
        }else if(currentServePlayer.equals(secondPlayerName)){
            currentServePlayer = firstPlayerName;
            forServe.setVisibility(View.VISIBLE);
            fpLayoutForServe.setVisibility(View.VISIBLE);
            spLayoutForServe.setVisibility(View.INVISIBLE);
        }
    }

    private void tieBreak(){
        firstServeInTieBreak = currentServePlayer;
        nullVariables();
        isTieBreak = true;
    }

    private void returnToServe(){
        currentServe = 1;
        if(currentServePlayer.equals(firstPlayerName)){
            forServeIn.setVisibility(View.GONE);
            forServe.setVisibility(View.VISIBLE);
            fpLayoutForServe.setVisibility(View.VISIBLE);
            spLayoutForServe.setVisibility(View.INVISIBLE);
        }else if(currentServePlayer.equals(secondPlayerName)){
            forServeIn.setVisibility(View.GONE);
            forServe.setVisibility(View.VISIBLE);
            spLayoutForServe.setVisibility(View.VISIBLE);
            fpLayoutForServe.setVisibility(View.INVISIBLE);
        }
    }

    private void putInDb(){
        dbHelper = new DbHelper(this);
        putInStatistic();
        putInGames();
    }

    private void showTables(){
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        Cursor cursor1 = database.query(DbHelper.TABLE_GAMES,null,null,null,null,null,null);
        if(cursor1.moveToFirst()){
            int idIndex = cursor1.getColumnIndex(DbHelper.KEY_ID);
            int fpIdIndex = cursor1.getColumnIndex(DbHelper.KEY_FIRST_PLAYER_ID);
            int spIdIndex = cursor1.getColumnIndex(DbHelper.KEY_SECOND_PLAYER_ID);
            int dateIndex = cursor1.getColumnIndex(DbHelper.KEY_GAME_DATE);
            int gameNameIndex = cursor1.getColumnIndex(DbHelper.KEY_GAME_NAME);
            int fpStatIdIndex = cursor1.getColumnIndex(DbHelper.KEY_FIRST_PLAYER_STATISTIC_ID);
            int spStatIdIndex = cursor1.getColumnIndex(DbHelper.KEY_SECOND_PLAYER_STATISTIC_ID);
            int fpFsIndex = cursor1.getColumnIndex(DbHelper.KEY_FP_FIRST_SET);
            int spFsIndex = cursor1.getColumnIndex(DbHelper.KEY_SP_FIRST_SET);
            int fpSsIndex = cursor1.getColumnIndex(DbHelper.KEY_FP_SECOND_SET);
            int spSsIndex = cursor1.getColumnIndex(DbHelper.KEY_SP_SECOND_SET);
            int fpTsIndex = cursor1.getColumnIndex(DbHelper.KEY_FP_THIRD_SET);
            int spTsIndex = cursor1.getColumnIndex(DbHelper.KEY_SP_THIRD_SET);
            int winnerIndex = cursor1.getColumnIndex(DbHelper.KEY_WINNER_ID);
            Log.d("MyLog","Таблица игр");
            do{
                Log.d("MyLog", "id=" + cursor1.getInt(idIndex) + " first_player_id=" + cursor1.getInt(fpIdIndex) + " second_player_id=" + cursor1.getInt(spIdIndex) + " game_date=" + cursor1.getString(dateIndex) + " game_name=" + cursor1.getString(gameNameIndex) + " fp_stat_id=" + cursor1.getInt(fpStatIdIndex) + " sp_stat_id="+cursor1.getInt(spStatIdIndex) + " fpFs=" + cursor1.getInt(fpFsIndex) + " spFs=" + cursor1.getInt(spFsIndex) + " fpSs=" + cursor1.getInt(fpSsIndex) + " spSs=" + cursor1.getInt(spSsIndex) + " fpTs=" + cursor1.getInt(fpTsIndex) + " spTs=" + cursor1.getInt(spTsIndex) + " winnerId=" + cursor1.getInt(winnerIndex));
            } while (cursor1.moveToNext());
        }else {
            Log.d("MyLog","0 rows");
        }
        cursor1.close();

        Cursor cursor2 = database.query(DbHelper.TABLE_GAME_STATISTIC,null,null,null,null,null,null);
        if(cursor2.moveToFirst()){
            int idIndex = cursor2.getColumnIndex(DbHelper.KEY_ID);
            int playerIdIndex = cursor2.getColumnIndex(DbHelper.KEY_PLAYER_ID);
            int totalPointsWonIdIndex = cursor2.getColumnIndex(DbHelper.KEY_TOTAL_POINTS_WON);
            int fsInIndex = cursor2.getColumnIndex(DbHelper.KEY_FIRST_SERVE_IN);
            int fsPointWonIndex = cursor2.getColumnIndex(DbHelper.KEY_FIRST_SERVE_POINTS_WON);
            int ssInIndex = cursor2.getColumnIndex(DbHelper.KEY_SECOND_SERVE_IN);
            int ssPointWonIndex = cursor2.getColumnIndex(DbHelper.KEY_SECOND_SERVE_POINTS_WON);
            int acesIndex = cursor2.getColumnIndex(DbHelper.KEY_ACES);
            int dfIndex = cursor2.getColumnIndex(DbHelper.KEY_DOUBLE_FAULTS);
            int winnersIndex = cursor2.getColumnIndex(DbHelper.KEY_WINNERS);
            int ueIndex = cursor2.getColumnIndex(DbHelper.KEY_UNFORCED_ERRORS);

            Log.d("MyLog","Таблица ИГРОВАЯ СТАТИСТИКА");
            do{
                Log.d("MyLog", "id=" + cursor2.getInt(idIndex) + " player_id=" + cursor2.getInt(playerIdIndex) + " total_points_won=" + cursor2.getString(totalPointsWonIdIndex) + " first_serve_in=" + cursor2.getString(fsInIndex) + " first_serve_points_won=" + cursor2.getString(fsPointWonIndex) + " second_serve_in=" + cursor2.getString(ssInIndex) + " second_serve_points_won=" + cursor2.getString(ssPointWonIndex) + " aces=" + cursor2.getInt(acesIndex) + " double_faults=" + cursor2.getInt(dfIndex) + " winners=" + cursor2.getInt(winnersIndex) + " unforced_errors=" + cursor2.getInt(ueIndex));
            } while (cursor2.moveToNext());
        }else {
            Log.d("MyLog","0 rows");
        }
    }

    private void putInStatistic(){
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        ///////////////////////////////////////////////////////////////////////////////////////Первый игрок
        contentValues1.put(DbHelper.KEY_PLAYER_ID, fpId);
        String fpTotalPointsWonStr = fpTotalPointsWon + "/" + totalPoints + "(" + (fpTotalPointsWon * 100)/totalPoints + "%)";
        contentValues1.put(DbHelper.KEY_TOTAL_POINTS_WON,fpTotalPointsWonStr);

        String fpFirstServeInStr = "";
        if(fpFirstServeTotal != 0){
            fpFirstServeInStr = fpFirstServeCount + "/" + fpFirstServeTotal + "(" + (fpFirstServeCount * 100)/fpFirstServeTotal + "%)";
        } else {
            fpFirstServeInStr = fpFirstServeCount + "/" + fpFirstServeTotal;
        }
        contentValues1.put(DbHelper.KEY_FIRST_SERVE_IN,fpFirstServeInStr);

        String fpFirstServePointStr = "";
        if(fpFirstServeCount != 0){
            fpFirstServePointStr = fpFirstServePointsCount + "/" + fpFirstServeCount + "(" + (fpFirstServePointsCount * 100)/fpFirstServeCount + "%)";
        } else {
            fpFirstServePointStr = fpFirstServePointsCount + "/" + fpFirstServeCount;
        }
        contentValues1.put(DbHelper.KEY_FIRST_SERVE_POINTS_WON,fpFirstServePointStr);

        String fpSecondServeInStr = "";
        if(fpSecondServeTotal != 0){
            fpSecondServeInStr = fpSecondServeCount + "/" + fpSecondServeTotal + "(" + (fpSecondServeCount * 100)/fpSecondServeTotal + "%)";
        } else {
            fpSecondServeInStr = fpSecondServeCount + "/" + fpSecondServeTotal;
        }
        contentValues1.put(DbHelper.KEY_SECOND_SERVE_IN,fpSecondServeInStr);

        String fpSecondServePointStr = "";
        if(fpSecondServeCount != 0){
            fpSecondServePointStr = fpSecondServePointsCount + "/" + fpSecondServeCount + "(" + (fpSecondServePointsCount * 100)/fpSecondServeCount + "%)";
        } else {
            fpSecondServePointStr = fpSecondServePointsCount + "/" + fpSecondServeCount;
        }
        contentValues1.put(DbHelper.KEY_SECOND_SERVE_POINTS_WON,fpSecondServePointStr);

        contentValues1.put(DbHelper.KEY_ACES, fpAces);
        contentValues1.put(DbHelper.KEY_DOUBLE_FAULTS, fpDoubleFaults);
        contentValues1.put(DbHelper.KEY_WINNERS, fpWinners);
        contentValues1.put(DbHelper.KEY_UNFORCED_ERRORS, fpUnforcedError);
        database.insert(DbHelper.TABLE_GAME_STATISTIC,null,contentValues1);

        ///////////////////////////////////////////////////////////////////////////////////Второй игрок
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(DbHelper.KEY_PLAYER_ID, spId);

        String spTotalPointsWonStr = spTotalPointsWon + "/" + totalPoints + "(" + (spTotalPointsWon * 100)/totalPoints + "%)";
        contentValues2.put(DbHelper.KEY_TOTAL_POINTS_WON,spTotalPointsWonStr);

        String spFirstServeInStr = "";
        if(spFirstServeTotal != 0){
            spFirstServeInStr = spFirstServeCount + "/" + spFirstServeTotal + "(" + (spFirstServeCount * 100)/spFirstServeTotal + "%)";
        } else {
            spFirstServeInStr = spFirstServeCount + "/" + spFirstServeTotal;
        }
        contentValues2.put(DbHelper.KEY_FIRST_SERVE_IN,spFirstServeInStr);

        String spFirstServePointStr = "";
        if(spFirstServeCount != 0){
            spFirstServePointStr = spFirstServePointsCount + "/" + spFirstServeCount + "(" + (spFirstServePointsCount * 100)/spFirstServeCount + "%)";
        } else {
            spFirstServePointStr = spFirstServePointsCount + "/" + spFirstServeCount;
        }
        contentValues2.put(DbHelper.KEY_FIRST_SERVE_POINTS_WON,spFirstServePointStr);

        String spSecondServeInStr = "";
        if(spSecondServeTotal != 0){
            spSecondServeInStr = spSecondServeCount + "/" + spSecondServeTotal + "(" + (spSecondServeCount * 100)/spSecondServeTotal + "%)";
        } else {
            spSecondServeInStr = spSecondServeCount + "/" + spSecondServeTotal;
        }
        contentValues2.put(DbHelper.KEY_SECOND_SERVE_IN,spSecondServeInStr);

        String spSecondServePointStr = "";
        if(spSecondServeCount != 0){
            spSecondServePointStr = spSecondServePointsCount + "/" + spSecondServeCount + "(" + (spSecondServePointsCount * 100)/spSecondServeCount + "%)";
        } else {
            spSecondServePointStr = spSecondServePointsCount + "/" + spSecondServeCount;
        }
        contentValues2.put(DbHelper.KEY_SECOND_SERVE_POINTS_WON,spSecondServePointStr);

        contentValues2.put(DbHelper.KEY_ACES, spAces);
        contentValues2.put(DbHelper.KEY_DOUBLE_FAULTS, spDoubleFaults);
        contentValues2.put(DbHelper.KEY_WINNERS, spWinners);
        contentValues2.put(DbHelper.KEY_UNFORCED_ERRORS, spUnforcedError);
        database.insert(DbHelper.TABLE_GAME_STATISTIC,null,contentValues2);

        Cursor cursor1 = database.query(DbHelper.TABLE_GAME_STATISTIC, new String[]{"_id"},DbHelper.KEY_PLAYER_ID + " =? " + " AND " + DbHelper.KEY_DOUBLE_FAULTS + " =? " + " AND " + DbHelper.KEY_WINNERS + " =? " + " AND " + DbHelper.KEY_UNFORCED_ERRORS + " =? ", new String[]{Integer.toString(fpId),Integer.toString(fpDoubleFaults),Integer.toString(fpWinners),Integer.toString(fpUnforcedError)},null,null,null);
        if(cursor1.moveToFirst()){
            int fpIdIndex = cursor1.getColumnIndex(DbHelper.KEY_ID);
            fpStatId = cursor1.getInt(fpIdIndex);
        }else Log.d("MyLog", "NOT FOUND 1st");

        cursor1.close();

        Cursor cursor2 = database.query(DbHelper.TABLE_GAME_STATISTIC, new String[]{"_id"},DbHelper.KEY_PLAYER_ID + " =? " + " AND " + DbHelper.KEY_DOUBLE_FAULTS + " =? " + " AND " + DbHelper.KEY_WINNERS + " =? " + " AND " + DbHelper.KEY_UNFORCED_ERRORS + " =? ", new String[]{Integer.toString(spId),Integer.toString(spDoubleFaults),Integer.toString(spWinners),Integer.toString(spUnforcedError)},null,null,null);
        if(cursor2.moveToFirst()){
            int spIdIndex = cursor2.getColumnIndex(DbHelper.KEY_ID);
            spStatId = cursor2.getInt(spIdIndex);
        } else Log.d("MyLog", "NOT FOUND 2nd");
        cursor2.close();
    }

    private void putInGames(){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.KEY_FIRST_PLAYER_ID,fpId);
        contentValues.put(DbHelper.KEY_SECOND_PLAYER_ID,spId);
        contentValues.put(DbHelper.KEY_GAME_DATE,date);
        contentValues.put(DbHelper.KEY_GAME_NAME,gameName);
        contentValues.put(DbHelper.KEY_FIRST_PLAYER_STATISTIC_ID,fpStatId);
        contentValues.put(DbHelper.KEY_SECOND_PLAYER_STATISTIC_ID,spStatId);
        contentValues.put(DbHelper.KEY_FP_FIRST_SET,fpFs);
        contentValues.put(DbHelper.KEY_SP_FIRST_SET,spFs);
        contentValues.put(DbHelper.KEY_FP_SECOND_SET,fpSs);
        contentValues.put(DbHelper.KEY_SP_SECOND_SET,spSs);
        contentValues.put(DbHelper.KEY_FP_THIRD_SET,fpTs);
        contentValues.put(DbHelper.KEY_SP_THIRD_SET,spTs);
        contentValues.put(DbHelper.KEY_WINNER_ID,winnerId);
        database.insert(DbHelper.TABLE_GAMES,null,contentValues);
        dbHelper.close();
    }

    private void initListeners(){
        /////////////////////////////////////////////////////////////ВЫБОР ПОДАЮЩЕГО
        fpServeFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTables();
                forChooseServe.setVisibility(View.GONE);
                forServe.setVisibility(View.VISIBLE);
                spLayoutForServe.setVisibility(View.INVISIBLE);
                currentServePlayer = firstPlayerName;
                currentAction = "Первая подача";
                setCurrentActionView();
            }
        });

        spServeFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTables();
                forChooseServe.setVisibility(View.GONE);
                forServe.setVisibility(View.VISIBLE);
                fpLayoutForServe.setVisibility(View.INVISIBLE);
                currentServePlayer = secondPlayerName;
                currentAction = "Первая подача";
                setCurrentActionView();
            }
        });
        /////////////////////////////////////////////////////////////РЕЗУЛЬТАТ ПОДАЧИ

        fpAceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////////////////////////////Статистика
                fpAces++;
                fpTotalPointsWon++;
                totalPoints++;
                if(currentServe == 1){
                    fpFirstServeTotal++;
                    fpFirstServeCount++;
                    fpFirstServePointsCount++;
                } else if(currentServe == 2){
                    fpSecondServeTotal++;
                    fpSecondServeCount++;
                    fpSecondServePointsCount++;
                }
                /////////////////////////////////
                fpWinBall();
                returnToServe();
                currentAction = "Первая подача";
                setCurrentActionView();
            }

        });

        fpFaultCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentServe == 2){
                    ////////////////////////////////////СТАТИСТИКА
                    fpDoubleFaults++;
                    fpSecondServeTotal++;
                    totalPoints++;
                    spTotalPointsWon++;
                    //////////////////////////////////////////////
                    spWinBall();
                    returnToServe();
                    currentAction = "Первая подача";
                    setCurrentActionView();
                }else if(currentServe == 1){
                    ////////////////////////////////////СТАТИСТИКА
                    fpFirstServeTotal++;
                    //////////////////////////////////////////////
                    currentAction = "Вторая подача";
                    setCurrentActionView();
                    currentServe++;
                }
            }
        });

        fpServeInCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////////СТАТИСТИКА
                if(currentServe == 1){
                    fpFirstServeCount++;
                    fpFirstServeTotal++;
                } else if(currentServe == 2){
                    fpSecondServeCount++;
                    fpSecondServeTotal++;
                }

                //////////////////////////////////////////////
                forServe.setVisibility(View.GONE);
                forServeIn.setVisibility(View.VISIBLE);
                currentAction = "Мяч в игре";
                setCurrentActionView();
            }
        });
        /////////////////////////////////////////////////////////////МЯЧ В ИГРЕ
        fpWinnersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////////СТАТИСТИКА
                totalPoints++;
                fpWinners++;
                fpTotalPointsWon++;
                if(currentServe == 1 && currentServePlayer.equals(firstPlayerName)){
                    fpFirstServePointsCount++;
                } else if(currentServe == 2 && currentServePlayer.equals(firstPlayerName)){
                    fpSecondServePointsCount++;
                }
                //////////////////////////////////////////////
                fpWinBall();
                returnToServe();
                currentAction = "Первая подача";
                setCurrentActionView();
            }
        });

        fpUnforcedErrorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////////СТАТИСТИКА
                totalPoints++;
                fpUnforcedError++;
                spTotalPointsWon++;
                if(currentServe == 1 && currentServePlayer.equals(secondPlayerName)){
                    spFirstServePointsCount++;
                } else if(currentServe == 2 && currentServePlayer.equals(secondPlayerName)){
                    spSecondServePointsCount++;
                }

                //////////////////////////////////////////////
                spWinBall();
                returnToServe();
                currentAction = "Первая подача";
                setCurrentActionView();
            }
        });




        /////////////////////////////////////////////////////////////РЕЗУЛЬТАТ ПОДАЧИ
        spAceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////////////////////////////Статистика
                spAces++;
                spTotalPointsWon++;
                totalPoints++;
                if(currentServe == 1){
                    spFirstServeTotal++;
                    spFirstServeCount++;
                    spFirstServePointsCount++;
                } else if(currentServe == 2){
                    spSecondServeTotal++;
                    spSecondServeCount++;
                    spSecondServePointsCount++;
                }
                /////////////////////////////////
                spWinBall();
                returnToServe();
                currentAction = "Первая подача";
                setCurrentActionView();
            }
        });

        spFaultCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentServe == 2){
                    ////////////////////////////////////СТАТИСТИКА
                    spDoubleFaults++;
                    spSecondServeTotal++;
                    totalPoints++;
                    fpTotalPointsWon++;
                    //////////////////////////////////////////////
                    fpWinBall();
                    returnToServe();
                    currentAction = "Первая подача";
                    setCurrentActionView();
                }else if(currentServe == 1) {
                    ////////////////////////////////////СТАТИСТИКА
                    spFirstServeTotal++;

                    //////////////////////////////////////////////
                    currentAction = "Вторая подача";
                    setCurrentActionView();
                    currentServe++;
                }
            }
        });

        spServeInCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////////СТАТИСТИКА
                if(currentServe == 1){
                    spFirstServeCount++;
                    spFirstServeTotal++;
                } else if(currentServe == 2){
                    spSecondServeCount++;
                    spSecondServeTotal++;
                }

                //////////////////////////////////////////////
                forServe.setVisibility(View.GONE);
                forServeIn.setVisibility(View.VISIBLE);
                currentAction = "Мяч в игре";
                setCurrentActionView();
            }
        });

        /////////////////////////////////////////////////////////////МЯЧ В ИГРЕ

        spWinnersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////////СТАТИСТИКА
                totalPoints++;
                spWinners++;
                spTotalPointsWon++;
                if(currentServe == 1 && currentServePlayer.equals(secondPlayerName)){
                    spFirstServePointsCount++;
                } else if(currentServe == 2 && currentServePlayer.equals(secondPlayerName)){
                    spSecondServePointsCount++;
                }
                //////////////////////////////////////////////
                spWinBall();
                returnToServe();
                currentAction = "Первая подача";
                setCurrentActionView();
            }
        });

        spUnforcedErrorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////////СТАТИСТИКА
                totalPoints++;
                spUnforcedError++;
                fpTotalPointsWon++;
                if(currentServe == 1 && currentServePlayer.equals(firstPlayerName)){
                    fpFirstServePointsCount++;
                } else if(currentServe == 2 && currentServePlayer.equals(firstPlayerName)){
                    fpSecondServePointsCount++;
                }
                //////////////////////////////////////////////
                fpWinBall();
                returnToServe();
                currentAction = "Первая подача";
                setCurrentActionView();
            }
        });
    }
}
