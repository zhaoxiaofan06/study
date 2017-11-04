package com.shop.www.shopapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shop.www.shopapplication.adapter.GoodsListAdapter;
import com.shop.www.shopapplication.model.GoodsModel;
import com.shop.www.shopapplication.model.LoginReturn;
import com.shop.www.shopapplication.rest.DbClient;
import com.shop.www.shopapplication.rest.RestClient;
import com.shop.www.shopapplication.services.UserLogin;

import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView enterprise_name;
    private TextView username;
    private TextView userpass;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.init();
    }

    private void init(){
        enterprise_name=(TextView)findViewById(R.id.enterprise_name);
        username=(TextView)findViewById(R.id.username);
        userpass=(TextView)findViewById(R.id.userpass);
        login=(Button)findViewById(R.id.btn_login);

        enterprise_name.setOnClickListener(this);
        username.setOnClickListener(this);
        userpass.setOnClickListener(this);
        login.setOnClickListener(this);

        LoginReturn object=DbClient.getInstance(getApplicationContext()).lastLoginReturn(getApplicationContext());
        if(object!=null){
            //enterprise_name.setText("xc");
            //username.setText("00001");
            //userpass.setText("123456");
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onClick(View view){
        int id=view.getId();
        switch (id){
//            case R.id.enterprise_name:
//                Toast.makeText(LoginActivity.this,enterprise_name.getText(),Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.username:
//                Toast.makeText(LoginActivity.this,username.getText(),Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.userpass:
//                Toast.makeText(LoginActivity.this,userpass.getText(),Toast.LENGTH_SHORT).show();
//                break;
            case R.id.btn_login:
                loginCheck();
                break;
            default:
                break;
        }
    }

    private void loginCheck(){
        String url=getResources().getString(R.string.api_url);
        Log.d("test",url);
        RestClient client=RestClient.getDedault(url);
        Log.d("test",username.getText().toString());
        Log.d("test",userpass.getText().toString());
        Call<LoginReturn> call=client.getLoginReturn(username.getText().toString().trim(),userpass.getText().toString().trim());
        call.enqueue(new Callback<LoginReturn>() {
            @Override
            public void onResponse(Call<LoginReturn> call, Response<LoginReturn> response) {
                try {
                    int code=response.code();
                    switch (code){
                        case 500:
                            Toast.makeText(LoginActivity.this,"500",Toast.LENGTH_SHORT).show();
                            break;
                        case 401:
                            Toast.makeText(LoginActivity.this,"401",Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(LoginActivity.this,"400",Toast.LENGTH_SHORT).show();
                            break;
                        case 200:
                            Gson gson=new Gson();
                            LoginReturn retrun=response.body();
                            Log.d("test",gson.toJson(retrun).toString());

                            try{
                                DbClient.getInstance(getApplicationContext()).clearLoginReturn();
                                //retrun.setMsg("这是一个测试");
                                DbClient.getInstance(getApplicationContext()).dbHelper.loginReturnsDao().createOrUpdate(retrun);
                                //DbClient.getInstance(getApplicationContext()).dbHelper.loginReturnsDao().delete(retrun);
                                //LoginReturn object=DbClient.getInstance(getApplicationContext()).dbHelper.loginReturnsDao().queryForId(retrun.getId());
                                //LoginReturn obj=DbClient.getInstance(getApplicationContext()).lastLoginReturn(getApplicationContext());
                                //Log.d("login_object",gson.toJson(object));
                                //Log.d("login_obj",gson.toJson(obj));
                            }catch (SQLException e){
                                e.printStackTrace();
                            }

                            if(retrun.getStatus()==1){
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }else{
                                Toast.makeText(LoginActivity.this,retrun.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                }finally {

                }
            }

            @Override
            public void onFailure(Call<LoginReturn> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
