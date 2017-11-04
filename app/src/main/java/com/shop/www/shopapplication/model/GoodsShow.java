package com.shop.www.shopapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by think on 2017/10/15.
 */

public class GoodsShow {

    @SerializedName("data")
    public GoodsDetail goodsDetail;

    public GoodsDetail getGoodsDetail(){
        return goodsDetail;
    }
}
