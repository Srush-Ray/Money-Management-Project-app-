package com.example.sdl;

public class OweDeptDetails {
    public String person=null;
    public String amount=null;

    public OweDeptDetails(){
    }
    public OweDeptDetails(String person,String amount) {
        this.amount = amount;
        this.person=person;
    }
    public OweDeptDetails(String amount) {
        this.amount = amount;
    }
}
