package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 26.04.17.
 */
public class ParserOPSKINS extends ClassParser implements InterfaceParserMethod {


    interface callToParsersDATABASE{
        void OPSKINSNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetOPSKINSSkins();
        void parseJSONOPSKINS(boolean parseJson);
    }

    public ParserOPSKINS(ParsersDATABASE ParsersBD){
        regestrationCallBackOPSKINS(ParsersBD);
        startMainThread();
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackOPSKINS( ParsersDATABASE callToParsersDB){
        this.callToParsersDB = callToParsersDB;
    }

    @Override
    public void parse() throws JSONException, IOException {

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;
        org.json.JSONObject jsonObjectALL = null;
        org.json.JSONObject Skins = null;
        Map<String,SkinValue> mapSkins = new HashMap<>();
        Iterator<String> iterator = null;
        SkinValue skinValue = null;
        org.json.JSONObject itemsMap = null;
        String name = null;
        float price = -1;

        if(parser != null){
            try {
                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("https://api.opskins.com/IPricing/GetAllLowestListPrices/v1/?appid=730"));// get answer from query
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/OPSKINS.docx",jsonAnswer);
                callToParsersDB.parseJSONOPSKINS(true);
            } catch (ParseException e) {
               // e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/OPSKINS.docx");
                callToParsersDB.parseJSONOPSKINS(false);
            }

        }
        if(jsonAnswer != null){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            Skins = (org.json.JSONObject) jsonObjectALL.get("response");
        }
        if(Skins != null){
            iterator = Skins.keys();
        }

        if(iterator != null)
        while (iterator.hasNext()){
            name = iterator.next();
            itemsMap = (org.json.JSONObject)Skins.get(name);
            price = Float.parseFloat(itemsMap.get("price").toString())/100;

            if(!mapSkins.containsKey(name) && name != null && price > 0 ){
                skinValue = new SkinValue(name,price);
                mapSkins.put(name,skinValue);
            } else if (mapSkins.containsKey(name) && name != null && price > 0){
                mapSkins.get(name).setPrice(price);
            }

            name = null;
            skinValue = null;
            price = -1;
            itemsMap = null;

        }

        if(mapSkins.size() > 100){
            System.out.println("PARSE OPSKINS FINISH("+mapSkins.size()+")");

           if ((callToParsersDB.isAllowGetOPSKINSSkins())){
               callToParsersDB.OPSKINSNewSkins(mapSkins);
               mapSkins.clear();
               mapSkins = null;
               jsonAnswer = null;
               jsonObjectALL = null;
               skinValue = null;
               Skins = null;
               iterator = null;
               parser = null;
               readWriteFile = new ReadWriteFile();
               getRequest = new GetRequest();
           }
        } else {
            System.out.println("PARSE OPSKINS ERROR");
            parse();
        }



    }

    @Override
    public void startMainThread() {
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

}
