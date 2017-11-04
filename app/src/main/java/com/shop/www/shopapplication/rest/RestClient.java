package com.shop.www.shopapplication.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shop.www.shopapplication.model.GoodsDetail;
import com.shop.www.shopapplication.model.GoodsModel;
import com.shop.www.shopapplication.model.GoodsShow;
import com.shop.www.shopapplication.model.LoginReturn;
import com.shop.www.shopapplication.services.GoodsDetailService;
import com.shop.www.shopapplication.services.GoodsList;
import com.shop.www.shopapplication.services.UserLogin;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by think on 2017/10/14.
 */

public class RestClient {
    public static String api_url;
    public static RestClient restClient=null;

    public RestClient(String api_url){
        this.api_url=api_url;
    }

    public static RestClient getDedault(String api_url){
        if(restClient==null){
            synchronized (RestClient.class){
                restClient=new RestClient(api_url);
            }
        }
        return restClient;
    }

    public Retrofit getRetrofit(){
        Gson gson = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();

        Retrofit retrofit=new Retrofit
                .Builder()
                .baseUrl(api_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    public Call<GoodsModel> getGoodsList(int p,int pagesize){
        Retrofit retrofit=getRetrofit();
        GoodsList goodsListService=retrofit.create(GoodsList.class);
        return goodsListService.getList(p,pagesize);
    }

    public Call<GoodsShow> getGoodsDetail(int proid){
        Retrofit retrofit=getRetrofit();
        GoodsDetailService goodsDetailService=retrofit.create(GoodsDetailService.class);
        return goodsDetailService.getDetail(proid);
    }

    public Call<LoginReturn> getLoginReturn(String username,String password){
        Retrofit retrofit=getRetrofit();
        UserLogin userLogin=retrofit.create(UserLogin.class);
        return userLogin.getReturnData(username,password);
    }
}
