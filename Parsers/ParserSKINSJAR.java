package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public class ParserSKINSJAR extends ClassParser implements InterfaceParserMethod {

    public ParserSKINSJAR (){}
    public ParserSKINSJAR(ParsersDATABASE ParsersBD){
        regestrationCallBackSKINSJAR(ParsersBD);
        startMainThread();
    }

    interface callToParsersDATABASE{
        void SKINSJARNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetSKINSJARSkins();
        void parseJsonSKINSJAR(boolean parseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackSKINSJAR( ParsersDATABASE callToParsersDB){
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
                        Thread.sleep(22000);

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
        String jsonAnswer = null;
        org.json.JSONObject jsonObjectALL = null;
        Iterator<String> iterator = null;
        org.json.JSONObject skin;
        Map<String,SkinValue> mapSkins = new HashMap<>();


        SkinValue skins;
        String key;
        String name = null;
        float price = -1;
        int numberBot = -1;


        if (parser != null) {
            try {
                jsonAnswer = "" + parser.parse(getRequest.sendGetQuery("https://skinsjar.com/loadBots?refresh=1"));// get answer from query
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/SKINSJAR.docx",jsonAnswer);
                callToParsersDB.parseJsonSKINSJAR(true);
                System.out.println("parseJsonSKINSJAR = true");
            } catch (ParseException e) {
                //  e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/SKINSJAR.docx");
                callToParsersDB.parseJsonSKINSJAR(false);
                System.out.println("parseJsonSKINSJAR = false");
            }
        }
        if (jsonAnswer != null) {
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if (jsonObjectALL != null) {
            iterator = jsonObjectALL.keys();
        }

        if (iterator != null)
            while (iterator.hasNext()) {
                key = iterator.next();
                skin = (org.json.JSONObject) jsonObjectALL.get(key);

                if (!skin.isNull("name")) {
                    name = skin.get("name").toString();
                }
                if (!skin.isNull("bot")) {
                    numberBot = Integer.parseInt(skin.get("bot").toString());
                }
                if (!skin.isNull("priceSell")) {
                    price = (float) Double.parseDouble(String.format("%.4s%n", ((Float.parseFloat(skin.get("priceSell").toString())))));
                }

                if (mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1) {
                    mapSkins.get(name).setPrice(price);
                    mapSkins.get(name).setListBotsCSMHasSkin(numberBot);

                } else if(!mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                    skins = new SkinValue(name, price);
                    skins.setListBotsSkinJARWHasSkin(numberBot);
                    mapSkins.put(name, skins);

                }
            name = null;
            price = -1;
            numberBot = -1;
            skin = null;
            skins = null;
            key = null;

            }

        if (mapSkins.size() > 100) {

            System.out.println("PARSE SKINSJAR FINISH(" + mapSkins.size() + ")");
            if(callToParsersDB.isAllowGetSKINSJARSkins()){
                callToParsersDB.SKINSJARNewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                jsonAnswer = null;
                jsonObjectALL = null;
                skin = null;
                skins = null;
                parser = null;
                getRequest = new GetRequest();
                readWriteFile = new ReadWriteFile();
            }

        } else {
            System.out.println("PARSE SKINSJAR CRASH:(");
            parse();
        }
    }


}
