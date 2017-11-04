package com.shop.www.shopapplication.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by think on 2017/10/21.
 */

public class LoginReturn {
    @DatabaseField(id=true)
    private int id;
    @DatabaseField(canBeNull = true)
    private int status;
    @DatabaseField(canBeNull = true)
    private String msg;
    public int getId(){return id;}
    public int getStatus(){
        return status;
    }
    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg=msg;
    }
}
