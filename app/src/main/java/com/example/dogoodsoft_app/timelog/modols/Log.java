package com.example.dogoodsoft_app.timelog.modols;

import org.litepal.crud.DataSupport;

/**
 * Created by dogoodsoft-app on 2017/11/16.
 */

public class Log extends DataSupport{

    private int id;
    private String name;
    private Long counTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCounTime() {
        return counTime;
    }

    public void setCounTime(Long counTime) {
        this.counTime = counTime;
    }
}
