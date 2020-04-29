package com.example.sdl;

import java.util.Date;

public class DataOD
{

    public Integer amount;
    public String type=null;
    public String id;
    public Date date;
    public String OD;

    DataOD()
    {}
    DataOD(String OD, Integer amount,String type,String id)
    {

        this.OD=OD;
        this.amount=amount;
        this.type=type;
        this.id=id;
    }
    DataOD(String OD, Integer amount)
    {

        this.OD=OD;
        this.amount=amount;
    }
    DataOD(String OD, Integer amount,Date date)
    {

        this.OD=OD;
        this.amount=amount;
        this.date=date;
    }

    public DataOD(String person, Integer amt, String od) {
        this.OD=OD;
        this.amount=amount;
        this.type=od;
    }
}
