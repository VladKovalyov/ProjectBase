package com.company.Parsers;

import com.company.SkinValue;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 25.04.17.
 */
public class ParserCSTM extends ClassParser implements InterfaceParserMethod{

    volatile private Map<String,SkinValue> mapSkinsBUYNOW = new ConcurrentHashMap<>();
    private boolean buyNowActive = false;

    interface callToParsersDATABASE{
        void CSTMNewSkins(Map<String,SkinValue> mapInput);
        void BUY_NOWNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetCSTMSkins();
        boolean isAllowGetBUYNOWSkins();
    }

    public ParserCSTM(){}
    public ParserCSTM(ParsersDATABASE ParsersBD){
        regestrationCallBackCSTM(ParsersBD);
        startMainThread();
    }

    private ParsersDATABASE callToParsersDB;

    public void regestrationCallBackCSTM(ParsersDATABASE callToParsersDB){
        this.callToParsersDB = callToParsersDB;
    }

    @Override
    public void  startMainThread()
    {
        Thread threadParse = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isLiveThread()) {
                    try {

                        setParseFlag(false);
                        parse();
                        System.gc();
                        setParseFlag(true);
                        Thread.sleep(80000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });threadParse.start();
    }

    @Override
    public void parse() {

        Map<String ,SkinValue> mapSkins = new HashMap<>();
        boolean downloadFile = false;
        try {
            downloadDBCSGOTM();
            downloadFile = true;
        } catch (Exception e) {
           // e.printStackTrace();
            downloadFile = false;
        }

        String csvFile = "src/JsonSitesSaveded/itemsCSGOTM.csv";
        BufferedReader in = null;
        String line = "";
        String cvsSplitBy = ";";


        int count = 0;
        float price = 0;
        float priceNOW = 0;
        String marketPath = "";
        String name;
        SkinValue newSkin = null;
        SkinValue oldSkins = null;

        if(downloadFile) {
            File fileDir = new File(csvFile);

            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));

                while ((line = in.readLine()) != null) {
                    // use comma as separator
                    String[] skinInfo = line.split(cvsSplitBy);
                    if (count > 0) {
                        name = skinInfo[11].replace("\"", "");
                        marketPath = skinInfo[0].toString() + "_" + skinInfo[1].toString();

                        if (!name.equals("")) {
                            price = (float) Double.parseDouble(String.format("%.4s%n", (((Float.parseFloat(skinInfo[2]) / 100)))));
                        }

                        if (!mapSkins.containsKey(name) && price > 0) {
                            marketPath = skinInfo[0].toString() + "_" + skinInfo[1].toString();
                            newSkin = new SkinValue(name, price);
                            newSkin.setMarketPath(marketPath);
                            mapSkins.put(name, newSkin);

                        } else if (mapSkins.containsKey(name) && price > 0) {

                            oldSkins = mapSkins.get(name);
                            //priceNOW = (float)Double.parseDouble(String.format("%.4s%n", ( ((Float.parseFloat(skinInfo[2])/100)))));
                            if (oldSkins.getPrice() > price) {
                                // price = priceNOW;
                                marketPath = skinInfo[0].toString() + "_" + skinInfo[1].toString();
                                oldSkins.setPrice(price);
                                oldSkins.setMarketPath(marketPath);
                            }
                            mapSkins.replace(name, oldSkins);
                        }

                    }
                    count++;
                    price = -1;
                    newSkin = null;
                    oldSkins = null;
                    marketPath = null;
                    name = null;

                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("READ CSV FILE FINISH ("+mapSkins.size()+")");

//            if(callToParsersDB.isAllowGetCSTMSkins())
//                callToParsersDB.CSTMNewSkins(mapSkins);

                makeListForMassInfo(mapSkins);

            mapSkins.clear();
            mapSkins = null;
            newSkin = null;
            in = null;
            getRequest = new GetRequest();


        }
        else {
            System.out.println("download file error");
        }
    }

    private void makeListForMassInfo(Map<String,SkinValue> mapSkins){

        ArrayList<String> arrayString = new ArrayList<>();
        String I99PathsSkins = null;
        SkinValue skin = null;

        int sizeMap = mapSkins.size();
        int count = 0;

        System.out.println("MAKE LIST FOR MASS INFO" + sizeMap);

        for (String key:mapSkins.keySet()) {

            skin = mapSkins.get(key);
            count++;

            if(count != 99 && !(count == sizeMap)) {
                I99PathsSkins += skin.getMarketPath() + ",";

            }
            else if (count == 99 || count == (sizeMap)){
                I99PathsSkins += skin.getMarketPath();
                arrayString.add(I99PathsSkins);
                I99PathsSkins = null;
                sizeMap -= count;
                count = 0;

            }
        }
        System.out.println("MAKE LIST FOR MASS INFO FINISH("+arrayString.size()+")");

//        mapSkinsBUYNOW.clear();
        threadBuyNow(arrayString);
        arrayString = null;

    }

    private void threadBuyNow(ArrayList<String> arrayStrings){
        buyNowActive = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i <= arrayStrings.size()-1){
                    System.out.println("threadBuyNow active all ="+arrayStrings.size() + " get =" +i);
                    try {
                        sendMassInfoCGGOTM(arrayStrings.get(i));
                        i++;
                        Thread.sleep(300);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                if(callToParsersDB.isAllowGetBUYNOWSkins()){
//                    callToParsersDB.BUY_NOWNewSkins(mapSkinsBUYNOW);
//
//                }
                           System.out.println("threadBuyNow disable");
                buyNowActive = false;

            }
        });thread.start();
    }

    private void sendMassInfoCGGOTM(String arrayString) throws IOException { // MassInfo

        Map<String,SkinValue> mapSkinsBUYNOW = new HashMap<>();

        URL url = new URL("https://market.csgo.com/api/MassInfo/0/2/2/1/?key=7lVtxiN737a4gPR5e902SlWbV32EDkE");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        writer.write("list="+arrayString);
        writer.flush();
        String line;
        String json = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = reader.readLine()) != null) {
            json = "" + line;
        }
        writer.close();
        reader.close();


        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        try {

            Object obj = parser.parse(json);
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
            org.json.simple.JSONArray results = (org.json.simple.JSONArray) jsonObject.get("results");

            org.json.simple.JSONObject buy_offers;
            org.json.simple.JSONObject best_offer;
            String name = null;
            org.json.simple.JSONObject info = null;
            SkinValue skin = null;
            float price = -1;
            int countOffers = 0;


            for (int i = 0 ;i < results.size(); i++){
                buy_offers =  (org.json.simple.JSONObject)results.get(i);
                if(buy_offers.get("info") != null){
                    info = (org.json.simple.JSONObject)buy_offers.get("info");
                }else {
                    continue;
                }
                    name = (String) info.get("market_hash_name");

                // System.out.println("name_EN = " + name_EN);

                if(!(buy_offers.get("buy_offers") instanceof Boolean)){
                    //System.out.println("result = boolean");

                    best_offer = (org.json.simple.JSONObject) buy_offers.get("buy_offers");
                    price = Float.parseFloat(best_offer.get("best_offer").toString());

                    if(name !=null && !mapSkinsBUYNOW.containsKey(name) && price > 0){

                        price = Float.parseFloat(String.format("%.4s%n", ((price / 100))*0.93));
                        skin = new SkinValue(name,price);
                        mapSkinsBUYNOW.put(name,skin);

                          countOffers++;
                      }

                }

                name = null;
                price = -1;


            }
             // System.out.println("Count offers =" + countOffers);
//            callToParsersDB.BUY_NOWNewSkins(mapSkinsBUYNOW);
            reader = null;
            writer = null;
            arrayString = null;
            conn = null;
            url = null;
            results = null;
            json = null;
            jsonObject = null;
            buy_offers = null;
            best_offer = null;
            info = null;
            skin = null;
            getRequest = new GetRequest();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void downloadDBCSGOTM() throws Exception { //download DB
        while (buyNowActive){
            Thread.sleep(1000);
            System.out.println("wait BUYNOW");
        }
        System.out.println("download start");
        URL website = new URL("https://market.csgo.com/itemdb/" + getNameDB());
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("src/JsonSitesSaveded/itemsCSGOTM.csv");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        System.out.println("download finish");

        website = null;
        rbc.close();
        fos.close();
        rbc = null;
        fos = null;

    }

    private String getNameDB() throws Exception {

        String json = getRequest.sendGetQuery("https://market.csgo.com/itemdb/current_730.json");
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();

        Object obj = parser.parse(json);
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        String returnName = (String) jsonObject.get("db");
        System.out.println("name database = " + jsonObject.get("db"));

        return returnName;
    }

}
