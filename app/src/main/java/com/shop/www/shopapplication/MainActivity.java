package com.shop.www.shopapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shop.www.shopapplication.common.GoodsEvent;
import com.shop.www.shopapplication.common.RxBus;
import com.shop.www.shopapplication.fragment.GoodsDetailFragment;
import com.shop.www.shopapplication.fragment.GoodsListFragment;
import com.shop.www.shopapplication.fragment.HomeFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public RxBus rxBus;
    public Toolbar toolbar;
    public CompositeSubscription subscription;
    public String TAG="test";
    public Bundle bundle;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle=savedInstanceState;
        rxBus=RxBus.getDefault();
        init();
    }

    protected void init(){
        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        menu=toolbar.getMenu();
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_call:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_location:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_mail:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_task:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        toolbar.setTitle("主页");
        replaceFragmnet(new HomeFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        subscription=new CompositeSubscription();
        subscription.add(rxBus.toObserverable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event){
                        if (event instanceof GoodsEvent) {
                            GoodsEvent e=(GoodsEvent)event;
                            if(e.getType()==GoodsEvent.Type.GOTO_GOODS_LIST){

                                replaceFragmnet(new GoodsListFragment());

                            } else if(e.getType()==GoodsEvent.Type.GOTO_GOODS_DETAIL){
                                int proid=e.getResultCode();
                                //Toast.makeText(MainActivity.this,"proid="+proid,Toast.LENGTH_SHORT).show();
                                GoodsDetailFragment detailFragment=new GoodsDetailFragment();
                                detailFragment.setProid(proid);
                                replaceFragmnet(detailFragment);
                            }else{

                                replaceFragmnet(new HomeFragment());

                            }
                        }
                    }
                }));
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscription.unsubscribe();
        subscription.clear();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }


    @Override
    public void onClick(View view){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
//        switch (id){
//            case R.id.backup:
//                Toast.makeText(this,"开始备份",Toast.LENGTH_SHORT).show();
//                break;
//        }
        return true;
    }

    private void replaceFragmnet(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_id,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public RxBus getRxBusSingleton() {
        if (rxBus == null) {
            rxBus = RxBus.getDefault();
        }

        return rxBus;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(MainActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onBackPressed(){
        int count=getSupportFragmentManager().getBackStackEntryCount();
        //Toast.makeText(MainActivity.this,count+"",Toast.LENGTH_SHORT).show();
        if(count > 0){
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_id);
            if(fragment instanceof HomeFragment){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                int imageResource = android.R.drawable.ic_dialog_alert;
                Drawable image = getResources().getDrawable(imageResource);

                builder.setTitle("退出")
                        .setMessage("退出程序吗?")
                        .setIcon(image)
                        .setCancelable(false)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                Intent startMain = new Intent(Intent.ACTION_MAIN);
                                startMain.addCategory(Intent.CATEGORY_HOME);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(startMain);
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }else{
                super.onBackPressed();
            }
        }else{
            getSupportFragmentManager().popBackStack();
        }
    }
}
