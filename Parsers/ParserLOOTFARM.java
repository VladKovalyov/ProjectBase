package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 24.04.17.
 */
public class ParserLOOTFARM extends ClassParser implements InterfaceParserMethod{

    volatile private int countUpdates = 6;

    interface callToParsersDATABASE{
        void LootFarmNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetLOOTFARMSkins();
        void parseJsonLOOTFARM(boolean parseJson);
    }

    public ParserLOOTFARM(ParsersDATABASE ParsersBD){
        regestrationCallBackLOOTFARM(ParsersBD);
        startMainThread();
    }

    public ParserLOOTFARM(){}

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackLOOTFARM( ParsersDATABASE callToParsersDB){
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

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        Map<String,SkinValue> mapSkins = new HashMap<>();

        getAllPricesLOOTFARM(mapSkins);
        getUnstableLOOTFARM(mapSkins);

        String jsonAnswer = null;
        org.json.JSONObject jsonObjectALL = null;
        org.json.JSONObject jsonObjectResult = null;
        Iterator<String> iterator = null;

        SkinValue newSkin = null;
        String name = null;
        String nameST = "StatTrak™ ";
        float price = -1;



        try {
            jsonAnswer  =  ""+parser.parse(getRequest.sendGetQuery("https://loot.farm/botsInventory.json"));// get answer from query
            //readWriteFile.writeJsonToFile("src/JsonSitesSaveded/LOOTFARM.docx",jsonAnswer);
            System.out.println("ReadJsonLOOTFARM");
            callToParsersDB.parseJsonLOOTFARM(true);
        } catch (Exception e) {
           // e.printStackTrace();
            jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/LOOTFARM.docx");
            System.out.println("readFileLOOTFARMJson");
            callToParsersDB.parseJsonLOOTFARM(false);
        }


        if(jsonAnswer != null){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            jsonObjectResult = jsonObjectALL.getJSONObject("result");
        }
        if(jsonObjectResult != null){
            iterator = jsonObjectResult.keys();
        }

        if(iterator != null)
        while (iterator.hasNext()) {
            String key = iterator.next();
            JSONObject value = (JSONObject) jsonObjectResult.get(key);
            JSONObject typeStructure = null;
            if(value.get("t") instanceof JSONObject){
                typeStructure = (JSONObject) value.get("t");
            }
            String type = "";

            if(!typeStructure.isNull("t")) {
                type = typeStructure.get("t").toString();
            }

            if(!value.isNull("n")) {
                 name = value.get("n").toString();
            }


            if (!value.isNull("pst")){
                nameST += name;
            }

            if(type.equals("Kn")){
                name ="★ "+name;
                nameST ="★ "+nameST;
            }

            if (!value.isNull("e")) {
                switch (value.get("e").toString()) {
                    case "FN":name += " (Factory New)";nameST += " (Factory New)";break;
                    case "MW":name += " (Minimal Wear)";nameST += " (Minimal Wear)";break;
                    case "FT":name += " (Field-Tested)";nameST += " (Field-Tested)";break;
                    case "WW":name += " (Well-Worn)";nameST += " (Well-Worn)";break;
                    case "BS":name += " (Battle-Scarred)";nameST += " (Battle-Scarred)";break;

                }

            }

            if (name != null && !value.isNull("p")){
                price = Float.parseFloat(value.get("p").toString()) / 100;
                if (price > 0) {
                    newSkin = new SkinValue(name, price);
                    newSkin.setLOOTFARM_HAS(true);
                    if (!mapSkins.containsKey(name))
                        mapSkins.put(name, newSkin);
                    else
                        mapSkins.replace(name, newSkin);
                }

            }
            if (nameST != null && !value.isNull("pst")){
                price = Float.parseFloat(value.get("pst").toString()) / 100;
                if(price > 0) {
                    newSkin = new SkinValue(nameST, price);
                    newSkin.setLOOTFARM_HAS(true);
                    if (!mapSkins.containsKey(nameST))
                        mapSkins.put(nameST, newSkin);
                    else
                        mapSkins.replace(nameST, newSkin);

                }
            }
            name = null;
            nameST = null;
            nameST = "StatTrak™ ";
            newSkin = null;
            price = -1;
        }

        if (mapSkins.size() > 100){
            System.out.println("PARSE LOOTFARM FINISH("+mapSkins.size()+")");
            if (callToParsersDB.isAllowGetLOOTFARMSkins()){
                callToParsersDB.LootFarmNewSkins(mapSkins);
                countUpdates--;
                if(countUpdates == 0)
                    countUpdates = 6;
                 }
            mapSkins.clear();
            mapSkins = null;
            newSkin = null;
            jsonAnswer = null;
            jsonObjectALL = null;
            jsonObjectResult = null;
            iterator = null;
            parser = null;
            readWriteFile = new ReadWriteFile();
            getRequest = new GetRequest();
        } else {
            System.out.println("PARSE LOOTFARM CRASH:(");
            parse();
        }



    }

    private void getUnstableLOOTFARM(Map<String,SkinValue> mapSkins) throws IOException, JSONException {

        if (countUpdates == 6) {

            String listSkins = "" + getRequest.sendGetQuery("https://loot.farm/unstable.html");

            org.jsoup.nodes.Document doc1 = Jsoup.parse(listSkins);
            org.jsoup.select.Elements link = doc1.getElementsByTag("BODY");


            String string = link.get(0).toString();
            String[] str = string.split("\\n <br>");
            int countSkins = 0;
            SkinValue skinValue;

            char[] chars = str[0].toCharArray();
            String string1 = "";

            for (int i = 8; i < chars.length; i++) {
                string1 += "" + chars[i];

            }
            str[0] = string1;

            for (int i = 0; i < str.length; i++) {

                if (!mapSkins.containsKey(str[i])) {
                    skinValue = new SkinValue(str[i]);
                    skinValue.setLOOTFARM_UNSTABLE(true);
                    mapSkins.put(str[i],skinValue);
                    countSkins++;
                } else {
                    mapSkins.get(str[i]).setLOOTFARM_UNSTABLE(true);
                }

            }
            str = null;
            string = null;
            string1 = null;
            doc1 = null;
            listSkins = null;
            link = null;
            skinValue = null;
            getRequest = new GetRequest();
            System.out.println("count unstable = " + countSkins);
        }
    }

    private void getAllPricesLOOTFARM(Map<String,SkinValue> mapSkins) throws IOException, JSONException {

        if(countUpdates == 6) {

            mapSkins.clear();

            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            String jsonAnswer = null;
            org.json.JSONArray allSkins = null;
            org.json.JSONObject skin;
            SkinValue skinValue = null;
            String name = null;
            float price = -1;
            int countHaveSkins = -1;
            int maxCountSkins = -1;
            int countSkins = 0;

            if (parser != null) {
                try {
                    jsonAnswer = "" + parser.parse(getRequest.sendGetQuery("https://loot.farm/fullprice.json"));
                    readWriteFile.writeJsonToFile("src/JsonSitesSaveded/LOOTFARMDB.docx",jsonAnswer);
                    System.out.println("download LOOTFARM DB = true");
                } catch (ParseException e) {
                    // e.printStackTrace();
                    jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/LOOTFARMDB.docx");
                    System.out.println("downloadLOOTFARM DB = false, read file ");
                }
            }
            if (jsonAnswer != null) {
                allSkins = new JSONArray(jsonAnswer);
            }

            if (allSkins != null)
                for (int i = 0; i < allSkins.length(); i++) {

                    skin = (JSONObject) allSkins.get(i);

                    if (!skin.isNull("name") && skin.has("name")) {
                        name = skin.get("name").toString();
                        skinValue = new SkinValue(name);
                    }

                    if (!skin.isNull("price") && skin.has("price")) {
                        price = Float.parseFloat(skin.get("price").toString()) / 100;
                        skinValue.setPrice(price);
                    }
                    if (!skin.isNull("have") && skin.has("have")) {
                        countHaveSkins = Integer.parseInt(skin.get("have").toString());
                        skinValue.setCountHaveSkinLootFarm(countHaveSkins);
                    }
                    if (!skin.isNull("max") && skin.has("max")) {
                        maxCountSkins = Integer.parseInt(skin.get("max").toString());
                        skinValue.setMaxCountSkin(maxCountSkins);
                    }

                    //  System.out.println("name:" + skin.get("name") +"\t price:"+ skin.get("price") +"\t have:"+skin.get("have") +"\t max:"+skin.get("max"));

                    if (!mapSkins.containsKey(name) && name != null && price > 0 && countHaveSkins != -1 && maxCountSkins != -1) {

                        skinValue.setLOOTFARM_HAS(false);
                        mapSkins.put(name, skinValue);

                    }

                    name = null;
                    price = -1;
                    countHaveSkins = -1;
                    maxCountSkins = -1;
                    skinValue = null;
                    countSkins++;
                }

                jsonAnswer = null;
                parser = null;
                skin = null;
                skinValue = null;
                allSkins = null;
                getRequest = new GetRequest();
                readWriteFile = new ReadWriteFile();

            System.out.println("Loootfarm Stable Price = " + countSkins);

        } else {
            for(String name : mapSkins.keySet()){
                mapSkins.get(name).setLOOTFARM_HAS(false);
            }
        }
    }

    public static void main(String[] args) {
        new ParserLOOTFARM().startMainThread();
    }
}
