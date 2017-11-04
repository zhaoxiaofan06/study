package com.shop.www.shopapplication.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.www.shopapplication.MainActivity;
import com.shop.www.shopapplication.R;
import com.shop.www.shopapplication.common.GoodsEvent;
import com.shop.www.shopapplication.common.RxBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private RxBus rxBus;
    private View view;
    private MainActivity mainActivity;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_home,container,false);
        mainActivity=(MainActivity) getActivity();
        mainActivity.toolbar.setTitle("主页");
        mainActivity.toolbar.setTitleTextColor(Color.BLACK);
        mainActivity.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mainActivity.getMenuInflater().inflate(R.menu.toolbar,mainActivity.menu);
        mainActivity.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                switch (id){
                    case R.id.backup:
                        Toast.makeText(mainActivity,"已备份",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delete:
                        Toast.makeText(mainActivity,"已删除",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        //rxBus=mainActivity.getRxBusSingleton();
        rxBus=RxBus.getDefault();
        Button button=(Button)view.findViewById(R.id.list_item_id);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view){
        int id=view.getId();
        switch (id){
            case R.id.list_item_id:
                GoodsEvent e=new GoodsEvent(GoodsEvent.Type.GOTO_GOODS_LIST,0,null);
                if(rxBus.hasObservers()){
                    rxBus.send(e);
                }
                break;
        }
    }

}
