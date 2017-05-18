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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 24.04.17.
 */
public class ParserCSMONEY extends ClassParser implements InterfaceParserMethod{


    public ParserCSMONEY(){

    }
    public ParserCSMONEY(ParsersDATABASE ParsersBD){
        regestrationCallBackCSMONEY(ParsersBD);
        startMainThread();
    }


    interface callToParsersDATABASE{
        void CSMONEYNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetCSMONEYSkins();
        void parseJsonCSMONEY(boolean ParseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackCSMONEY( ParsersDATABASE callToParsersDB){
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
                        Thread.sleep(10000);

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

        Map<String,Integer> getNumberBot = null;
        try {
            getNumberBot = getAllNumberBotsCSMONEY();
        } catch (Exception e) {
            e.printStackTrace();
        }

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;// get answer from query
        org.json.JSONObject jsonObjectALL = null;
        Iterator<String> iterator = null;
        Map<String,SkinValue> mapSkins = new HashMap<>();

        int numberBot = -1;
        String name = null;
        float price = -1;
        SkinValue skin;
        JSONArray value;
        JSONObject objectSkin;
        String key = null;

        if (parser != null){

            try {
                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("https://cs.money/load_all_bots_inventory?hash="+new Date().getTime()));
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/CSMONEY.docx",jsonAnswer);
                callToParsersDB.parseJsonCSMONEY(true);
                System.out.println("parseJsonCSMONEY = true");
            } catch (ParseException e) {
                e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/CSMONEY.docx");
                callToParsersDB.parseJsonCSMONEY(false);
                System.out.println("parseJsonCSMONEY = false");
            }
        }
        if(jsonAnswer != null){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }

        if( jsonObjectALL != null){
            iterator = jsonObjectALL.keys();
        }

        if(iterator != null)
        while (iterator.hasNext()){
            key = iterator.next();
            value = (JSONArray) jsonObjectALL.get(key);

            for(int i = 0 ; i < value.length(); i ++){
                objectSkin = value.getJSONObject(i);
                //  System.out.println(objectSkin.get("m") + " "+ objectSkin.get("p") + " bot: "+objectSkin.get("b"));

                if (!objectSkin.isNull("m")){
                    name = objectSkin.get("m").toString();
                }
               if(!objectSkin.isNull("p")) {
                   price = Float.parseFloat(objectSkin.get("p").toString());
               }
                if(getNumberBot.containsKey(key)){
                    numberBot = getNumberBot.get(key);
                }

                if (name != null && mapSkins.containsKey(name) && price > 0 && numberBot != -1){
                    mapSkins.get(name).setPrice(price);
                    mapSkins.get(name).setListBotsCSMHasSkin(numberBot);
                } else if (name != null && !mapSkins.containsKey(name) && price > 0 && numberBot != -1){
                    skin = new SkinValue(name,price);
                    skin.setListBotsCSMHasSkin(numberBot);
                    mapSkins.put(name,skin);
                }

                name = null;
                price = -1;
                numberBot = -1;
                objectSkin = null;
                skin = null;

            }
            value =  null;
            key = null;
        }


        if(mapSkins.size() > 100){
            System.out.println("PARSE CSMONEY FINISH("+mapSkins.size()+")");
            if(callToParsersDB.isAllowGetCSMONEYSkins()){
                callToParsersDB.CSMONEYNewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                getNumberBot = null;
                parser = null;
                jsonAnswer = null;
                jsonObjectALL = null;
                skin = null;
                iterator = null;
                value = null;
                objectSkin = null;
                readWriteFile = new ReadWriteFile();
                getRequest = new GetRequest();
            }
        } else {
            System.out.println("PARSE CSMONEY CRASH:(");
            parse();
        }

    }


    private Map<String,Integer> getAllNumberBotsCSMONEY() throws JSONException, IOException {

        Map<String,Integer> getNumberBot = new ConcurrentHashMap<>();

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer = null;// get answer from query
        JSONObject jsonObjectALL = null;
        JSONArray jsonArray = null;
        try {
            jsonAnswer = ""+parser.parse(getRequest.sendGetQuery("https://cs.money/get_info?hash="+new Date().getTime()));
            readWriteFile.writeJsonToFile("src/JsonSitesSaveded/BOTSCSMONEY.docx",jsonAnswer);
        } catch (ParseException e) {
            jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/BOTSCSMONEY.docx");
           // e.printStackTrace();
        } catch (IOException e) {
           // e.printStackTrace();
            jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/BOTSCSMONEY.docx");

        }

        if(jsonAnswer != null){
            try {
                jsonObjectALL = new JSONObject(jsonAnswer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonObjectALL != null){
            try {
                jsonArray = jsonObjectALL.getJSONArray("list_bots");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(jsonArray != null)
        for (int i = 0; i < jsonArray.length(); i++) {

            org.json.JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            getNumberBot.put(jsonObject.get("name").toString(),i+1);
        }
        jsonAnswer = null;
        jsonArray = null;
        jsonObjectALL = null;
        parser = null;
        getRequest = new GetRequest();

        return getNumberBot;
    }


}
