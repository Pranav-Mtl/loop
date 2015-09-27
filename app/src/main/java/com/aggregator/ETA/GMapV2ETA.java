package com.aggregator.ETA;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Pranav Mittal on 9/27/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class GMapV2ETA {

    public Document getDocument(LatLng start, LatLng end, String mode) {
        String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=driving";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDurationText (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");

        String finalDuration="";

        if(nl1.getLength()>0){
            for(int i=0;i<nl1.getLength();i++){
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                finalDuration=node2.getTextContent();

            }
        }
        return finalDuration;
    }

    public String getDistanceText (Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        String finalDistance="";

        if(nl1.getLength()>0){
            for(int i=0;i<nl1.getLength();i++){
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                finalDistance=node2.getTextContent();

            }
        }
        return finalDistance;
    }


    private int getNodeIndex(NodeList nl, String nodename) {
        for(int i = 0 ; i < nl.getLength() ; i++) {
            if(nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

}
