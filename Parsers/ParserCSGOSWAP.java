package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public class ParserCSGOSWAP extends ClassParser implements InterfaceParserMethod{


    public ParserCSGOSWAP(){}
    public ParserCSGOSWAP(ParsersDATABASE ParsersBD){
        regestrationCallBackCSGOSWAP(ParsersBD);
        startMainThread();
    }

    interface callToParsersDATABASE{
        void CSGOSWAPNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetCSGOSWAPSkins();
        void parseJsonCSGOSWAP(boolean ParseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackCSGOSWAP( ParsersDATABASE callToParsersDB){
        this.callToParsersDB = (callToParsersDATABASE) callToParsersDB;
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
                        Thread.sleep(25000);

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
    public void parse() throws IOException, JSONException {

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;
        org.json.JSONObject jsonObjectALL = null;
        org.json.JSONArray jsonArray = null;
        JSONObject item = null;
        Map<String,SkinValue> mapSkins = new HashMap<>();

        String name = null;
        float price = -1;
        SkinValue skin = null;
        int numberBot = -1;
        int countSkinsONE = 0;

        if(parser != null){
            try {

                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("https://csgoswap.com/api/inventory/bot"));
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/CSGOSWAP.docx",jsonAnswer);
                callToParsersDB.parseJsonCSGOSWAP(true);
                System.out.println("parseJsonCSGOSWAP = true");

            } catch (ParseException e) {

               jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/CSGOSWAP.docx");
                callToParsersDB.parseJsonCSGOSWAP(false);
                System.out.println("parseJsonCSGOSWAP = false");

            }
        }
        if(jsonAnswer != null){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            jsonArray = jsonObjectALL.getJSONArray("items");
        }

        if(jsonArray != null)
        for (int i = 0; i < jsonArray.length(); i++){
            item = jsonArray.getJSONObject(i);
            // System.out.println(item.get("marketName") + " "+item.get("price") +"botID= "+item.get("botId"));

            if(!item.isNull("marketName") && item.has("marketName")) {
                name = item.get("marketName").toString();
            }
            if(!item.isNull("price") && item.has("price")) {
                price = Float.parseFloat(item.get("price").toString());
            }
            if(!item.isNull("botId") && item.has("botId")){
                numberBot = Integer.parseInt(item.get("botId").toString());
            }


            if (mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1) {
                mapSkins.get(name).setPrice(price);
                mapSkins.get(name).setListBotsCSGSWHasSkin(numberBot);
                countSkinsONE = mapSkins.get(name).getCountItemsCSGSW();
                countSkinsONE++;
                mapSkins.get(name).setCountItemsCSGSW( countSkinsONE );
            } else if(!mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                skin = new SkinValue(name, price);
                skin.setListBotsCSGSWHasSkin(numberBot);
                skin.setCountItemsCSGSW( 1 );
                mapSkins.put(name, skin);
            }


            name = null;
            numberBot = -1;
            skin = null;
            item = null;
            price = -1;
            countSkinsONE = 0;
        }



        if(mapSkins.size() > 100){

            System.out.println("PARSE CSGOSWAP FINISH("+mapSkins.size()+")");
            if(callToParsersDB.isAllowGetCSGOSWAPSkins()){
                callToParsersDB.CSGOSWAPNewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                parser = null;
                item = null;
                jsonAnswer = null;
                jsonArray = null;
                jsonObjectALL = null;
                readWriteFile = new ReadWriteFile();
                getRequest = new GetRequest();
            }
        }else{
            System.out.println("PARSE CSGOSWAP CRASH:(");
            parse();
        }

    }

}
