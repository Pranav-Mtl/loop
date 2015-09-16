package com.aggregator.BL;


import com.aggregator.Constant.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by appslure on 9/8/2015.
 */
public class GettingProfileInformation {

    String userId,phone,pass,email;
    String finalValue;
    public String status;

    public String getData(String userId)
    {
        this.userId=userId;


        try {
            String result = fetRecord(userId);
            finalValue  = validate(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String userId)
    {
        String text = null;
        // http://appslure.in/unitedbysport.in/demo19/selecturl.php?email=balrampatel4077@gmail.com
        //http://unitedbysport.in/web/location.php?latitude=25.3176452&longitude=82.9739144
        //http://unitedbysport.in/demo19/index.php/webservice/location
        //http://unitedbysport.in/demo19/index.php/webservice/fetch_game_by_id?id=85
        //http://unitedbysport.in/demo19/index.php/webservice/fetch_create_game?email=balrampatel477@gmail.com
        //http://unitedbysport.in/demo19/index.php/webservice/login?email=ballu@ballu.com&password=ballu
        //?fullname=Balram%20patel&email=ballu@ballu.com&password=dfss&age=21&gender=male&location=delhi";
        //http://loop/webservices/loop1/profile.php?name=abcd&email=monika@appslure.in&mob_no=1234567890&pass=password
        //http://www.appslure.in/loop/webservices/loop1/profile.php?name=abcd&email=monika@appslure.in&mob_no=1234567890&pass=password
        //http://appslure.in/loop/webservices/loop1/edit_profile.php?user_id=9

        String url="user_id="+userId;

        try
        {
            URI uri = new URI("http", "www.appslure.in", "/loop/webservices/loop1/edit_profile.php",url, null);
            System.out.println("URI"+uri);
            String value=uri.toASCIIString();
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet=new HttpGet(value);
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);
            System.out.println("TEXT RETURN BY THE---hhhhhhhhhhuuuuo--------->"+text);
        }
        catch (Exception e)
        {
            System.out.println("in web services catch block");
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return text;
    }



    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);
            if (n>0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public String validate(String strValue)
    {
        System.out.println("ththththththpppppppppp------>"+strValue);

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//
            Constant.usrname=jsonObject1.get("name").toString();
            Constant.phoneNumber=jsonObject1.get("mobile").toString();
            Constant.emaiId=jsonObject1.get("email").toString();
            Constant.password=jsonObject1.get("password").toString();
            System.out.println("getting record---->"+Constant.usrname+"  "+Constant.phoneNumber);


        } catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
        return status;
    }






}