package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public class ParserRAFFLETRADES extends ClassParser implements InterfaceParserMethod {

    public  ParserRAFFLETRADES(){}
    public ParserRAFFLETRADES(ParsersDATABASE ParsersBD){
        regestrationCallBackRAFFLETRADES(ParsersBD);
        startMainThread();
    }

    interface callToParsersDATABASE{
        void RAFFLETRADESNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetRAFFLETRADESSkins();
        void paseJsonRAFFLETRADES(boolean parseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackRAFFLETRADES( ParsersDATABASE callToParsersDB){
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
                        Thread.sleep(20000);

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
    public void parse() throws JSONException, IOException {


        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;
        org.json.JSONObject returnJSON = null;
        JSONArray responseFromJSON = null;
        org.json.JSONObject objectSkin = null;
        Map<String,SkinValue> mapSkins = new HashMap<>();

        SkinValue skin;
        String name = null;
        int conditions = 0;
        float price = -1;
        int numberBot = -1;
        boolean unstable = false;

        if(parser != null){
            try {
                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("https://api.skintrades.com/v1/inventory/"));// get answer from query
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/SKINTRADES.docx",jsonAnswer);
                callToParsersDB.paseJsonRAFFLETRADES(true);
                System.out.println("paseJsonSKINTRADES = true");
            } catch (org.json.simple.parser.ParseException e) {
              //  e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/SKINTRADES.docx");
                callToParsersDB.paseJsonRAFFLETRADES(false);
                System.out.println("paseJsonSKINTRADES = false");
            }
        }
        if(jsonAnswer != null){
            returnJSON = new org.json.JSONObject(jsonAnswer);
        }
        if(returnJSON != null){
            responseFromJSON = (JSONArray)returnJSON.getJSONArray("response");
        }

        if(responseFromJSON != null)
        for (int i = 0 ; i < responseFromJSON.length(); i++){
            objectSkin = (JSONObject) responseFromJSON.get(i);// get array skins from bot

                if(objectSkin.has("market_name") && !objectSkin.isNull("market_name")  ){
                    name = objectSkin.getString("market_name");
                    conditions++;
                }
                if(objectSkin.has("price") && !objectSkin.isNull("price")){
                    price = (float) objectSkin.getDouble("price");
                    conditions++;
                }
                if(objectSkin.has("bot_id") && !objectSkin.isNull("bot_id")){
                    numberBot = objectSkin.getInt("bot_id");
                    conditions++;
                }
                if(objectSkin.has("blockedItem") && !objectSkin.isNull("blockedItem")){
                    unstable = objectSkin.getBoolean("blockedItem");
                    conditions++;
                 }

                if (mapSkins.containsKey(name) && conditions == 4) {
                    mapSkins.get(name).setPrice(price);
                    mapSkins.get(name).setListBotsSKINTRADESHasSkin(numberBot);
                    mapSkins.get(name).setSKINTRADES_UNSTABLE(unstable);
                } else if(!mapSkins.containsKey(name) && conditions == 4){
                    skin = new SkinValue(name,price);
                    skin.setListBotsSKINTRADESHasSkin(numberBot);
                    skin.setSKINTRADES_UNSTABLE(unstable);
                    mapSkins.put(name, skin);
                }

            conditions = 0;
            objectSkin = null;
            skin = null;
        }


        if (mapSkins.size() > 100){
            System.out.println("PARSE SKINTRADES FINISH("+mapSkins.size()+")");
            if(callToParsersDB.isAllowGetRAFFLETRADESSkins()){
                callToParsersDB.RAFFLETRADESNewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                jsonAnswer = null;
                responseFromJSON = null;
                returnJSON = null;
                parser = null;
                readWriteFile = new ReadWriteFile();
                getRequest = new GetRequest();
            }

        }else {
            System.out.println("PARSE RAFFLETRADES CRASH:(");
            parse();
        }


    }

    private int getRealNumberBotRAFFLETRADES(String nameBot){

        switch (nameBot){
            case "76561198335641200" : return 1;
            case "76561198258939509" : return 2;
            case "76561198335548380" : return 3;
            case "76561198335321857" : return 4;
            case "76561198334957143" : return 5;
        }
        return -1;
    }

    public static void main(String[] args) throws IOException, JSONException {
        new ParserRAFFLETRADES().parse();
    }
}
