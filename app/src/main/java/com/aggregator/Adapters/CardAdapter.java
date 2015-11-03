package com.aggregator.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BL.AddFav;
import com.aggregator.BL.TripHistoryBL;
import com.aggregator.Constant.Constant;
import com.aggregator.loop.R;
import com.aggregator.loop.TicketScreen;
import com.aggregator.loop.TripFeedback;
import com.aggregator.loop.TripHistory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pranav Mittal on 9/18/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */



public class CardAdapter extends  RecyclerView.Adapter<CardAdapter.ContactViewHolder> {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Context mContext;
    TripHistoryBL objTripHistoryBL=new TripHistoryBL();
    String status;
    View _itemColoured;
    String userID;
    AddFav objAddFav;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public CardAdapter(Context context,String id,String page) {
        mContext=context;
        userID=id;
        try {
            status = objTripHistoryBL.getAllTrip(id,page);
        }
        catch (Exception e){
            e.printStackTrace();

        }
        objAddFav=new AddFav();

    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.card_view, viewGroup, false);

        return new ContactViewHolder(itemView,mContext);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {

        Log.d("POSITION",i+"");
        if("ACTIVE".equalsIgnoreCase(Constant.tripStatus[i])){
            contactViewHolder.ivLeftTop.setBackgroundResource(R.drawable.ic_ticket);
            contactViewHolder.tvLeftTop.setText("Ticket");
            try {
                Date dtArray = dateFormat.parse(Constant.tripTime[i]);
                contactViewHolder.tvTime.setText(new SimpleDateFormat("K:mm a").format(dtArray));
            }
            catch (Exception    e){

            }

        }
        else
        {
            if("1".equalsIgnoreCase(Constant.tripFeedbackStatus[i])) {
                contactViewHolder.ivLeftTop.setBackgroundResource(R.drawable.ic_red_star);
                contactViewHolder.tvLeftTop.setText("");
            }
            else {
                contactViewHolder.ivLeftTop.setBackgroundResource(R.drawable.ic_trip_white_star);
                contactViewHolder.tvLeftTop.setText("Rate trip");
            }
            try {

            String dd[]=Constant.tripDate[i].split(" ");

                String onlydate=sdf.parse(dd[0])+"";
                onlydate=onlydate.substring(0,10);

                Date dtArray = dateFormat.parse(Constant.tripTime[i]);
                contactViewHolder.tvTime.setText(onlydate + " at " + new SimpleDateFormat("K:mm a").format(dtArray));
               // contactViewHolder.tvTime.setText(onlydate + " at " + Constant.tripTime[i]);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        if("No".equalsIgnoreCase(Constant.tripFav[i])){
            contactViewHolder.ivLeftBottom.setBackgroundResource(R.drawable.ic_white_heart);
            contactViewHolder.tvLeftBottom.setText("Add to favourites");
        }
        else
        {
            contactViewHolder.ivLeftBottom.setBackgroundResource(R.drawable.ic_red_heart);
            contactViewHolder.tvLeftBottom.setText("");
        }

        contactViewHolder.tvStartPoint.setText(Constant.tripStartName[i]);
        contactViewHolder.tvEndPoint.setText(Constant.tripEndName[i]);

       /* contactViewHolder.tvCost.setText("Cost : ₹ "+Constant.tripTotalAmount[i]+" (Loop Credit: ₹ "+Constant.tripLoopCredit[i]+", Paytm: ₹ "+Constant.tripPrice[i]+" )");*/
        contactViewHolder.tvCost.setText("₹ "+Math.round(Double.valueOf(Constant.tripLoopCredit[i])) );

        contactViewHolder.llLeftBottom.setOnClickListener(clickListener);
        contactViewHolder.llLeftTop.setOnClickListener(clickListener);

        contactViewHolder.llLeftBottom.setTag(contactViewHolder);
        contactViewHolder.llLeftTop.setTag(contactViewHolder);

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ContactViewHolder holder = (ContactViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            switch (view.getId()){

                case R.id.trip_ll_left_top:

                    if(Constant.tripStatus[position].equalsIgnoreCase("ACTIVE")){
                        Intent intent=new Intent(mContext, TicketScreen.class);
                        intent.putExtra("BookingID", Constant.tripRunId[position]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    else{
                        Intent intent=new Intent(mContext, TripFeedback.class);
                        intent.putExtra("RunID",Constant.tripRunId[position]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.trip_ll_left_bottom:

                    try {
                        String result = new AddToFav().execute(userID, Constant.tripRouteID[position], Constant.tripStartID[position], Constant.tripEndID[position]).get();
                        if(result.equals(Constant.WS_RESULT_SUCCESS)){
                            Toast.makeText(mContext,"Route Added to Favourite list",Toast.LENGTH_LONG).show();
                            mContext.startActivity(new Intent(mContext, TripHistory.class));

                            //updateAdapter(position, "Yes");
                            //notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(mContext,"Route Removed from Favourite list",Toast.LENGTH_LONG).show();
                            mContext.startActivity(new Intent(mContext, TripHistory.class));

                            //updateAdapter(position, "No");
                            //notifyDataSetChanged();
                        }


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };

    @Override
    public int getItemCount() {
        int count=0;
        try {
            count=Constant.tripRouteID.length;
        }
        catch (NullPointerException e){
            count=0;
        }

        return count;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivLeftTop,ivLeftBottom;
        TextView tvLeftTop,tvLeftBottom,tvTime,tvStartPoint,tvEndPoint,tvCost;

        LinearLayout llLeftTop,llLeftBottom,llRight;
        Context context;

        public ContactViewHolder(View gridView,Context mContext) {
            super(gridView);
            context=mContext;
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

        @Override
        public void onClick(View v) {


        }
    }


    private class AddToFav extends AsyncTask<String,String,String> {

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


    public void updateAdapter(int pos,String ss) {
        Constant.tripFav[pos]=ss;

        //and call notifyDataSetChanged

        notifyDataSetChanged();
    }
}
