package com.aggregator.Constant;

import com.aggregator.loop.R;

/**
 * Created by Pranav Mittal on 8/19/2015.
 */
public class Constant {
    public static  String STREMAILADDREGEX="^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$"; //EMAIL REGEX

    ////////////////       WEB SERVICE URL      //////////////////////\

    public static String WS_HTTP="Http";
   // http://ec2-52-74-60-197.ap-southeast-1.compute.amazonaws.com/webservices
    public static String WS_DOMAIN_NAME="ec2-52-74-60-197.ap-southeast-1.compute.amazonaws.com";
    public static String WS_PATH="/webservices/";

    ////////////////       WEB SERVICES      ////////////////

    public static String WEBSERVICE_SIGNUP="signup.php";
    public static String WEBSERVICE_LOGIN="login.php";
    public static String WEBSERVICE_ROUTES="all_route.php";
    public static String WEBSERVICE_ROUTES_LOGIN="logged_in_routes.php";
    public static String WEBSERVICE_BOOKING_ROUTES="selected_route.php";
    public static String WEBSERVICE_BOOKING="booking.php";
    public static String WEBSERVICE_UPDATE_STATUS="updatestatus.php";
    public static String WEBSERVICE_RESEND_OTP="reset_otp.php";
    public static String WEBSERVICE_TICKET="ticket.php";
    public static String WEBSERVICE_Route_Search="search.php";

    public static String WEBSERVICE_PROFILE="profile.php";
    public static String WEBSERVICE_COMMENT="comment.php";
    public static String WEBSERVICE_FEEDBACK="feedback.php";
    public static String WEBSERVICE_TRIP="trip_history.php";
    public static String WEBSERVICE_FAV="fav.php";
    public static String WEBSERVICE_HELP="help.php";
    public static String WEBSERVICE_SHARE_EARN="user_referrals.php";
    public static String WEBSERVICE_PROMO_CODE="promo_code.php";
    public static String WEBSERVICE_EDIT_PROFILE="edit_profile.php";
    ///////////////    INTERNET CONNECTION MESSAGE     //////////////////////

    public static String ERR_INTERNET_CONNECTION_NOT_FOUND_MSG="Click YES to open settings or Click NO to go back";
    public static String ERR_INTERNET_CONNECTION_NOT_FOUND="No Internet Connection";

    public static String ERR_DROP_PICK_LOCATION="drop location must be after pick location";


    //////////////      WEB SERVICE RESULT      /////////////////

    public static String WS_RESULT_SUCCESS="success";
    public static String WS_RESULT_FAILURE="failure";
    public static String Tab2Name;


    //////////////////       SHARED PREFERENCE          ////////////////////////

    public static  String PREFS_NAME = "MyPrefsFile";   //SHARED PREFERENCE FILE NAME

    public static String SHARED_PREFERENCE_EMAIL="email";
    public static String SHARED_PREFERENCE_User_id="id";
    public static String SHARED_PREFERENCE_PHONE="phone";
    public static String SHARED_PREFERENCE_LOGIN_STATUS="status";

    ///   SAVE BOOKING DATA    ///

    public static String SHARED_PREFERENCE_BOOKING_SOURCE_id="source";
    public static String SHARED_PREFERENCE_BOOKING_DESTINATION_id="destination";
    public static String SHARED_PREFERENCE_BOOKING_ROUTE_ID="routeId";
    public static String SHARED_PREFERENCE_BOOKING_Run_ID="date";
    public static String SHARED_PREFERENCE_BOOKING_TIME="time";
    public static String SHARED_PREFERENCE_BOOKING_PRICE="price";




    //////////////        ALL ROUTES VARIABLE      /////////////////////


    public static String routeId[];
    public static String routeExpand[];
    public static String routeName[];

    //////////////        ALL Search ROUTES VARIABLE      /////////////////////


    public static String routeSearchId[];
    public static String routeSearchExpand[];
    public static String routeSearchName[];

    ////////////       SELECTED ROUTE VARIABLE     ////////////////////

    public static String jsonUP;
    public static String jsonDOWN;
    public static String jsonRoutePrice;

    public static String pointID[];
    public static String pointName[];
    public static String pointOrder[];
    public static Double pointDistance[];
    public static String pointTime[];
    public static String pointRun[];

    public static String pointRunArray[];

    public static Double routeFixedPrice;      // fixed route price
    public static Double routePerKmPrice;      //  route price per km


    public static String boookingRouteName[];
    public static String boookingRouteAbv[];
    public static String boookingRouteStation[];
    public static String boookingRouteTime[];
    public static String boookingRoutePrice[];



  //  public static String searchRootID[];

    ///////////      Favourites Route Variable     ////////////////

    public static String favRouteID[];
    public static String favRouteStartID[];
    public static String favRouteEndID[];
    public static String favRouteStartName[];
    public static String favRouteEndName[];

    ///////////      Recent Route Variable     ////////////////

    public static String recentRouteID[];
    public static String recentRouteStartID[];
    public static String recentRouteEndID[];
    public static String recentRouteStartName[];
    public static String recentRouteEndName[];

    //////////     Login User Details       //////////////////

    public static String name;
    public static String loopCredit;
    public static String paytmWallet;
    public static String paytmID;


    /////////     miscellaneous variables   ///////////////////

    public static String OTP;

    public static boolean favJson=false;
    public static boolean recentJson=false;


    public static String NAME="";
    public static String LoopCredit="";
    public static String amount;
    public static String LoopCreditText="Loop Credit : ₹ ";
    public static String PayTMWalet="";
    public static String PayTMWaletText="Paytm Credit : ₹ ";
    public static String LoopCreditUsed="";

    public static String usrname;
    public static String password;
    public static String phoneNumber;
    public static String emaiId;

    public static boolean swapRoute=false;

    public static int favSelectedItem=-1;
    public static int recSelectedItem=-1;

    public static String date;
    public static String pickPoint;
    public static String dropPoint;
    public static String rate;
    public static String feedback_comment;
    public static Float feedback_rating;


    public static String tripRouteID[];
    public static String tripStartID[];
    public static String tripStartName[];
    public static String tripEndID[];
    public static String tripEndName[];
    public static String tripPrice[];
    public static String tripDate[];
    public static String tripTime[];
    public static String tripFav[];
    public static String tripStatus[];
    public static String tripRunId[];



    public static String TITLES[] = {"Book a Ride","Trips","Promos","Invite & Earn","Rate Us","Notifications","Tutorial","Help",};

    public static int ICONS[] = {R.drawable.ic_side_trips,R.drawable.ic_side_bus, R.drawable.ic_side_promo,R.drawable.ic_side_invite_earn,R.drawable.ic_side_rate,R.drawable.ic_side_notification, R.drawable.ic_side_tutorial,R.drawable.ic_side_help};




}
