package com.example.trainerhelper.pojo;

public class PlayerElement {
    private String abr,name,surname,patronymic,birthday;
    private int idInDb;
    private String countString;
    private int hitsCount,currentHit,result,game;

    public PlayerElement(int idInDb,String name,String surname,String patronymic, String birthday){
        this.idInDb = idInDb;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.abr = initAbr();
        this.currentHit = 0;
        this.result = 0;
    }
    private String initAbr(){
        String abr = surname + " " + name.substring(0,1) + ". " + patronymic.substring(0,1) + ".";
        return abr;
    }

    public String getAbr() {
        return abr;
    }

    public int getIdInDb() {
        return idInDb;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setHitsCount(int hitsCount) {
        this.hitsCount = hitsCount;
        this.countString = currentHit + "/" + this.hitsCount;
    }

    public void setCurrentHit(int currentHit) {
        this.currentHit = currentHit;
        this.countString = currentHit + "/" + this.hitsCount;
    }

    public String getCountString() {
        return countString;
    }

    public int getCurrentHit() {
        return currentHit;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public int getGame() {
        return game;
    }
}
