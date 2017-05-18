package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public class ParserCSTRADEME extends ClassParser implements InterfaceParserMethod {

    public ParserCSTRADEME(ParsersDATABASE ParsersBD){
        regestrationCallBackCSTRADEME(ParsersBD);
        startMainThread();
    }

    interface callToParsersDATABASE{
        void CSTRADEMENewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetCSTRADEMESkins();
        void parseJsonCSTRADEME(boolean parseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackCSTRADEME( ParsersDATABASE callToParsersDB){
        this.callToParsersDB = (callToParsersDATABASE) callToParsersDB;
    }

    @Override
    public void  startMainThread()
    {
//        Thread threadParse = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (isLiveThread()) {
//                    try {
//
//
//                        Thread.sleep(100000);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });threadParse.start();

        try {
            setParseFlag(false);
            parse();
            System.gc();
            setParseFlag(true);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void parse() throws JSONException, IOException {

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;
        org.json.JSONObject jsonObjectALL = null;
        Iterator<String> iterator = null;
        Map<String,SkinValue> mapSkins = new HashMap<>();
        JSONObject item = null;
        SkinValue skin = null;
        String key = null;
        JSONArray arrayBots = null;
        String name = null;
        float price = -1;
        int numberBot = -1;


        if(parser != null){
            try {
                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("http://csgotrade.me/load_all_bots_inventory?hash="+new Date().getTime()));// get answer from query
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/CSTRADEME.docx",jsonAnswer);
                callToParsersDB.parseJsonCSTRADEME(true);
            } catch (ParseException e) {
               // e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/CSTRADEME.docx");
                setParseJsonReturned(false);
            }
        }

        if(jsonAnswer != null && jsonAnswer.length() > 1){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            iterator = jsonObjectALL.keys();
        }

        if(iterator != null)
        while (iterator.hasNext()){
            key = iterator.next();
            arrayBots = (JSONArray) jsonObjectALL.get(key);
            for(int i = 0; i < arrayBots.length();i++){

                item = arrayBots.getJSONObject(i);

                if(item.has("market_hash_name") && !item.isNull("market_hash_name")){
                    name = item.get("market_hash_name").toString();
                }
                if(item.has("steamid_bot") && !item.isNull("steamid_bot")){
                    numberBot = getRealNumberBot(item.get("steamid_bot").toString());
                }
               if(item.has("price") && !item.isNull("price")){
                   price = Float.parseFloat(item.get("price").toString());
               }


                if (mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                    mapSkins.get(name).setPrice(price);
                    mapSkins.get(name).setListBotsCSMHasSkin(numberBot);
                } else if (!mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                    skin = new SkinValue(name,price);
                    skin.setListBotsCSMHasSkin(numberBot);
                    mapSkins.put(name,skin);
                }

                name = null;
                price = -1;
                numberBot = -1;
                skin = null;
                item = null;

               // System.out.println(item.get("market_hash_name") + " " + item.get("price") + "botId =" +item.get("steamid_bot"));

            }
            arrayBots = null;
            key = null;

        }


        if(mapSkins.size() > 100){
            System.out.println("PARSE CSTRADEME FINISH("+mapSkins.size()+")");
            if(callToParsersDB.isAllowGetCSTRADEMESkins())
            {
                callToParsersDB.CSTRADEMENewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                skin = null;
                jsonAnswer = null;
                jsonObjectALL = null;
                iterator = null;
                item = null;
                parser = null;
            }
        }else{
            System.out.println("PARSE CSTRADEME CRASH:(");
            parse();
        }

    }

    @Override
    public int getRealNumberBot(String nameBot){
        switch (nameBot){
            case "76561198335446395" : return 1;
            case "76561198310085437" : return 2;
            case "76561198000923943" : return 3;
            case "76561198336564833" : return 4;
            case "76561198335114438" : return 5;
            case "76561198318160041" : return 6;
        }
        return -1;
    }

}
