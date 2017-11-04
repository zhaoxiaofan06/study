package com.shop.www.shopapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shop.www.shopapplication.MainActivity;
import com.shop.www.shopapplication.R;
import com.shop.www.shopapplication.common.GoodsEvent;
import com.shop.www.shopapplication.common.RxBus;
import com.shop.www.shopapplication.model.GoodsDetail;
import com.shop.www.shopapplication.model.GoodsModel;
import java.util.List;

/**
 * Created by think on 2017/10/15.
 */

public class GoodsListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private View view;

    private List<GoodsDetail> goodsList;
    private static Context context;
    private int page;
    private int total_pages;
    private


    static class ItemViewHolder extends ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textView;
        private TextView priceView;
        private int proid;

        public ItemViewHolder(View view){
            super(view);
            imageView=(ImageView) view.findViewById(R.id.goods_image);
            textView=(TextView) view.findViewById(R.id.goods_text);
            priceView=(TextView) view.findViewById(R.id.goods_price);
            imageView.setOnClickListener(this);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            GoodsEvent e=new GoodsEvent(GoodsEvent.Type.GOTO_GOODS_DETAIL,proid,null);
            //MainActivity mainActivity=(MainActivity) GoodsListAdapter.context;
            RxBus rxBus=RxBus.getDefault();
            if(rxBus.hasObservers()){
                rxBus.send(e);
            }
        }
    }

    static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    public GoodsListAdapter(){
        super();
    }

    public void setData(GoodsModel goodsModel,int page){
        this.goodsList=goodsModel.getGoodsList();
        this.total_pages=goodsModel.getTotal();
        this.page=page;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        if (viewType == TYPE_ITEM) {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_goods_item,parent,false);
            context=parent.getContext();
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder holder=(ItemViewHolder)viewHolder;
            GoodsDetail goods = goodsList.get(position);
            holder.textView.setText(goods.getProname());
            holder.priceView.setText(goods.getMarketprice() + "￥");
            Glide.with(context).load(goods.getProimg()).override(210, 210).centerCrop().into(holder.imageView);
            holder.proid = goods.getProid();
        }
    }

    @Override
    public int getItemCount(){
        int count=0;
        if(goodsList.size()>0){
            if(page<total_pages){
                count=goodsList.size()+1;
            }else{
                count=goodsList.size();
            }
        }
        //Log.d("adapter","total_pages="+total_pages+",page="+page+",count="+count+"");
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if(page<total_pages){
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }else{
            return TYPE_ITEM;
        }
    }
}
