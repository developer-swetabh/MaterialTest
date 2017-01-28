package com.swetupuru.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swetupuru.pojo.Information;
import com.swetupuru.materialtest.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by SWETABH on 12/15/2015.
 */
public class AdapterDrawer extends RecyclerView.Adapter<AdapterDrawer.ItemHolder>
{
    private  LayoutInflater inflater;
    List<Information> data= Collections.emptyList();
    Context context;
    public AdapterDrawer(Context context,List<Information> data)
    {
        inflater = LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }

    public void delete(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.custom_row,parent,false);
        ItemHolder holder= new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Information current=data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public ItemHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.listText);
            icon=(ImageView) itemView.findViewById(R.id.listIcon);

        }


    }

}
