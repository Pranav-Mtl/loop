package com.aggregator.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aggregator.loop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pranav Mittal on 11/1/2015.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.DealerCategoryHolder> {

    Context mContext;

    List listCategory=new ArrayList<>();
    HashMap<String,Boolean> hashMap=new HashMap<String,Boolean>();

    List listCategoryBoolean=new ArrayList<Boolean>();

    public NotificationAdapter(Context context){
        mContext=context;
        listCategory.add("Message");



    }

    @Override
    public DealerCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.dealer_category_raw, parent, false);

        return new DealerCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealerCategoryHolder holder, int position) {
        holder.tvMessage.setText(listCategory.get(position).toString());
        holder.tvDate.setText("Date");

        holder.cbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v ;

                if(cb.isChecked()){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public static class DealerCategoryHolder extends RecyclerView.ViewHolder{

        TextView tvMessage,tvDate;
        CheckBox cbCategory;

        public DealerCategoryHolder(View itemView) {
            super(itemView);

            tvMessage= (TextView) itemView.findViewById(R.id.notification_msg);
            cbCategory= (CheckBox) itemView.findViewById(R.id.notification_cb);
            tvDate= (TextView) itemView.findViewById(R.id.notification_date);

        }
    }
}
