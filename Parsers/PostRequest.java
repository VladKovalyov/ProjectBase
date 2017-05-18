package com.company.Parsers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by admin on 25.04.17.
 */
public class PostRequest {

    private static final String USER_AGENT = "User-Agent";

    public PostRequest(){}

    public String sendPostRequest(String inputURL, String inputPostData) throws IOException {

        String url = ""+inputURL;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        //con.setRequestProperty("Content-Type","application/json");


        String postJsonData = "" + inputPostData;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postJsonData);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
       // System.out.println("nSending 'POST' request to URL : " + url);
      //  System.out.println("Post Data : " + postJsonData);
       // System.out.println("Response Code : " + responseCode);

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
           // e.printStackTrace();
            return "";
        }
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        in = null;
        wr = null;
        postJsonData = null;
        obj = null;
        con = null;

        //printing result from response
        // System.out.println(response.toString());
        return response.toString();
    }

}
