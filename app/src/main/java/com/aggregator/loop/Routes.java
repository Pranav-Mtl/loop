package com.aggregator.loop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.Adapters.FavRouteAdapter;
import com.aggregator.Adapters.RecentRouteAdapter;
import com.aggregator.Adapters.RoutesAdapter;
import com.aggregator.Adapters.RoutesAdapterSearch;
import com.aggregator.BL.RoutesBL;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Routes extends AppCompatActivity implements View.OnClickListener {

    TextView tvRecentOne,tvRecentTwo;
    RoutesBL objRoutesBL;
    ProgressDialog mProgressDialog;
    ImageButton btnDone;

    AutoCompleteTextView tvSearchRoute;
    ImageButton routesCross;

    ExpandableListView expListView;
    private boolean[] mGroupStates;
    private boolean[] mGroupSearch;
    private boolean[] mGroupSearchedList;

    ArrayList<String> adapterList = new ArrayList<String>();

    String TITLES[] = {"Book a Ride","Trips","Promos","Invite & Earn","Rate Us","Notifications","Tutorial","Help",};

    int ICONS[] = {R.drawable.ic_side_trips,R.drawable.ic_side_bus, R.drawable.ic_side_promo,R.drawable.ic_side_invite_earn,R.drawable.ic_side_rate,R.drawable.ic_side_notification, R.drawable.ic_side_tutorial,R.drawable.ic_side_help};
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    List<String> listDataHeaderSearch;
    HashMap<String, List<String>> listDataChildSearch;

    RoutesAdapter objRoutesAdapter;
    RoutesAdapterSearch objRoutesAdapterSearch;

    int routeID=-1;

    View _lastColored,_itemColoured;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;

    String NAME = "";
    String LoopCredit = "";
    String PayTMWalet ="";
    String logInType;

    View currentGroup;

    private Toolbar toolbar;

    LinearLayout llFavourite,llAllRoute,llBottomRoute,llRecent,llNoRoute;

    RelativeLayout rlMain;

    ExpandableListView elvSearch;

    TextView tvTabRoute,tvTabFav;
    TextView tvSelectedRouteSource,tvSelectedRouteDestination;

    ListView lvFav,lvRecent;

    int selectedGroupPosition=-1;

    FavRouteAdapter objFavRouteAdapter;
    RecentRouteAdapter objRecentRouteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);


        btnDone= (ImageButton) findViewById(R.id.route_done);
        routesCross= (ImageButton) findViewById(R.id.routes_cross);
        expListView= (ExpandableListView) findViewById(R.id.expandable_list);
        tvSearchRoute= (AutoCompleteTextView) findViewById(R.id.route_search);
        llFavourite= (LinearLayout) findViewById(R.id.route_layout_fav);
        llRecent= (LinearLayout) findViewById(R.id.route_layout_recent);
        llAllRoute= (LinearLayout) findViewById(R.id.route_layout_allroute);
        llBottomRoute= (LinearLayout) findViewById(R.id.ll_bottom_route);
        llNoRoute= (LinearLayout) findViewById(R.id.route_layout_norecent);
        rlMain= (RelativeLayout) findViewById(R.id.route_main);
        lvFav= (ListView) findViewById(R.id.routes_lv_fav);
        lvRecent= (ListView) findViewById(R.id.routes_lv_recent);

        tvSelectedRouteSource= (TextView) findViewById(R.id.routes_selected_source);
        tvSelectedRouteDestination= (TextView) findViewById(R.id.routes_selected_destination);


        tvTabRoute= (TextView) findViewById(R.id.toolbar_Tab1);
        tvTabFav= (TextView) findViewById(R.id.toolbar_Tab2);

        elvSearch=(ExpandableListView) findViewById(R.id.expandable_searched_list);
        NAME="Full Name";
        LoopCredit="Loop Credit: Rs. 500";
        PayTMWalet="Paytm Wallet: Rs 200";


        logInType=Util.getSharedPrefrenceValue(Routes.this,Constant.SHARED_PREFERENCE_User_id);

        if(logInType==null){
            TITLES=new String[3];
            TITLES[0]="Book a Ride";
            TITLES[1]="Tutorial";
            TITLES[2]="Rate us";
            ICONS=new int[3];
            ICONS[0]=R.drawable.ic_side_bus;
            ICONS[1]=R.drawable.ic_side_tutorial;
            ICONS[2]=R.drawable.ic_side_rate;

            NAME="Sign In";
            LoopCredit="";
            PayTMWalet="";
        }




        objRoutesBL=new RoutesBL();
        mProgressDialog=new ProgressDialog(Routes.this);


        final View activityRootView = findViewById(R.id.DrawerLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();

                System.out.println("Height :" + heightDiff);

                if (heightDiff > 200) {
                    // keyboard is
                    Log.d("SoftKeyboard", "Soft keyboard shown");
                    routesCross.setVisibility(View.VISIBLE);

                } else {
                    // keyboard is down
                    Log.d("SoftKeyboard", "Soft keyboard hidden");
                    elvSearch.setVisibility(View.GONE);
                    expListView.setVisibility(View.VISIBLE);
                    tvSearchRoute.setText("");
                    routesCross.setVisibility(View.INVISIBLE);

                }
            }
        });




        tvSearchRoute.clearFocus();
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //System.out.println("At home Screen2");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("");



        drawerAdapter = new DrawerAdapter(TITLES, ICONS, NAME, LoopCredit,PayTMWalet, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

        // And passing the titles,icons,header view name, header view email,
        // and header  view profile picture
        mRecyclerView.setAdapter(drawerAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();





        btnDone.setOnClickListener(this);
        routesCross.setOnClickListener(this);
        tvTabRoute.setOnClickListener(this);
        tvTabFav.setOnClickListener(this);



        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideSoftKeyboard(Routes.this);
                if (_lastColored != null) {
                    _lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                    _lastColored.invalidate();
                    btnDone.setVisibility(View.INVISIBLE);
                }
            }
        });

//        mGroupStates = new boolean[objRoutesAdapter.getGroupCount()];


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                                                int previousGroup = -1;

                                                @Override
                                                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                                                    currentGroup=v;

                                                    selectedGroupPosition=groupPosition;

                                                    Constant.favSelectedItem=-1;
                                                    Constant.recSelectedItem=-1;

                                                    routeID = Integer.valueOf(Constant.routeId[groupPosition]);


                                                    mGroupStates[groupPosition] = !mGroupStates[groupPosition];
                                                    llBottomRoute.setVisibility(View.VISIBLE);
                                                    btnDone.setVisibility(View.VISIBLE);
                                                    // Check expanding or collapsing
                                                    System.out.println("GROUP COLLAPSE DATA" + mGroupStates[groupPosition]);
                                                    if (mGroupStates[groupPosition]) {
                                                        // group is being expanded


                                                    } else {
                                                        // group is being collapsed
                                                        llBottomRoute.setVisibility(View.INVISIBLE);
                                                        btnDone.setVisibility(View.INVISIBLE);
                                                    }
                                                    if (_lastColored != null) {
                                                        _lastColored.setBackgroundColor(getResources().getColor(R.color.WhiteColor));
                                                        _lastColored.invalidate();

                                                    }





                                                  /*  _lastColored = currentGroup;
                                                    currentGroup.setBackgroundColor(getResources().getColor(R.color.RouteSelectedColor));*/



                                                    String headerName = listDataHeader.get(groupPosition);

                                                    String SDSplit[] = headerName.split("-");
                                                    String source = SDSplit[0];
                                                    String destination = SDSplit[1];



                                                    tvSelectedRouteSource.setText(source);
                                                    tvSelectedRouteDestination.setText(destination);

                                                   // btnDone.setVisibility(View.VISIBLE);
                                                    return false;
                                                }

                                            }

        );



      expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);

                System.out.println("PREVIOUS GROUP" + previousGroup);
                System.out.println("CURRENT GROUP" + groupPosition);
                previousGroup = groupPosition;
                llBottomRoute.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
            }

        });


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                /* btnDone.setVisibility(View.VISIBLE);
                if (_lastColored != null) {
                    _lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                    _lastColored.invalidate();
                }
                _lastColored = v;
                v.setBackgroundColor(Color.parseColor("#e4e4e4"));*/
                routeID = Integer.valueOf(Constant.routeId[groupPosition]);

                return false;
            }
        });

        //new GetRoutes().execute();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {



                        if (position != 0) {
                            if (_itemColoured != null) {
                                _itemColoured.setBackgroundColor(Color.parseColor("#1fc796"));
                                _itemColoured.invalidate();
                            }
                            _itemColoured = view;
                            view.setBackgroundColor(Color.parseColor("#66daae"));
                        }

                            if(logInType==null){
                                if(position==0)
                                {
                                    startActivity(new Intent(getApplicationContext(),SignIn.class));
                                }
                                else if(position==1){

                                }
                            }
                            else {
                                if(position==0){
                                    startActivity(new Intent(getApplicationContext(),LoopProfile.class));
                                }
                                else if(position==2){
                                    startActivity(new Intent(getApplicationContext(),TripHistory.class));
                                }
                                else if(position==4){
                                    startActivity(new Intent(getApplicationContext(),InviteActivity.class));
                                }
                                else if(position==8){
                                    startActivity(new Intent(getApplicationContext(),HelpActivity.class));
                                }
                                else if(position==7){

                                }
                                else if(position==6){
                                    //startActivity(new Intent(getApplicationContext(),TripFeedback.class));
                                }

                            }




                    }
                }));


        tvSearchRoute.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1 && s.length() <= 10) {
                    // Toast.makeText(getApplicationContext(),"char:"+s,Toast.LENGTH_LONG).show();
                    new GetSearchedRoutes().execute(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        if(Util.isInternetConnection(Routes.this))
        {

            if(logInType==null)
            {

                try {
                    tvTabRoute.setText("Routes");
                    tvTabRoute.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                    tvTabFav.setText("Recent");
                    llAllRoute.setVisibility(View.VISIBLE);
                    llFavourite.setVisibility(View.GONE);
                    llNoRoute.setVisibility(View.GONE);
                    new GetRoutes().execute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else
            {

                tvTabRoute.setText("Routes");
                tvTabFav.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                tvTabFav.setText("Favourites");
                llAllRoute.setVisibility(View.GONE);
                new GetRoutesLogin().execute("10");
            }
        }
        //prepareListData();
        else
        {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Routes.this);

// Setting Dialog Title
            alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);
// Setting Dialog Message
            alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);
// Setting Icon to Dialog

// Setting Positive "Yes" Btn
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
// Setting Neg57ative "NO" Btn
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog

                            dialog.cancel();
                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();
        }



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
           case R.id.route_done:
               startActivity(new Intent(getApplicationContext(), BookingNew.class).putExtra("RouteId", routeID));
                break;
            case R.id.toolbar_Tab1:

                tvTabRoute.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                tvTabFav.setBackgroundColor(getResources().getColor(R.color.LightGreen));
                llAllRoute.setVisibility(View.VISIBLE);
                llFavourite.setVisibility(View.GONE);

                llRecent.setVisibility(View.GONE);
                llNoRoute.setVisibility(View.GONE);

                btnDone.setVisibility(View.VISIBLE);

                if (_lastColored != null) {
                    _lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                    _lastColored.invalidate();
                }
                break;
            case R.id.toolbar_Tab2:
                tvTabRoute.setBackgroundColor(getResources().getColor(R.color.LightGreen));
                tvTabFav.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                btnDone.setVisibility(View.VISIBLE);
                if (_lastColored != null) {
                    _lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                    _lastColored.invalidate();
                }
                if(logInType==null)
                {
                    llAllRoute.setVisibility(View.GONE);
                    llNoRoute.setVisibility(View.VISIBLE);
                }
                else {
                    if(Constant.recentJson)
                       objRecentRouteAdapter.notifyDataSetChanged();
                    if(Constant.favJson)
                        objFavRouteAdapter.notifyDataSetChanged();

                    llAllRoute.setVisibility(View.GONE);
                    llNoRoute.setVisibility(View.GONE);
                    if(Constant.favJson){
                        llFavourite.setVisibility(View.VISIBLE);

                    }
                    if(Constant.recentJson){
                        llRecent.setVisibility(View.VISIBLE);

                    }
                }
                break;
            case R.id.routes_cross:
                elvSearch.setVisibility(View.GONE);
                expListView.setVisibility(View.VISIBLE);
                tvSearchRoute.setText("");
                if (_lastColored != null) {
                    _lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                    _lastColored.invalidate();
                }
                break;
        }
    }

    private class GetRoutes extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String result=objRoutesBL.getAllRoutes();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                prepareListData();
                objRoutesAdapter = new RoutesAdapter(Routes.this, listDataHeader, listDataChild,llBottomRoute);
                expListView.setAdapter(objRoutesAdapter);
                mGroupStates = new boolean[objRoutesAdapter.getGroupCount()];
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<String> TrainerTutor = new ArrayList<String>();
        //String strSubRoute[]=new String[Constant.routeId.length];


        // Adding child data

        for(int i=0;i<Constant.routeId.length;i++){
            System.out.println("Route name"+Constant.routeName[i]);
            System.out.println("Route expand"+Constant.routeExpand[i]);
            listDataHeader.add(Constant.routeName[i]);
            TrainerTutor.add(Constant.routeExpand[i]);

        }

        for(int i=0;i<Constant.routeId.length;i++){
            List<String> listRoute = new ArrayList<String>();
            listRoute.add(Constant.routeExpand[i]);
            listDataChild.put(listDataHeader.get(i),listRoute);
        }

       // Adding child data


       /* List<String> TrainerTutor2 = new ArrayList<String>();
        TrainerTutor2.add("Vikhroli Station > Vikhroli Park Site > Powai - Colgate > Hiranandani - Galleria > Hiranandani - Aromas > Supreme Biz Park");

        List<String> TrainerTutor3 = new ArrayList<String>();
        TrainerTutor3.add("Nahar Amrit Shati - Zara > Nahar Amrit Shakt - Dmart > Raheja - Main Gate > Bommerang > Sakinaka Metro ");


        listDataChild.put(listDataHeader.get(1), TrainerTutor2);
        listDataChild.put(listDataHeader.get(2), TrainerTutor3);*/
    }


        @Override
        public void onBackPressed() {

            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", null).show();
        }


    private class GetSearchedRoutes extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

           /* progressDialog.show();
            progressDialog.setMessage("Loading");*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String URL="route_name="+ params[0];
            String txtJson = RestFullWS.callWS(URL, Constant.WEBSERVICE_Route_Search);
            return txtJson;
        }

        @Override
        protected void onPostExecute(String result) {

            //set adapter here
            try {

                validate(result);
                prepareListSearchData();
                expListView.setVisibility(View.GONE);
                elvSearch.setVisibility(View.VISIBLE);
                objRoutesAdapterSearch = new RoutesAdapterSearch(Routes.this, listDataHeaderSearch, listDataChildSearch);

                elvSearch.setAdapter(objRoutesAdapterSearch);

                mGroupSearch = new boolean[objRoutesAdapterSearch.getGroupCount()];


                elvSearch.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                                                      int previousGroup = -1;

                                                      @Override
                                                      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                                                        // btnDone.setVisibility(View.VISIBLE);

                                                          // Check expanding or collapsing

                                                          if (_lastColored != null) {
                                                              _lastColored.setBackgroundColor(getResources().getColor(R.color.WhiteColor));
                                                              _lastColored.invalidate();
                                                          }

                                                          Constant.favSelectedItem=-1;
                                                          Constant.recSelectedItem=-1;

                                                          mGroupSearch[groupPosition] = !mGroupSearch[groupPosition];
                                                          // Check expanding or collapsing
                                                          llBottomRoute.setVisibility(View.VISIBLE);
                                                          btnDone.setVisibility(View.VISIBLE);
                                                          if (mGroupSearch[groupPosition]) {
                                                              // group is being expanded

                                                          } else {
                                                              // group is being collapsed
                                                              llBottomRoute.setVisibility(View.INVISIBLE);
                                                              btnDone.setVisibility(View.INVISIBLE);
                                                          }

                                                      /*    _lastColored = v;
                                                          v.setBackgroundColor(getResources().getColor(R.color.RouteSelectedColor));*/

                                                          String headerName= listDataHeaderSearch.get(groupPosition);

                                                          String SDSplit[]=headerName.split("-");
                                                          String source=SDSplit[0];
                                                          String destination=SDSplit[1];

                                                          llBottomRoute.setVisibility(View.VISIBLE);

                                                          tvSelectedRouteSource.setText(source);
                                                          tvSelectedRouteDestination.setText(destination);

                                                          //btnDone.setVisibility(View.VISIBLE);

                                                          return false;
                                                      }

                                                  }

                );


                elvSearch.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)
                            elvSearch.collapseGroup(previousGroup);

                        System.out.println("PREVIOUS GROUP" + previousGroup);
                        System.out.println("CURRENT GROUP" + groupPosition);
                        previousGroup = groupPosition;

                    }

                });

                elvSearch.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


                        if (_lastColored != null) {
                            _lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                            _lastColored.invalidate();
                        }
                        _lastColored = v;
                        v.setBackgroundColor(Color.parseColor("#e4e4e4"));
                        routeID=Integer.valueOf(Constant.routeId[groupPosition]);
                        return false;
                    }
                });

                //progressDialog.dismiss();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }






    private String validate(String result)
    {
        String status="";
        JSONParser jsonP=new JSONParser();
        try {

            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            Constant.routeSearchId=new String[jsonArrayObject.size()];
            Constant.routeSearchName=new String[jsonArrayObject.size()];
            Constant.routeSearchExpand=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.routeSearchId[i]=jsonObject.get("route_id").toString();
                Constant.routeSearchName[i]=jsonObject.get("route_name").toString();
                Constant.routeSearchExpand[i]=jsonObject.get("sub_route").toString().replace("[","").replace("]","").replaceAll("\"", "").replaceAll(",", " ● ");
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return "";
    }

    private void prepareListSearchData() {
        listDataHeaderSearch = new ArrayList<String>();
        listDataChildSearch = new HashMap<String, List<String>>();
        List<String> TrainerTutor = new ArrayList<String>();
        String strSubRoute[] = new String[Constant.routeSearchId.length];


        // Adding child data

        for (int i = 0; i < Constant.routeSearchId.length; i++) {
            System.out.println("Route name" + Constant.routeSearchName[i]);
            System.out.println("Route expand" + Constant.routeSearchExpand[i]);
            listDataHeaderSearch.add(Constant.routeSearchName[i]);
            TrainerTutor.add(Constant.routeSearchExpand[i]);
        }

        for (int i = 0; i < Constant.routeSearchId.length; i++) {
            List<String> listRoute = new ArrayList<String>();
            listRoute.add(Constant.routeSearchExpand[i]);
            listDataChildSearch.put(listDataHeaderSearch.get(i), listRoute);
        }
    }


    private class GetRoutesLogin extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String result=objRoutesBL.getAllRoutesLogin(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try
            {
                llBottomRoute.setVisibility(View.VISIBLE);
                if(Constant.favJson)
                {
                    Log.d("Fav json truw-->",Constant.favJson+"");
                    llFavourite.setVisibility(View.VISIBLE);
                    objFavRouteAdapter=new FavRouteAdapter(getApplicationContext(),tvSelectedRouteSource,tvSelectedRouteDestination,logInType);
                    lvFav.setAdapter(objFavRouteAdapter);
                }
                if(Constant.recentJson)
                {
                    Log.d("Recent json truw-->",Constant.recentJson+"");
                    llRecent.setVisibility(View.VISIBLE);
                    objRecentRouteAdapter=new RecentRouteAdapter(getApplicationContext(),tvSelectedRouteSource,tvSelectedRouteDestination,logInType);
                    lvRecent.setAdapter(objRecentRouteAdapter);
                }


                prepareListData();
                objRoutesAdapter = new RoutesAdapter(Routes.this, listDataHeader, listDataChild,llBottomRoute);
                expListView.setAdapter(objRoutesAdapter);

                mGroupStates = new boolean[objRoutesAdapter.getGroupCount()];

                tvTabFav.setText(Constant.Tab2Name);

                lvFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final int pos=position;


                                String source = Constant.favRouteStartName[pos];
                                String destination = Constant.favRouteEndName[pos];
                                Constant.favSelectedItem=pos;
                                objFavRouteAdapter.notifyDataSetChanged();

                                routeID = Integer.valueOf(Constant.favRouteID[pos]);
                                if(Constant.recentJson)
                                {
                                    Constant.recSelectedItem=-1;
                                    objRecentRouteAdapter.notifyDataSetChanged();
                                }



                       /* if (_lastColored != null) {
                            _lastColored.setBackgroundColor(getResources().getColor(R.color.WhiteColor));
                            _lastColored.invalidate();
                        }
                        _lastColored = view;
                        view.setBackgroundColor(getResources().getColor(R.color.RouteSelectedColor));
*/
                                llBottomRoute.setVisibility(View.VISIBLE);

                                tvSelectedRouteSource.setText(source);
                                tvSelectedRouteDestination.setText(destination);
                                if(selectedGroupPosition!=-1) {
                                    expListView.collapseGroup(selectedGroupPosition);
                                    objRoutesAdapter.notifyDataSetChanged();
                                }
                                btnDone.setVisibility(View.VISIBLE);



                    }
                });

                lvRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String source=Constant.recentRouteStartName[position];
                        String destination=Constant.recentRouteEndName[position];

                        Constant.recSelectedItem=position;

                        routeID = Integer.valueOf(Constant.recentRouteID[position]);

                        objRecentRouteAdapter.notifyDataSetChanged();

                        if(Constant.favJson)
                        {
                            Constant.favSelectedItem=-1;
                            objFavRouteAdapter.notifyDataSetChanged();
                        }

                        /*if (_lastColored != null) {
                            _lastColored.setBackgroundColor(getResources().getColor(R.color.WhiteColor));
                            _lastColored.invalidate();
                        }
                        _lastColored = view;
                        view.setBackgroundColor(getResources().getColor(R.color.RouteSelectedColor));*/



                        tvSelectedRouteSource.setText(source);
                        tvSelectedRouteDestination.setText(destination);

                        if(selectedGroupPosition!=-1) {
                            expListView.collapseGroup(selectedGroupPosition);
                            objRoutesAdapter.notifyDataSetChanged();
                        }

                        btnDone.setVisibility(View.VISIBLE);

                    }
                });


            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                mProgressDialog.dismiss();
            }



        }
    }


}
