package com.aggregator.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aggregator.BL.AddFav;
import com.aggregator.Constant.Constant;
import com.aggregator.loop.R;

/**
 * Created by Pranav Mittal on 9/5/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class FavRouteAdapter extends BaseAdapter  {

    Context mContext;
    ImageButton ivHeart;
    TextView tvSource,tvDestination;
    LinearLayout ll;
    String userId;
    AddFav objAddFav;
    TextView source,destination;
    RecentRouteAdapter objRecentRouteAdapter;

    public  FavRouteAdapter(Context context,TextView s,TextView d,String userID){
        mContext=context;
        userId=userID;
        objAddFav=new AddFav();
        source=s;
        destination=d;

    }



    @Override
    public int getCount() {
        return Constant.favRouteID.length;
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
            gridView= infalInflater.inflate(R.layout.fav_list_raw, null);

            tvSource= (TextView) gridView.findViewById(R.id.fav_source);
            tvDestination= (TextView) gridView.findViewById(R.id.fav_destination);
            ivHeart= (ImageButton) gridView.findViewById(R.id.fav_heart);
            ll= (LinearLayout) gridView.findViewById(R.id.fav_ll);

        }

            ivHeart.setBackgroundResource(R.drawable.ic_red_heart);

        if(Constant.favSelectedItem==position)
        {
            gridView.setBackgroundColor(Color.parseColor("#66daae"));
        }
        else
        {
            gridView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        //ivHeart.setOnClickListener(this);
        //ivHeart.setTag(position);
      /*  ll.setOnClickListener(this);
        ll.setTag(position);*/

        tvSource.setText(Constant.favRouteStartName[position]);
        tvDestination.setText(Constant.favRouteEndName[position]);

        gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
        return gridView;
    }

   /* @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fav_heart:
                int position=(int)v.getTag();
                try {
                    String result=new GetFav().execute(userId,Constant.favRouteID[position],Constant.favRouteStartID[position],Constant.favRouteEndID[position]).get();
                    if(result.equals(Constant.WS_RESULT_SUCCESS)){
                        Toast.makeText(mContext, "Added Fav", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(mContext,"Remove Fav",Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.fav_ll:
                int pos=(int)v.getTag();
                System.out.println("POSITION"+pos);
                source.setText(Constant.favRouteStartName[pos]);
                destination.setText(Constant.favRouteEndName[pos]);
                Log.d("Source--->", Constant.favRouteStartName[pos]);
                Log.d("Destination--->",Constant.favRouteEndName[pos]);
                Constant.favSelectedItem=pos;
                Constant.recSelectedItem=-1;

                //objRecentRouteAdapter.notifyDataSetChanged();
                break;
        }
    }*/

    private class GetFav extends AsyncTask<String,String,String> {

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
}
