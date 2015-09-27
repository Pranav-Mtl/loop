package com.aggregator.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aggregator.BL.AddFav;
import com.aggregator.Constant.Constant;
import com.aggregator.loop.R;
import com.aggregator.loop.RouteNew;

/**
 * Created by Pranav Mittal on 9/19/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RecList>{

    Context mContext;

    String userId;
    AddFav objAddFav;
    TextView source,destination;
    ProgressDialog mProgressDialog;
    RouteNew objRouteNew;
    RelativeLayout llBottom;
    ImageButton btnDone;

    int position;

    public  RouteAdapter(Context context,TextView s,TextView d,String userID,RelativeLayout ll,ImageButton btn,RouteNew routeNew){
        mContext=context;
        userId=userID;
        objAddFav=new AddFav();
        source=s;
        destination=d;
        objRouteNew=routeNew;
        llBottom=ll;
        btnDone=btn;
        mProgressDialog=new ProgressDialog(objRouteNew);

    }


    @Override
    public RecList onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.fav_list_raw, parent, false);

        return new RecList(itemView,mContext);
    }

    @Override
    public void onBindViewHolder(RecList holder, int position) {

        if("No".equals(Constant.recentRouteFavStatus[position]))
             holder.ivHeart.setBackgroundResource(R.drawable.ic_white_heart);
        else
            holder.ivHeart.setBackgroundResource(R.drawable.ic_red_heart);
        // ivHeart.setOnClickListener(this);
        //ivHeart.setTag(position);


        holder.ivHeart.setOnClickListener(clickListener);
        holder.ll.setOnClickListener(clickListener);

        holder.ivHeart.setTag(holder);
        holder.ll.setTag(holder);

        holder.tvSource.setText(Constant.recentRouteStartName[position]);
        holder.tvDestination.setText(Constant.recentRouteEndName[position]);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecList holder = (RecList) view.getTag();
             position = holder.getAdapterPosition();
            switch (view.getId()) {
                case R.id.fav_heart:

                    try {
                       new GetFav().execute(userId, Constant.recentRouteID[position], Constant.recentRouteStartID[position], Constant.recentRouteEndID[position]);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.fav_ll:
                    llBottom.setVisibility(View.VISIBLE);
                    btnDone.setVisibility(View.VISIBLE);
                    System.out.println("POSITION" + position);
                    source.setText(Constant.recentRouteStartName[position]);
                    destination.setText(Constant.recentRouteEndName[position]);
                    Log.d("Source--->", Constant.recentRouteStartName[position]);
                    Log.d("Destination--->", Constant.recentRouteEndName[position]);
                    Constant.recSelectedItem = position;
                    Constant.favSelectedItem = -1;
                    objRouteNew.routeID=Integer.valueOf(Constant.recentRouteID[position]);

                    if (Constant._lastColored != null) {
                        if(Constant._lastColored==view){
                            Constant._lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                            Constant._lastColored.invalidate();
                            llBottom.setVisibility(View.GONE);
                            btnDone.setVisibility(View.INVISIBLE);
                            Constant._lastColored=null;
                        }
                        else {
                            Constant._lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                            Constant._lastColored.invalidate();
                            Constant._lastColored = view;
                            view.setBackgroundColor(Color.parseColor("#66daae"));
                        }
                    }
                    else {
                        Constant._lastColored = view;
                        view.setBackgroundColor(Color.parseColor("#66daae"));
                    }

                    //objFavRouteAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    public int getItemCount() {
        return Constant.recentRouteID.length;
    }

    public static  class RecList extends RecyclerView.ViewHolder{

        ImageButton ivHeart;
        TextView tvSource,tvDestination;
        LinearLayout ll;
        Context mContext;

        public RecList(View itemView,Context context) {
            super(itemView);
            mContext=context;
            tvSource= (TextView) itemView.findViewById(R.id.fav_source);
            tvDestination= (TextView) itemView.findViewById(R.id.fav_destination);
            ivHeart= (ImageButton) itemView.findViewById(R.id.fav_heart);
            ll= (LinearLayout) itemView.findViewById(R.id.fav_ll);
        }
    }
    private class GetFav extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objAddFav.addfav(params[0], params[1], params[2], params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try
            {
                if (result.equals(Constant.WS_RESULT_SUCCESS)) {
                    Toast.makeText(mContext, "Route Added to Fav.", Toast.LENGTH_SHORT).show();
                    //Constant.recentRouteFavStatus[position]="Yes";
                    mContext.startActivity(new Intent(objRouteNew,RouteNew.class).setFlags (Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(mContext, "Route Removed from Fav.", Toast.LENGTH_SHORT).show();
                    //Constant.recentRouteFavStatus[position]="No";
                    mContext.startActivity(new Intent(objRouteNew,RouteNew.class).setFlags (Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                notifyDataSetChanged();

            }catch (Exception e){

            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }
}
