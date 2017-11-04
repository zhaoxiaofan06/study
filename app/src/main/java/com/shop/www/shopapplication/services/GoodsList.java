package com.shop.www.shopapplication.services;

import com.shop.www.shopapplication.model.GoodsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by think on 2017/10/15.
 */

public interface GoodsList {
    @GET("api.php/goods/list?ishit=2")
    Call<GoodsModel> getList(@Query("p") int p,@Query("pagesize") int pagesize);
}
