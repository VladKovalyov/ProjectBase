package com.company.Parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 24.04.17.
 */
public class GetRequest {

    public GetRequest() {

    }

    public String sendGetQuery(String urlInput) throws IOException {

        String url = urlInput;
        String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);


        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            return "";
           // e.printStackTrace();
        }

        String inputLine;
        String returnLine = "";
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            //System.out.println(inputLine);
            returnLine += inputLine;
        }
        in.close();

        //print result
        //System.out.println(response.toString());

        in = null;
        url = null;
        obj = null;
        con = null;
        inputLine = null;
        return returnLine;
    }

    public static void main(String[] args) throws IOException {
        new GetRequest().sendGetQuery("https://api.skintrades.com/v1/inventory/");
    }
}
