package com.shop.www.shopapplication.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shop.www.shopapplication.MainActivity;
import com.shop.www.shopapplication.R;
import com.shop.www.shopapplication.model.GoodsDetail;
import com.shop.www.shopapplication.model.GoodsShow;
import com.shop.www.shopapplication.rest.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsDetailFragment extends Fragment {
    private View view;
    private MainActivity mainActivity;
    private Context context;
    private ImageView imageView;
    private TextView textView;
    private int proid;

    public GoodsDetailFragment() {
        // Required empty public constructor
    }

    public void setProid(int proid){
        this.proid=proid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_detail,container,false);
        mainActivity=(MainActivity) getActivity();
        mainActivity.toolbar.setTitle("商品详情");
        mainActivity.toolbar.setTitleTextColor(Color.BLACK);
        mainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        imageView=(ImageView) view.findViewById(R.id.goods_detail_image);
        textView=(TextView) view.findViewById(R.id.goods_detail_text);

        String url=mainActivity.getResources().getString(R.string.api_url);
        RestClient client=RestClient.getDedault(url);
        Call<GoodsShow> call=client.getGoodsDetail(proid);

        call.enqueue(new Callback<GoodsShow>() {
            @Override
            public void onResponse(Call<GoodsShow> call, Response<GoodsShow> response) {
                try {
                    int code=response.code();
                    switch (code){
                        case 200:
                            GoodsDetail goodsDetail=response.body().getGoodsDetail();
                            textView.setText(goodsDetail.getProname());
                            Glide.with(mainActivity).load(goodsDetail.getProimg()).into(imageView);
                            break;
                        default:
                            break;
                    }
                }finally {
                }
            }

            @Override
            public void onFailure(Call<GoodsShow> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return view;
    }
}
