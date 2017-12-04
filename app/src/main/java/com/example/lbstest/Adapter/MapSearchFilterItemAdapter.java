package com.example.lbstest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.lbstest.Entity.MapSearchFilterItem;
import com.example.lbstest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类为适配器类用于对地图搜索框下的ListView进行初始化和设置。
 * Created by 天道 北云 on 2017/8/24.
 * @author BeiYun
 */

public class MapSearchFilterItemAdapter extends ArrayAdapter<MapSearchFilterItem> implements Filterable{
    private List<MapSearchFilterItem> mOriginalValues;
    private List<MapSearchFilterItem> objects;
    private Context context;
    private int resourceId;
    public MapSearchFilterItemAdapter(Context context,int textViewResourceId,List<MapSearchFilterItem> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        this.context=context;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MapSearchFilterItem item = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.name=(TextView) view.findViewById(R.id.id_main_Search_filter_item);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(item.getName());
        return view;
    }
    private class ViewHolder{
        TextView name;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                //objects.clear()为清空数组内的内容，对原内容无影响。
                objects.clear();
                //将object赋新值。
                objects.addAll((List<MapSearchFilterItem>) results.values);
//                objects = (List<MapSearchFilterItem>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<String> filteredArrList = new ArrayList<String>();
                //如果最初的mOriginalValues还未赋值则为其初始化。
                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<MapSearchFilterItem>(objects);
                }
                //如果限制条件为空则全部赋值
                if (constraint == null || constraint.length() == 0) {
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    //否则依据条件进行查找
                    final String constraintString = constraint.toString().toLowerCase();
                    final ArrayList<MapSearchFilterItem> values=new ArrayList<MapSearchFilterItem>(mOriginalValues);
                    final int count = values.size();
                    final ArrayList<MapSearchFilterItem> newValues = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        final String valueText = values.get(i).getName().toLowerCase();
                        if (valueText.contains(constraintString)) {
                            newValues.add(new MapSearchFilterItem(valueText));
                        } else {
                            final String[] words = valueText.split(" ");
                            for (String word : words) {
                                if (word.contains(constraintString)) {
                                    newValues.add(new MapSearchFilterItem(valueText));
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;
            }
        };
        return filter;
    }
}

