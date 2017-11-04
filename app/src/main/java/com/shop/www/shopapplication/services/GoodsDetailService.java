package com.shop.www.shopapplication.services;

import com.shop.www.shopapplication.model.GoodsShow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by think on 2017/10/15.
 */

public interface GoodsDetailService {
    @GET("api.php/goods/detail")
    Call<GoodsShow> getDetail(@Query("proid") int proid);
}
