package com.aggregator.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BL.AddFav;
import com.aggregator.BL.TripHistoryBL;
import com.aggregator.Constant.Constant;
import com.aggregator.loop.R;
import com.aggregator.loop.TicketScreen;
import com.aggregator.loop.TripFeedback;

import java.text.SimpleDateFormat;

/**
 * Created by Pranav Mittal on 9/14/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */

public class TripHistoryAdapter extends BaseAdapter implements View.OnClickListener
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Context mContext;
        TripHistoryBL objTripHistoryBL=new TripHistoryBL();
        String status;
        ImageView ivLeftTop,ivLeftBottom;
        TextView tvLeftTop,tvLeftBottom,tvTime,tvStartPoint,tvEndPoint,tvCost;

        LinearLayout llLeftTop,llLeftBottom,llRight;

        String userID;

        AddFav objAddFav;

        public TripHistoryAdapter(Context context,String id){

            mContext=context;
            userID=id;
            status=objTripHistoryBL.getAllTrip(id);
            objAddFav=new AddFav();


        }

        @Override
        public int getCount() {
            return Constant.tripRouteID.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            View gridView=null;
            //TextView tv,tv1;
            if (convertView != null){

                gridView=convertView;

            }else{
                gridView = new View(mContext);
                gridView= infalInflater.inflate(R.layout.trips_raw_list, null);

                ivLeftTop= (ImageView) gridView.findViewById(R.id.trip_left_top_img);
                ivLeftBottom= (ImageView) gridView.findViewById(R.id.trip_left_bottom_img);
                tvStartPoint= (TextView) gridView.findViewById(R.id.trip_start_point);
                tvEndPoint= (TextView) gridView.findViewById(R.id.trip_end_point);
                tvCost= (TextView) gridView.findViewById(R.id.trip_cost);
                tvTime= (TextView) gridView.findViewById(R.id.trip_tv_date_time);
                tvLeftTop= (TextView) gridView.findViewById(R.id.trip_left_top_text);
                tvLeftBottom= (TextView) gridView.findViewById(R.id.trip_left_bottom_text);
                llLeftTop= (LinearLayout) gridView.findViewById(R.id.trip_ll_left_top);
                llLeftBottom= (LinearLayout) gridView.findViewById(R.id.trip_ll_left_bottom);
            }

            if(Constant.tripStatus[position].equals("ACTIVE")){
                ivLeftTop.setBackgroundResource(R.drawable.ic_ticket);
                tvLeftTop.setText("Ticket");
            }
            else
            {
                ivLeftTop.setBackgroundResource(R.drawable.ic_red_star);
                tvLeftTop.setText("Rate Trip");
            }

            if(Constant.tripFav[position].equals("No")){
                ivLeftBottom.setBackgroundResource(R.drawable.ic_white_heart);
                tvLeftBottom.setText("Add to favourites.");
            }
            else
            {
                ivLeftBottom.setBackgroundResource(R.drawable.ic_red_heart);
                tvLeftBottom.setText("Favourite route.");
            }

            tvStartPoint.setText(Constant.tripStartName[position]);
            tvEndPoint.setText(Constant.tripEndName[position]);

            tvCost.setText("Cost : Rs. "+Constant.tripPrice[position]);
            String dd[]=Constant.tripDate[position].split(" ");
            try {
                String onlydate=sdf.parse(dd[0])+"";
                onlydate=onlydate.substring(0,10);
                tvTime.setText(onlydate + " at " + Constant.tripTime[position]);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            llLeftBottom.setOnClickListener(this);
            llLeftTop.setOnClickListener(this);

            llLeftBottom.setTag(position);
            llLeftTop.setTag(position);

            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            return gridView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.trip_ll_left_top:
                    int pos=(int)v.getTag();
                    if(Constant.tripStatus[pos].equals("ACTIVE")){
                        Intent intent=new Intent(mContext, TicketScreen.class);
                        intent.putExtra("BookingID", Constant.tripRunId[pos]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    else{
                        Intent intent=new Intent(mContext, TripFeedback.class);
                        intent.putExtra("RunID",Constant.tripRunId[pos]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.trip_ll_left_bottom:
                    int position=(int)v.getTag();
                    try {
                        String result = new AddToFav().execute(userID, Constant.tripRouteID[position], Constant.tripStartID[position], Constant.tripEndID[position]).get();
                        if(result.equals(Constant.WS_RESULT_SUCCESS)){
                            Toast.makeText(mContext,"Route Added to Favourite list",Toast.LENGTH_LONG).show();
                            updateAdapter(position, "Yes");
                            //notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(mContext,"Route Removed from Favourite list",Toast.LENGTH_LONG).show();

                            updateAdapter(position, "No");
                            //notifyDataSetChanged();
                        }


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

            }
        }

        private class AddToFav extends AsyncTask<String,String,String>{

            int pos;

            @Override
            protected String doInBackground(String... params) {
                String result=objAddFav.addfav(params[0], params[1], params[2], params[3]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public void updateAdapter(int pos,String ss) {
            Constant.tripFav[pos]=ss;

            //and call notifyDataSetChanged

            notifyDataSetChanged();
        }
    }
