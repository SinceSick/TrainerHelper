package com.example.trainerhelper.pojo;

public class GameElement {
    private String fpName,spName,date,gameName;
    private int fpFirstSet,fpSecondSet,fpThirdSet,spFirstSet,spSecondSet,spThirdSet,idInDb;

    public GameElement(int idInDb,String fpName,String spName,String date,String gameName, int fpFirstSet,int spFirstSet,int fpSecondSet,int spSecondSet,int fpThirdSet,int spThirdSet){
        this.idInDb = idInDb;
        this.fpName = fpName;
        this.spName = spName;
        this.date = date;
        this.gameName = gameName;
        this.fpFirstSet = fpFirstSet;
        this.spFirstSet = spFirstSet;
        this.fpSecondSet = fpSecondSet;
        this.spSecondSet = spSecondSet;
        this.fpThirdSet = fpThirdSet;
        this.spThirdSet = spThirdSet;
    }

    public String getFpName() {
        return fpName;
    }

    public String getSpName() {
        return spName;
    }

    public String getDate() {
        return date;
    }

    public String getGameName() {
        return gameName;
    }

    public int getFpFirstSet() {
        return fpFirstSet;
    }

    public int getFpSecondSet() {
        return fpSecondSet;
    }

    public int getFpThirdSet() {
        return fpThirdSet;
    }

    public int getSpFirstSet() {
        return spFirstSet;
    }

    public int getSpSecondSet() {
        return spSecondSet;
    }

    public int getSpThirdSet() {
        return spThirdSet;
    }

    public int getIdInDb() {
        return idInDb;
    }
}
