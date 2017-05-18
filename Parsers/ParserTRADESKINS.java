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
public class ParserTRADESKINS extends ClassParser implements InterfaceParserMethod {

    public ParserTRADESKINS(){}
    public ParserTRADESKINS(ParsersDATABASE ParsersBD){
        regestrationCallBackTRADESKINS(ParsersBD);
        startMainThread();
    }

    interface callToParsersDATABASE{
        void TRADESKINSNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetTRADESKINSSkins();
        void parseJsonTRADESKINS(boolean parseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackTRADESKINS( ParsersDATABASE callToParsersDB){
        this.callToParsersDB = (callToParsersDATABASE) callToParsersDB;
    }

    @Override
    public void parse() throws JSONException, IOException {

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;
        org.json.JSONObject jsonObjectALL = null;
        Iterator<String> iterator = null;
        Map<String,Integer> getNumberBotCSTRADESKINS = null;
        Map<String,SkinValue> mapSkins = new HashMap<>();


        JSONObject skin ;
        JSONArray skinsBOT ;
        String key = "";
        SkinValue skins;
        int countSkins = 0;
        String name = "";
        float price = (float)0.0;
        int numberBot = -1;

        try {
           getNumberBotCSTRADESKINS = getAllNumberBotsTRADESKINS();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("get list bots TRADESKINS error !!!");
        }

        if(parser != null){
            try {
                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("https://trade-skins.com/load_all_bots_inventory?hash="+new Date().getTime()));// get answer from query
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/TRADESKINS.docx",jsonAnswer);
                callToParsersDB.parseJsonTRADESKINS(true);
                System.out.println("parseJsonTRADESKINS = true");
            } catch (ParseException e) {
               // e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/TRADESKINS.docx");
                callToParsersDB.parseJsonTRADESKINS(false);
                System.out.println("parseJsonTRADESKINS = false");
            }
        }
        if(jsonAnswer != null ){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            iterator = jsonObjectALL.keys();
        }


        if(iterator != null)
        while (iterator.hasNext()){
            key = iterator.next();
            skinsBOT = (JSONArray) jsonObjectALL.get(key);

            for(int i = 0 ; i < skinsBOT.length(); i ++){
                skin = (JSONObject) skinsBOT.get(i);

                //   System.out.println(skin.get("m") + "   " +skin.get("p"));

                if (skin.has("m") && !skin.isNull("m")){
                    name = skin.get("m").toString();
                }
                if(skin.has("p") && !skin.isNull("p")){
                    price = Float.parseFloat(skin.get("p").toString());
                }
                if(skin.has("b") && !skin.isNull("b")){
                    numberBot = getNumberBotCSTRADESKINS.get(skin.get("b").toString());
                }

                if (mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1) {
                    mapSkins.get(name).setPrice(price);
                    mapSkins.get(name).setListBotsTRADESkinHasSkin(numberBot);
                    countSkins++;
                } else if(!mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                    skins = new SkinValue(name, price);
                    skins.setListBotsTRADESkinHasSkin(numberBot);
                    mapSkins.put(name, skins);
                    countSkins++;
                }

                name = null;
                price = -1;
                numberBot = -1;
                skin = null;
                skins = null;

            }
            skinsBOT = null;
            key = null;

        }

        if (countSkins > 100){

            System.out.println("PARSE TRADESKINS FINISH("+countSkins+")");
            if(callToParsersDB.isAllowGetTRADESKINSSkins()){
                callToParsersDB.TRADESKINSNewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                jsonAnswer = null;
                jsonObjectALL = null;
                parser = null;
                skin = null;
                skins = null;
                skinsBOT = null;
                getRequest = new GetRequest();
                readWriteFile = new ReadWriteFile();
            }

        }else {
            System.out.println("PARSE TRADESKINS CRASH:(");
            parse();
        }
    }

    public Map<String,Integer>  getAllNumberBotsTRADESKINS() throws Exception {

        Map<String,Integer> getBotCSTRADESKINS = new HashMap<>();
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("https://trade-skins.com/list_bots?hash="+new Date().getTime()));// get answer from query

        org.json.JSONArray jsonArray = new JSONArray(jsonAnswer);

        for (int i = 0; i < jsonArray.length(); i++) {

            org.json.JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            getBotCSTRADESKINS.put(jsonObject.get("steamid").toString(),i+1);
        }

        return  getBotCSTRADESKINS;
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

}
