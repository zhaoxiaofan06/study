package com.shop.www.shopapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by think on 2017/10/15.
 */

public class GoodsModel {
    @SerializedName("total")
    private int total;

    @SerializedName("data")
    private ArrayList<GoodsDetail> goodsList;

    public ArrayList<GoodsDetail> getGoodsList(){
        return goodsList;
    }

    public int getTotal() {return total;}
}
