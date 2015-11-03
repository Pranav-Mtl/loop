package com.aggregator.loop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aggregator.Adapters.DrawerAdapter;
import com.aggregator.Adapters.FavouriteAdapter;
import com.aggregator.Adapters.RouteAdapter;
import com.aggregator.Adapters.RoutesAdapter;
import com.aggregator.Adapters.RoutesAdapterSearch;
import com.aggregator.BL.RoutesBL;
import com.aggregator.Configuration.IMMResult;
import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;
import com.aggregator.WS.RestFullWS;
import com.aggregator.db.DBOperation;
import com.appsee.Appsee;
import com.google.android.gms.analytics.HitBuilders;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteNew extends AppCompatActivity implements View.OnClickListener,ExpandableListView.OnGroupClickListener  {

    TextView tvRecentOne,tvRecentTwo;
    RoutesBL objRoutesBL;
    ProgressDialog mProgressDialog;
    ImageView btnDone;

    AutoCompleteTextView tvSearchRoute;
    ImageButton routesCross;

    ExpandableListView expListView;
    private boolean[] mGroupStates;
    private boolean[] mGroupSearch;
    private boolean[] mGroupSearchedList;

    ArrayList<String> adapterList = new ArrayList<String>();

    String TITLES[] = {"Book a Ride","Trips","Recharge Loop Wallet","Promos","Invite & Earn","Notifications","Suggest A Route","Rate Us","Tutorial","Help",};

    int ICONS[] = {R.drawable.ic_side_trips,R.drawable.ic_side_bus,R.drawable.ic_side_credit, R.drawable.ic_side_promo,R.drawable.ic_side_invite_earn,R.drawable.ic_side_notification,R.drawable.ic_side_suggest,R.drawable.ic_side_rate, R.drawable.ic_side_tutorial,R.drawable.ic_side_help};
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    List<String> listDataHeaderSearch;
    HashMap<String, List<String>> listDataChildSearch;

    RoutesAdapter objRoutesAdapter;
    RoutesAdapterSearch objRoutesAdapterSearch;

    public int routeID=-1;



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

    boolean isOpened = false;

    View currentGroup;

    private Toolbar toolbar;

    LinearLayout llFavourite,llAllRoute,llRecent,llNoRoute,llBottomDone;

    RelativeLayout rlMain,llBottomRoute;

    ExpandableListView elvSearch;

    TextView tvTabRoute,tvTabFav;
    TextView tvSelectedRouteSource,tvSelectedRouteDestination;

    android.support.v7.widget.RecyclerView lvFav,lvRecent;

    int selectedGroupPosition=-1;
    int selectedSearchGroup=-1;

    FavouriteAdapter objFavRouteAdapter;
    RouteAdapter objRecentRouteAdapter;

    View _itemColoured;

    int xx,yy;

    DBOperation dbOperation;

    boolean internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_new);

        initialize();

        Appsee.start("de8395d3ae424245b695b4c9d6642f71");

        Display display = getWindowManager().getDefaultDisplay();

        // Point size = new Point();
        // display.getSize(size);
        int width = display.getWidth();
        int height = display.getHeight();

       // System.out.println("width" + width + "height" + height);

        if(width>=1000 && height>=1500){
            xx=700;
            yy=800;
        }
        else if(width>=700 && height>=1000)
        {
            xx=500;
            yy=500;
        }
        else
        {
            xx=400;
            yy=500;
        }

        final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                Log.d("Height Diff",heightDiff+"");

                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                    //Toast.makeText(getApplicationContext(), "Gotcha!!! softKeyboardup", Toast.LENGTH_SHORT).show();

                    routesCross.setVisibility(View.VISIBLE);

                    if (isOpened == false) {
                        //Do two things, make the view top visible and the editText smaller
                    }
                    isOpened = true;
                } else if (isOpened == true) {

                    if (tvSearchRoute.getText().toString().trim().length() == 0) {
                        elvSearch.setVisibility(View.GONE);
                        expListView.setVisibility(View.VISIBLE);
                        routesCross.setVisibility(View.INVISIBLE);
                    } else {
                        tvSearchRoute.setText("");
                        routesCross.setVisibility(View.INVISIBLE);
                    }
                    // Toast.makeText(getApplicationContext(), "softkeyborad Down!!!", Toast.LENGTH_SHORT).show();
                    isOpened = false;
                }
            }
        });

       /* final View activityRootView = findViewById(R.id.DrawerLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();

                System.out.println("Height :" + heightDiff);

                if (heightDiff >100) {
                    // keyboard is
                    Log.d("SoftKeyboard", "Soft keyboard shown");

                } else {
                    // keyboard is down
                    Log.d("SoftKeyboard", "Soft keyboard hidden");






                }
            }
        });*/





       // btnDone.setOnClickListener(this);
        routesCross.setOnClickListener(this);
        tvTabRoute.setOnClickListener(this);
        tvTabFav.setOnClickListener(this);
        expListView.setOnGroupClickListener(this);
        elvSearch.setOnGroupClickListener(this);
        llBottomDone.setOnClickListener(this);




        logInType= Util.getSharedPrefrenceValue(RouteNew.this, Constant.SHARED_PREFERENCE_User_id);


        if(logInType==null) {
            TITLES = new String[3];
            TITLES[0] = "Book a Ride";
            TITLES[1] = "Tutorial";
            TITLES[2] = "Rate us";
            ICONS = new int[3];
            ICONS[0] = R.drawable.ic_side_bus;
            ICONS[1] = R.drawable.ic_side_tutorial;
            ICONS[2] = R.drawable.ic_side_rate;

            Constant.NAME = "Sign In";
            Constant.LoopCredit = "";
            Constant.PayTMWalet = "";
        }

        if(Util.isInternetConnection(RouteNew.this)){
            internetConnection=true;
        }
        else {
            internetConnection=false;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);

        drawerAdapter = new DrawerAdapter(TITLES, ICONS, Constant.NAME, Constant.LoopCredit,Constant.PayTMWalet, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

        // And passing the titles,icons,header view name, header view email,
        // and header  view profile picture
        mRecyclerView.setAdapter(drawerAdapter);

        mLayoutManager = new com.twotoasters.android.support.v7.widget.LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //System.out.println("At home Screen2");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("");



        // Setting the adapter to RecyclerView


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


            if(logInType==null)
            {
                if(internetConnection) {
                    try {
                        tvTabRoute.setText("Routes");
                        tvTabRoute.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                        tvTabFav.setText("Recent");
                        llAllRoute.setVisibility(View.VISIBLE);
                        llFavourite.setVisibility(View.GONE);
                        llNoRoute.setVisibility(View.GONE);
                        new GetRoutes().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    showDialogConnection(this);
                }
            }
            else
            {
                if(internetConnection) {
                    tvTabRoute.setText("Routes");
                    tvTabFav.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                    tvTabFav.setText("Favourites");
                    llAllRoute.setVisibility(View.GONE);
                    llFavourite.setVisibility(View.GONE);
                    llNoRoute.setVisibility(View.GONE);
                    new GetRoutesLogin().execute(logInType);
                }
                else {
                    //setOfflineData();
                    showDialogConnection(this);
                }


            }



        /* GET DATA FOR SEARCHED KEYWORD*/

        tvSearchRoute.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1 && s.length() <= 10) {
                    // Toast.makeText(getApplicationContext(),"char:"+s,Toast.LENGTH_LONG).show();
                    if(internetConnection){
                    llBottomRoute.setVisibility(View.GONE);
                    btnDone.setVisibility(View.INVISIBLE);
                    new GetSearchedRoutes().execute(s.toString());
                    }
                    else {
                        showDialogConnection(RouteNew.this);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (position != 0) {
                            if (_itemColoured != null) {
                                _itemColoured.setBackgroundColor(Color.parseColor("#66daae"));
                                _itemColoured.invalidate();
                            }
                            _itemColoured = view;
                            view.setBackgroundColor(Color.parseColor("#1fc796"));
                        }

                        if (logInType == null) {
                            if (position == 0) {
                                startActivity(new Intent(getApplicationContext(), SignIn.class));
                            } else if (position == 1) {
                                Drawer.closeDrawers();
                            }
                            else if (position == 2) {
                                startActivity(new Intent(getApplicationContext(), Tutorial.class));
                            }
                            else if (position == 3) {
                                Util.rateUs(getApplicationContext());
                            }
                        } else {

                            if (position == 0) {
                                startActivity(new Intent(getApplicationContext(), LoopProfile.class));
                            } else if (position == 1) {
                                Drawer.closeDrawers();
                            } else if (position == 2) {
                                startActivity(new Intent(getApplicationContext(), TripHistory.class));
                            } else if (position == 4) {
                                startActivity(new Intent(getApplicationContext(), PromoCode.class));
                            } else if (position == 5) {
                                startActivity(new Intent(getApplicationContext(), InviteActivity.class));
                            }
                            else if (position == 6) {
                                startActivity(new Intent(getApplicationContext(), Notification.class));
                            }
                            else if (position == 10) {
                                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                            }
                            else if (position == 3) {
                                startActivity(new Intent(getApplicationContext(), AddLoopCredit.class));
                            }
                            else if (position == 8) {
                                Util.rateUs(getApplicationContext());
                            } else if (position == 9) {
                                startActivity(new Intent(getApplicationContext(), Tutorial.class));
                            } else if (position == 7) {
                                startActivity(new Intent(getApplicationContext(),SuggestRoute.class));
                            }
                        }
                    }
                }));


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

    private void initialize(){
        btnDone= (ImageView) findViewById(R.id.route_done);
        routesCross= (ImageButton) findViewById(R.id.routes_cross);
        expListView= (ExpandableListView) findViewById(R.id.expandable_list);
        tvSearchRoute= (AutoCompleteTextView) findViewById(R.id.route_search);
        llFavourite= (LinearLayout) findViewById(R.id.route_layout_fav);
        llRecent= (LinearLayout) findViewById(R.id.route_layout_recent);
        llAllRoute= (LinearLayout) findViewById(R.id.route_layout_allroute);
        llBottomRoute= (RelativeLayout) findViewById(R.id.ll_bottom_route);
        llBottomDone= (LinearLayout) findViewById(R.id.ll_bottom_route_done);
        llNoRoute= (LinearLayout) findViewById(R.id.route_layout_norecent);
        rlMain= (RelativeLayout) findViewById(R.id.route_main);
        lvFav= (android.support.v7.widget.RecyclerView) findViewById(R.id.routes_lv_fav);
        lvRecent= (android.support.v7.widget.RecyclerView) findViewById(R.id.routes_lv_recent);

        tvSelectedRouteSource= (TextView) findViewById(R.id.routes_selected_source);
        tvSelectedRouteDestination= (TextView) findViewById(R.id.routes_selected_destination);


        tvTabRoute= (TextView) findViewById(R.id.toolbar_Tab1);
        tvTabFav= (TextView) findViewById(R.id.toolbar_Tab2);

        elvSearch=(ExpandableListView) findViewById(R.id.expandable_searched_list);

        objRoutesBL=new RoutesBL();
        mProgressDialog=new ProgressDialog(RouteNew.this);
        dbOperation=new DBOperation(RouteNew.this);
        dbOperation.createAndInitializeTables();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ll_bottom_route_done:
                String strLogin="";
                if(logInType==null){
                    strLogin="Not-logged in";
                }
                else {
                    strLogin="Logged-in";
                }
                 /* call google analytics*/
                try {
                    Application.tracker().setScreenName("Route Screen");
                    Application.tracker().send(new HitBuilders.EventBuilder()
                            .setLabel(strLogin)
                            .setCategory("Routes Screen")
                            .setAction("Next Button")
                            .setValue(routeID)
                            .build());

                    // AffleInAppTracker.inAppTrackerViewName(getApplicationContext(), "Landing Screen", "App First Screen", "APP Open", null);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(), BookingNew.class).putExtra("RouteId", routeID));
                break;
            case R.id.toolbar_Tab1:

                tvTabRoute.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                tvTabFav.setBackgroundColor(getResources().getColor(R.color.LightGreen));
                llAllRoute.setVisibility(View.VISIBLE);
                llFavourite.setVisibility(View.GONE);
                llRecent.setVisibility(View.GONE);
                llNoRoute.setVisibility(View.GONE);
                llBottomRoute.setVisibility(View.GONE);
                btnDone.setVisibility(View.INVISIBLE);

                if (Constant._lastColored != null) {
                    Constant._lastColored.setBackgroundColor(Color.parseColor("#ffffff"));
                    Constant._lastColored.invalidate();
                }
                break;
            case R.id.toolbar_Tab2:
                tvTabRoute.setBackgroundColor(getResources().getColor(R.color.LightGreen));
                tvTabFav.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));

                llBottomRoute.setVisibility(View.GONE);
                btnDone.setVisibility(View.INVISIBLE);

                if(selectedGroupPosition!=-1) {
                    expListView.collapseGroup(selectedGroupPosition);
                    objRoutesAdapter.notifyDataSetChanged();
                }

                if(selectedSearchGroup!=-1) {
                    elvSearch.collapseGroup(selectedSearchGroup);
                    objRoutesAdapterSearch.notifyDataSetChanged();
                }

                if(logInType==null)
                {
                    llAllRoute.setVisibility(View.GONE);
                    llNoRoute.setVisibility(View.VISIBLE);
                }
                else {
                    if(internetConnection) {

                        llAllRoute.setVisibility(View.GONE);
                        llNoRoute.setVisibility(View.GONE);
                        if (Constant.favJson) {
                            llFavourite.setVisibility(View.VISIBLE);

                        }
                        if (Constant.recentJson) {
                            llRecent.setVisibility(View.VISIBLE);

                        }
                    }
                    else {
                        llAllRoute.setVisibility(View.GONE);
                        llNoRoute.setVisibility(View.GONE);
                        llFavourite.setVisibility(View.VISIBLE);
                        llRecent.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.routes_cross:
                elvSearch.setVisibility(View.GONE);
                expListView.setVisibility(View.VISIBLE);
                tvSearchRoute.setText("");
                Util.hideSoftKeyboard(RouteNew.this);
                break;

        }
    }

    /* EXPENDABLE LIST CLICK LISTENER */

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        Log.d("Group Clicked",groupPosition + "");
        Log.d("Group ID", parent.getId() +"");
        Log.d("list ID", R.id.expandable_list + "");
            switch (parent.getId()){
                case R.id.expandable_list:
                   // groupParentClicked(groupPosition);
                    selectedGroupPosition=groupPosition;
                    routeID = Integer.valueOf(Constant.routeId[groupPosition]);


                    llBottomRoute.setVisibility(View.VISIBLE);
                    btnDone.setVisibility(View.VISIBLE);

                    String headerName = listDataHeader.get(groupPosition);

                    String SDSplit[] = headerName.split("-");
                    String source = SDSplit[0];
                    String destination = SDSplit[1];

                    Log.d("Source",source);
                    Log.d("Destination",destination);

                    tvSelectedRouteSource.setText(source);
                    tvSelectedRouteDestination.setText(destination);

                    mGroupStates[groupPosition] = !mGroupStates[groupPosition];

                    if (mGroupStates[groupPosition]) {
                        } else {
                            // group is being collapsed
                            llBottomRoute.setVisibility(View.GONE);
                            btnDone.setVisibility(View.INVISIBLE);
                        }
                    break;
                case R.id.expandable_searched_list:
                    //groupParentClickedSearched(groupPosition);
                    selectedSearchGroup=groupPosition;

                    routeID = Integer.valueOf(Constant.routeSearchId[groupPosition]);

                    Constant.favSelectedItem = -1;
                    Constant.recSelectedItem = -1;

                    mGroupSearch[groupPosition] = !mGroupSearch[groupPosition];
                    // Check expanding or collapsing
                    llBottomRoute.setVisibility(View.VISIBLE);
                    btnDone.setVisibility(View.VISIBLE);

                    String headerName1 = listDataHeaderSearch.get(groupPosition);

                    String SDSplit1[] = headerName1.split("-");
                    String source1 = SDSplit1[0];
                    String destination1 = SDSplit1[1];

                    tvSelectedRouteSource.setText(source1);
                    tvSelectedRouteDestination.setText(destination1);

                    mGroupSearch[groupPosition] = !mGroupSearch[groupPosition];
                    // Check expanding or collapsing


                    if (mGroupSearch[groupPosition]) {
                        // group is being expanded

                    } else {
                        // group is being collapsed
                        llBottomRoute.setVisibility(View.GONE);
                        btnDone.setVisibility(View.INVISIBLE);
                    }
                    break;
            }

        return false;
    }


    private void groupParentClicked(int groupPosition){



        // Check expanding or collapsing
        System.out.println("GROUP COLLAPSE DATA" + mGroupStates[groupPosition]);
       /* if (mGroupStates[groupPosition]) {
            // group is being expanded


        } else {
            // group is being collapsed
            llBottomRoute.setVisibility(View.INVISIBLE);
            btnDone.setVisibility(View.INVISIBLE);
        }*/

        /*  _lastColored = currentGroup;
           currentGroup.setBackgroundColor(getResources().getColor(R.color.RouteSelectedColor));*/




    }

    private void groupParentClickedSearched(int groupPosition){


        Log.d("Search List Clicked", groupPosition + "");



        if (mGroupSearch[groupPosition]) {
            // group is being expanded

        } else {
            // group is being collapsed
             llBottomRoute.setVisibility(View.GONE);
             btnDone.setVisibility(View.INVISIBLE);
        }

                                                      /*    _lastColored = v;
                                                          v.setBackgroundColor(getResources().getColor(R.color.RouteSelectedColor));*/



        //btnDone.setVisibility(View.VISIBLE);
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
                objRoutesAdapter = new RoutesAdapter(RouteNew.this, listDataHeader, listDataChild,llBottomRoute);
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
                objRoutesAdapterSearch = new RoutesAdapterSearch(RouteNew.this, listDataHeaderSearch, listDataChildSearch);

                elvSearch.setAdapter(objRoutesAdapterSearch);

                mGroupSearch = new boolean[objRoutesAdapterSearch.getGroupCount()];

                elvSearch.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)
                            elvSearch.collapseGroup(previousGroup);

                        System.out.println("PREVIOUS GROUP" + previousGroup);
                        System.out.println("CURRENT GROUP" + groupPosition);
                        previousGroup = groupPosition;

                        llBottomRoute.setVisibility(View.VISIBLE);
                        btnDone.setVisibility(View.VISIBLE);

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

            String result=objRoutesBL.getAllRoutesLogin(params[0],dbOperation);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {

                if(Constant.favJson)
                {
                    Log.d("Fav json truw-->",Constant.favJson+"");
                    llFavourite.setVisibility(View.VISIBLE);
                    objFavRouteAdapter=new FavouriteAdapter(getApplicationContext(),tvSelectedRouteSource,tvSelectedRouteDestination,logInType,llBottomRoute,btnDone,RouteNew.this);
                    LinearLayoutManager llm = new LinearLayoutManager(RouteNew.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    lvFav.setLayoutManager(llm);

                    lvFav.setAdapter(objFavRouteAdapter);
                }
                if(Constant.recentJson) {
                    Log.d("Recent json truw-->", Constant.recentJson + "");
                    llRecent.setVisibility(View.VISIBLE);
                    objRecentRouteAdapter = new RouteAdapter(getApplicationContext(), tvSelectedRouteSource, tvSelectedRouteDestination,logInType,llBottomRoute,btnDone,RouteNew.this);
                    lvRecent.setAdapter(objRecentRouteAdapter);
                    LinearLayoutManager llm = new LinearLayoutManager(RouteNew.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    lvRecent.setLayoutManager(llm);
                }

                if(!Constant.favJson && !Constant.recentJson){
                    tvTabRoute.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
                    tvTabFav.setBackgroundColor(getResources().getColor(R.color.LightGreen));
                    llAllRoute.setVisibility(View.VISIBLE);
                    llFavourite.setVisibility(View.GONE);
                    llRecent.setVisibility(View.GONE);
                    llNoRoute.setVisibility(View.GONE);
                    llBottomRoute.setVisibility(View.GONE);
                    btnDone.setVisibility(View.INVISIBLE);
                }

                drawerAdapter.notifyDataSetChanged();

                prepareListData();

                objRoutesAdapter = new RoutesAdapter(RouteNew.this, listDataHeader, listDataChild,llBottomRoute);
                expListView.setAdapter(objRoutesAdapter);

                mGroupStates = new boolean[objRoutesAdapter.getGroupCount()];

                tvTabFav.setText(Constant.Tab2Name);

            }
            catch (NullPointerException e){
                showDialogResponse(RouteNew.this);
            }
            catch (Exception e) {
                showDialogResponse(RouteNew.this);
            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }


  /*  private void NoResponseServer()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constant.ERR_NO_SERVER_RESPONSE)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        if (logInType == null)
                            new GetRoutes().execute();
                        else
                            new GetRoutesLogin().execute(logInType);
                    }
                });


        final AlertDialog alert = builder.create();
        alert.show();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        drawerAdapter.notifyDataSetChanged();

        if(Util.isInternetConnection(RouteNew.this))
            internetConnection=true;
        else
            internetConnection=false;
    }

    private void showDialogConnection(final Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText("No Internet");
        tvMsg.setText("Looks like you have no or very slow data connectivity.");
        btnClosePopup.setText("Cancel");
        btnsave.setText("Try again?");


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           if (logInType == null)
                                               new GetRoutes().execute();
                                           else
                                               new GetRoutesLogin().execute(logInType);

                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private void showDialogResponse(final Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText("D'oh!");
        tvMsg.setText("Sorry, something didn't quite work.");
        btnClosePopup.setText("Cancel");
        btnsave.setText("Try again?");


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           if (logInType == null)
                                               new GetRoutes().execute();
                                           else
                                               new GetRoutesLogin().execute(logInType);

                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private void setOfflineData(){

        tvTabRoute.setText("Routes");
        tvTabFav.setBackgroundColor(getResources().getColor(R.color.TabSelectedColor));
        tvTabFav.setText("Favourites");
        llAllRoute.setVisibility(View.GONE);
        llFavourite.setVisibility(View.GONE);
        llNoRoute.setVisibility(View.GONE);

        Cursor cursorAllRoute = dbOperation.getDataFromTableRoute();
        Cursor cursorFavRoute = dbOperation.getDataFromTableFavRoute();
        Cursor cursorRecentRoute = dbOperation.getDataFromTableRecentRoute();
        Cursor cursorPersonal = dbOperation.getDataFromTablePersonalInfo();

        setAllRoute(cursorAllRoute);
        setFavRoute(cursorFavRoute);
        setRecentRoute(cursorRecentRoute);
        setPersonalInfo(cursorPersonal);

        setFavAdapter();
        setRecentAdapter();
        setAllRouteAdapter();


    }

    private void setAllRoute(Cursor cursorAllRoute ){

        Constant.routeId=new String[cursorAllRoute.getCount()];
        Constant.routeName=new String[cursorAllRoute.getCount()];
        Constant.routeExpand=new String[cursorAllRoute.getCount()];

        int i=0;

        if (cursorAllRoute.getCount() > 0) {
            cursorAllRoute.moveToFirst();
            do {
               /* ChatPeopleBE people = addToChat(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5),cursor.getString(6), cursor.getString(7),cursor.getString(8));*/
                //ChatPeoples.add(people);
                Log.d("All routeID",cursorAllRoute.getString(0));
                Constant.routeId[i] =cursorAllRoute.getString(0);
                Constant.routeName[i] =cursorAllRoute.getString(1);
                Constant.routeExpand[i] = cursorAllRoute.getString(2);

                i++;


            } while (cursorAllRoute.moveToNext());
        }
        cursorAllRoute.close();


    }

    private void setFavRoute(Cursor cursorAllRoute ){

        Constant.favRouteID=new String[cursorAllRoute.getCount()];
        Constant.favRouteStartID=new String[cursorAllRoute.getCount()];
        Constant.favRouteEndID=new String[cursorAllRoute.getCount()];
        Constant.favRouteStartName=new String[cursorAllRoute.getCount()];
        Constant.favRouteEndName=new String[cursorAllRoute.getCount()];
        Constant.favRouteFavStatus=new String[cursorAllRoute.getCount()];
        Constant.favRouteStatus=new String[cursorAllRoute.getCount()];

        int i=0;

        if (cursorAllRoute.getCount() > 0) {
            cursorAllRoute.moveToFirst();
            do {
               /* ChatPeopleBE people = addToChat(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5),cursor.getString(6), cursor.getString(7),cursor.getString(8));*/
                //ChatPeoples.add(people);
                Log.d("fav routeID",cursorAllRoute.getString(0));
                Constant.favRouteID[i]=cursorAllRoute.getString(0);
                Constant.favRouteStartID[i]=cursorAllRoute.getString(1);
                Constant.favRouteEndID[i]=cursorAllRoute.getString(2);
                Constant.favRouteStartName[i]=cursorAllRoute.getString(3);
                Constant.favRouteEndName[i]=cursorAllRoute.getString(4);
                Constant.favRouteFavStatus[i]=cursorAllRoute.getString(5);
                Constant.favRouteStatus[i]=cursorAllRoute.getString(6);


                i++;


            } while (cursorAllRoute.moveToNext());
        }
        cursorAllRoute.close();

    }

    private void setRecentRoute(Cursor cursorAllRoute ){

        Constant.recentRouteID=new String[cursorAllRoute.getCount()];
        Constant.recentRouteStartID=new String[cursorAllRoute.getCount()];
        Constant.recentRouteEndID=new String[cursorAllRoute.getCount()];
        Constant.recentRouteStartName=new String[cursorAllRoute.getCount()];
        Constant.recentRouteEndName=new String[cursorAllRoute.getCount()];
        Constant.recentRouteFavStatus=new String[cursorAllRoute.getCount()];
        Constant.recentRouteStatus=new String[cursorAllRoute.getCount()];

        int i=0;

        if (cursorAllRoute.getCount() > 0) {
            cursorAllRoute.moveToFirst();
            do {
               /* ChatPeopleBE people = addToChat(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5),cursor.getString(6), cursor.getString(7),cursor.getString(8));*/
                //ChatPeoples.add(people);
                Log.d("recent routeID",cursorAllRoute.getString(0));
                Constant.recentRouteID[i]=cursorAllRoute.getString(0);
                Constant.recentRouteStartID[i]=cursorAllRoute.getString(1);
                Constant.recentRouteEndID[i]=cursorAllRoute.getString(2);
                Constant.recentRouteStartName[i]=cursorAllRoute.getString(3);
                Constant.recentRouteEndName[i]=cursorAllRoute.getString(4);
                Constant.recentRouteFavStatus[i]=cursorAllRoute.getString(5);
                Constant.recentRouteStatus[i]=cursorAllRoute.getString(6);


                i++;


            } while (cursorAllRoute.moveToNext());
        }
        cursorAllRoute.close();
    }

    private void setPersonalInfo(Cursor cursorAllRoute ){
        if (cursorAllRoute.getCount() > 0) {
            cursorAllRoute.moveToFirst();
            do {
               /* ChatPeopleBE people = addToChat(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5),cursor.getString(6), cursor.getString(7),cursor.getString(8));*/
                //ChatPeoples.add(people);

                Constant.NAME=cursorAllRoute.getString(0);
                Constant.currentLoopCredit=Integer.valueOf(cursorAllRoute.getString(1));
                Constant.LoopCredit=Constant.LoopCreditText+Constant.currentLoopCredit;





            } while (cursorAllRoute.moveToNext());
        }
        cursorAllRoute.close();

    }

    private void setFavAdapter(){
        llFavourite.setVisibility(View.VISIBLE);
        objFavRouteAdapter=new FavouriteAdapter(getApplicationContext(),tvSelectedRouteSource,tvSelectedRouteDestination,logInType,llBottomRoute,btnDone,RouteNew.this);
        LinearLayoutManager llm = new LinearLayoutManager(RouteNew.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvFav.setLayoutManager(llm);

        lvFav.setAdapter(objFavRouteAdapter);
    }

    private void setRecentAdapter(){
        llRecent.setVisibility(View.VISIBLE);
        objRecentRouteAdapter = new RouteAdapter(getApplicationContext(), tvSelectedRouteSource, tvSelectedRouteDestination,logInType,llBottomRoute,btnDone,RouteNew.this);
        lvRecent.setAdapter(objRecentRouteAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(RouteNew.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lvRecent.setLayoutManager(llm);
    }

    private void setAllRouteAdapter(){
        drawerAdapter.notifyDataSetChanged();

        prepareListData();

        objRoutesAdapter = new RoutesAdapter(RouteNew.this, listDataHeader, listDataChild,llBottomRoute);
        expListView.setAdapter(objRoutesAdapter);

        mGroupStates = new boolean[objRoutesAdapter.getGroupCount()];

        //tvTabFav.setText(Constant.Tab2Name);

    }

    public boolean isSoftKeyboardShown(InputMethodManager imm, View v) {

        IMMResult result = new IMMResult();
        int res;

        imm.showSoftInput(v, 0, result);

        // if keyboard doesn't change, handle the keypress
        res = result.getResult();
        if (res == InputMethodManager.RESULT_UNCHANGED_SHOWN ||
                res == InputMethodManager.RESULT_UNCHANGED_HIDDEN) {

            return true;
        }
        else
            return false;

    }
}
