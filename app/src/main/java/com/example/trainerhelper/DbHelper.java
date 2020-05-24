package com.example.trainerhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "trainDB";

    public static final String TABLE_PLAYERS = "players";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME= "surname";
    public static final String KEY_PATRONYMIC= "patronymic";
    public static final String KEY_BIRTHDAY= "birthday";
    public static final String KEY_GROUP= "group_id";
    public static final String KEY_GENDER= "gender";


    public static final String TABLE_GROUPS = "groups";
    public static final String KEY_GROUP_NAME = "name";
    public static final String KEY_MON= "mon";
    public static final String KEY_TUE= "tue";
    public static final String KEY_WED= "wed";
    public static final String KEY_THU= "thu";
    public static final String KEY_FRI= "fri";
    public static final String KEY_SAT= "sat";
    public static final String KEY_SUN= "sun";
    public static List<String> groupTable;


    public static final String TABLE_TRAIN_RESULTS = "train_results";
    public static final String KEY_PLAYER_ID = "player_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_FOREHAND_ON_GROUND = "forehand_on_ground";
    public static final String KEY_FOREHAND_VOLLEY = "forehand_volley";
    public static final String KEY_BACKHAND_ON_GROUND = "backhand_on_ground";
    public static final String KEY_BACKHAND_VOLLEY = "backhand_volley";
    public static final String KEY_SMASH = "smash";
    public static final String KEY_SERVE_IN = "serve_in";
    public static final String KEY_SERVE = "serve";
    public static final String KEY_RUN_30M = "run_30m";
    public static final String KEY_CHELNOK = "chelnok";
    public static final String KEY_BALL_THROW = "ball_throw";
    public static final String KEY_JUMP = "jump";
    public static final String KEY_FLEXIBILITY= "flexibility";

    public static final String TABLE_GAMES = "games";
    public static final String KEY_FIRST_PLAYER_ID = "first_player_id";
    public static final String KEY_SECOND_PLAYER_ID = "second_player_id";
    public static final String KEY_GAME_DATE = "date";
    public static final String KEY_GAME_NAME = "game_name";
    public static final String KEY_FIRST_PLAYER_STATISTIC_ID = "first_player_statistic_id";
    public static final String KEY_SECOND_PLAYER_STATISTIC_ID = "second_player_statistic_id";
    public static final String KEY_FP_FIRST_SET = "fp_first_set";
    public static final String KEY_SP_FIRST_SET = "sp_first_set";
    public static final String KEY_FP_SECOND_SET = "fp_second_set";
    public static final String KEY_SP_SECOND_SET = "sp_second_set";
    public static final String KEY_FP_THIRD_SET = "fp_third_set";
    public static final String KEY_SP_THIRD_SET = "sp_third_set";
    public static final String KEY_WINNER_ID = "winner_id";

    public static final String TABLE_GAME_STATISTIC = "game_statistic";
    public static final String KEY_TOTAL_POINTS_WON = "total_points_won";
    public static final String KEY_FIRST_SERVE_IN = "first_serve_in";
    public static final String KEY_FIRST_SERVE_POINTS_WON = "first_serve_points_won";
    public static final String KEY_SECOND_SERVE_IN = "second_serve_in";
    public static final String KEY_SECOND_SERVE_POINTS_WON = "second_serve_points_won";
    public static final String KEY_ACES = "aces";
    public static final String KEY_DOUBLE_FAULTS = "double_faults";
    public static final String KEY_WINNERS = "winners";
    public static final String KEY_UNFORCED_ERRORS = "unforced_errors";



    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PLAYERS + "("+KEY_ID + " integer primary key, " + KEY_NAME + " text, " + KEY_SURNAME + " text, " + KEY_PATRONYMIC + " text, " + KEY_BIRTHDAY + " text, " + " integer," + KEY_GROUP + " integer," + KEY_GENDER + " text)");

        db.execSQL("create table " + TABLE_GROUPS + "(" + KEY_ID + " integer primary key, "+ KEY_GROUP_NAME + " text, " + KEY_MON + " text, " + KEY_TUE + " text, " + KEY_WED + " text," + KEY_THU + " text," + KEY_FRI + " text, " + KEY_SAT + " text, " + KEY_SUN + " text)" );

        db.execSQL("create table " + TABLE_TRAIN_RESULTS + "(" + KEY_ID + " integer primary key, " + KEY_PLAYER_ID + " integer, " + KEY_DATE + " text, " + KEY_GAME_NAME + " text, " + KEY_FOREHAND_ON_GROUND + " text, " + KEY_FOREHAND_VOLLEY + " text, " + KEY_BACKHAND_ON_GROUND + " text, " + KEY_BACKHAND_VOLLEY + " text, " + KEY_SMASH + " text, " + KEY_SERVE_IN + " text, " + KEY_SERVE + " text, " + KEY_RUN_30M + " integer, " + KEY_CHELNOK + " integer, " + KEY_BALL_THROW + " integer, " + KEY_JUMP + " integer, " + KEY_FLEXIBILITY + " integer)" );

        db.execSQL("create table " + TABLE_GAMES + "(" + KEY_ID + " integer primary key, " + KEY_FIRST_PLAYER_ID + " integer, " + KEY_SECOND_PLAYER_ID + " integer, " + KEY_GAME_DATE + " text, " + KEY_GAME_NAME + " text, " + KEY_FIRST_PLAYER_STATISTIC_ID + " integer, " + KEY_SECOND_PLAYER_STATISTIC_ID + " integer, " + KEY_FP_FIRST_SET + " integer, " + KEY_SP_FIRST_SET + " integer, " + KEY_FP_SECOND_SET + " integer, " + KEY_SP_SECOND_SET + " integer, " + KEY_FP_THIRD_SET + " integer, " + KEY_SP_THIRD_SET + " integer," + KEY_WINNER_ID + " integer)"  );

        db.execSQL("create table " + TABLE_GAME_STATISTIC + "(" + KEY_ID + " integer primary key, " + KEY_PLAYER_ID + " integer, " + KEY_TOTAL_POINTS_WON + " text, " + KEY_FIRST_SERVE_IN + " text, " + KEY_FIRST_SERVE_POINTS_WON + " text, " + KEY_SECOND_SERVE_IN + " text, " + KEY_SECOND_SERVE_POINTS_WON + " text, " + KEY_ACES + " integer, " + KEY_DOUBLE_FAULTS + " integer, " + KEY_WINNERS + " integer, " + KEY_UNFORCED_ERRORS + " integer)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PLAYERS);
        db.execSQL("drop table if exists " + TABLE_GROUPS);
        db.execSQL("drop table if exists " + TABLE_TRAIN_RESULTS);
        db.execSQL("drop table if exists " + TABLE_GAMES);
        db.execSQL("drop table if exists " + TABLE_GAME_STATISTIC);
        onCreate(db);
    }

    public static void initList(){
        groupTable = new ArrayList<>();
        groupTable.add(DbHelper.KEY_MON);
        groupTable.add(DbHelper.KEY_TUE);
        groupTable.add(DbHelper.KEY_WED);
        groupTable.add(DbHelper.KEY_THU);
        groupTable.add(DbHelper.KEY_FRI);
        groupTable.add(DbHelper.KEY_SAT);
        groupTable.add(DbHelper.KEY_SUN);
    }

    public static String parseDate(int year, int month, int day){
        String str = String.valueOf(day);
        if( month / 10 > 0){
            str += "-" + month;
        }
        else {
            str += "-0" + month;
        }

        if( year / 10 > 0){
            str += "-" + year;
        }
        else {
            str += "-0" + year;
        }
        return str;
    }

    public static String parseTime(int hour, int minute){
        String str = "";
        if( hour / 10 > 0){
            str = String.valueOf(hour);
        }
        else {
            str += "0" + hour;
        }

        if( minute / 10 > 0){
            str += ":" + minute;
        }
        else {
            str += ":0" + minute;
        }
        return str;
    }
}
