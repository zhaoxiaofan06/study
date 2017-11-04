package com.shop.www.shopapplication.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shop.www.shopapplication.adapter.GoodsListAdapter;
import com.shop.www.shopapplication.MainActivity;
import com.shop.www.shopapplication.R;
import com.shop.www.shopapplication.common.RxBus;
import com.shop.www.shopapplication.model.GoodsModel;
import com.shop.www.shopapplication.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsListFragment extends Fragment implements View.OnClickListener{
    private RxBus rxBus;
    private View view;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private GoodsModel goodsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static int page=1;
    private int pagesize=10;
    private LinearLayoutManager layoutManager;
    private GoodsListAdapter adapter;
    private int total_pages;
    private Handler handler;
    private int delayTime;
    private ProgressBar progress;
    private TextView empty_view;
    private LinearLayout progress_wrap;
    private boolean loading=false;

    public GoodsListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_list,container,false);
        mainActivity=(MainActivity) getActivity();
        mainActivity.toolbar.setTitle("商品列表");
        mainActivity.toolbar.setTitleTextColor(Color.BLACK);
        mainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        mainActivity.menu.clear();

        handler=new Handler();
        delayTime=1500;
        //rxBus=mainActivity.getRxBusSingleton();
        rxBus=RxBus.getDefault();
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        empty_view=(TextView)view.findViewById(R.id.empty_view);
        progress=(ProgressBar)view.findViewById(R.id.progress);
        progress.setProgress(View.VISIBLE);
        progress_wrap=(LinearLayout)view.findViewById(R.id.progress_wrap);
        progress_wrap.setVisibility(View.VISIBLE);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorGreen));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mainActivity.toolbar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page--;
                        if(page>=1){
                            getListData(page);
                        }else{
                            page=1;
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, delayTime);
                //Toast.makeText(mainActivity,"page="+page,Toast.LENGTH_SHORT).show();
            }
        });

        layoutManager=new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState){
                //mainActivity.toolbar.setVisibility(View.GONE);
                super.onScrollStateChanged(recyclerView, newState);
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);
                if ( !loading && newState ==RecyclerView.SCROLL_STATE_IDLE && lastChildBottom == recyclerBottom && (lastPosition + 1 )==adapter.getItemCount()) {
                    loading=true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            if(page<=total_pages){
                                getListData(page);
                            }else{
                                page=total_pages;
                            }
                        }
                    }, delayTime);
                    //Toast.makeText(mainActivity,"滑动到底部了！",Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(mainActivity,"lastChildBottom ="+lastChildBottom+",recyclerBottom="+recyclerBottom,Toast.LENGTH_SHORT).show();
            }
        });
        adapter=new GoodsListAdapter();
        getListData(page);

        return view;
    }

    public void getListData(int page){
        String url=mainActivity.getResources().getString(R.string.api_url);
        RestClient client=RestClient.getDedault(url);
        Call<GoodsModel> call=client.getGoodsList(page,pagesize);

        call.enqueue(new Callback<GoodsModel>() {
            @Override
            public void onResponse(Call<GoodsModel> call, Response<GoodsModel> response) {
                try {
                    int code=response.code();
                    switch (code){
                        case 200:
                            goodsList=response.body();
                            total_pages=goodsList.getTotal();
                            if(goodsList.getGoodsList().size()>=1){
                                Gson gson=new Gson();
                                Log.d("return1",call.request().toString());
                                Log.d("return2",response.toString());
                                Log.d("return2",response.headers().toString());
                                Log.d("return2",gson.toJson(goodsList));

                                //mainActivity.toolbar.setVisibility(View.VISIBLE);
                                if(loading){
                                    loading=false;
                                }
                                adapter.setData(goodsList,GoodsListFragment.page);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }else{
                                empty_view.setVisibility(View.VISIBLE);
                                Toast.makeText(mainActivity,"暂无数据",Toast.LENGTH_SHORT).show();
                            }
                            //progress.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                }finally {
                    progress.setProgress(View.GONE);
                    progress_wrap.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GoodsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view){

    }
    @Override
    public void onPause() {
        super.onPause();
        progress.setProgress(View.GONE);
        progress_wrap.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
