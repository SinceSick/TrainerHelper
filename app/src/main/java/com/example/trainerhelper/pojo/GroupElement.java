package com.example.trainerhelper.pojo;


import java.util.List;

public class GroupElement {
    private int idInDb;
    private String name;
    private String time;
    private String days;
    private List<String> dayList;

    public GroupElement(int idInDb,String name,String time,List<String> dayList){
        this.idInDb = idInDb;
        this.name = name;
        this.time = time;
        this.dayList = dayList;
        this.days = initDays();
    }
    private String initDays(){
        days = "";
        for(int i = 0; i < dayList.size();i++){
            if(i != 0){
                days = days.concat("-");
            }
            switch (dayList.get(i)){
                case "mon":
                    days = days.concat("ПН");
                    break;
                case "tue":
                    days = days.concat("ВТ");
                    break;
                case "wed":
                    days = days.concat("СР");
                    break;
                case "thu":
                    days = days.concat("ЧТ");
                    break;
                case "fri":
                    days = days.concat("ПТ");
                    break;
                case "sat":
                    days = days.concat("СБ");
                    break;
                case "sun":
                    days = days.concat("ВС");
                    break;
            }
        }
        return days;
    }
    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDays() {
        return days;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdInDb() {
        return idInDb;
    }
}
