package com.company.Parsers;

/**
 * Created by admin on 06.05.17.
 */
public class NumberBotFromID {

    String id;
    Integer number;

    public NumberBotFromID(String id,Integer number){
        this.id = id;
        this.number = number;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
