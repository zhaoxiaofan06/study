package com.shop.www.shopapplication.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by think on 2017/10/15.
 */

public class GoodsDetail {

    @SerializedName("proname")
    private String proname;
    @SerializedName("proimg")
    private String proimg;
    @SerializedName("proid")
    private int proid;
    @SerializedName("marketprice")
    private BigDecimal marketprice;

    public String getProname(){
        return proname;
    }

    public String getProimg(){
        return proimg;
    }

    public int getProid(){
        return proid;
    }

    public BigDecimal getMarketprice() { return marketprice;}
}
