package com.shop.www.shopapplication.services;

import com.shop.www.shopapplication.model.LoginReturn;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by think on 2017/10/21.
 */

public interface UserLogin {
    @POST("api.php/login/check")
    @FormUrlEncoded
    Call<LoginReturn> getReturnData(@Field("username") String username, @Field("password") String password);
}
