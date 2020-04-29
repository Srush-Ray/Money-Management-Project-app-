package com.example.sdl;

public class Data {

    public String requirement;
    public Integer cost;
    public String id;

    Data(String requirement,Integer cost)
    {
        this.requirement=requirement;
        this.cost=cost;
    }

    public Data() {

    }

    public Data(String req, Integer cost, String id) {
        this.requirement=requirement;
        this.cost=cost;
        this.id=id;
    }
}
