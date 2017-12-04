package com.example.lbstest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lbstest.Entity.Poet;
import com.example.lbstest.MapActivity;
import com.example.lbstest.R;

import java.util.List;

/**
 * Created by 天道 北云 on 2017/9/3.
 * @author BeiYun
 */

public class SelectMainRecyclerViewPoetAdapter extends RecyclerView.Adapter<SelectMainRecyclerViewPoetAdapter.ViewHolder>{
    private Context mContext;
    private List<Poet> mPoetList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView poetImage;
        TextView poetName;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            poetImage = (ImageView) view.findViewById(R.id.id_select_main_image_view);
            poetName = (TextView) view.findViewById(R.id.id_select_main_textView);
        }
    }
    public SelectMainRecyclerViewPoetAdapter(List<Poet> fruitList) {
        mPoetList = fruitList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果主活动还未加载则加载 加载后不再多次加载。
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.poet_select_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Poet poet = mPoetList.get(position);
                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra(MapActivity.POET_NAME, poet.getName());
                intent.putExtra(MapActivity.POET_IMAGE_ID, poet.getImageId());
                mContext.startActivity(intent);
            }
        });
        return  holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Poet poet = mPoetList.get(position);
        holder.poetName.setText(poet.getName());
        Glide.with(mContext).load(poet.getImageId()).into(holder.poetImage);
    }
    @Override
    public int getItemCount() {
        return mPoetList.size();
    }
}
