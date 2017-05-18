package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public class ParserCSDEALS extends ClassParser implements InterfaceParserMethod {

    public ParserCSDEALS(ParsersDATABASE ParsersBD){
        regestrationCallBackCSDEALS(ParsersBD);
        startMainThread();
    }

    interface callToParsersDATABASE{
        void CSDEALSNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetCSDEALSSkins();
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackCSDEALS( ParsersDATABASE callToParsersDB){
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
//                        Thread.sleep(10000);
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
        }

    }

    @Override
    public void parse() throws JSONException {

        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;
        org.json.JSONObject jsonObjectALL = null;
        org.json.JSONArray jsonArray =  null;
        Map<String,SkinValue> mapSkins = new HashMap<>();

        JSONObject skin ;
        SkinValue skins;
        String name = null;
        float price = -1;
        int numberBot = -1;

        if(parser != null){
            try {
                jsonAnswer =  ""+ readWriteFile.downloadContentJson("src/JsonSitesSaveded/CSDEALS.docx");// get answer from query
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(jsonAnswer != null){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            jsonArray =  (org.json.JSONArray) jsonObjectALL.get("response");
        }

        if(jsonArray != null)
        for(int i = 0; i < jsonArray.length();i++){
            skin = (JSONObject) jsonArray.get(i);
            // System.out.println(skin.get("market_name")+"  "+skin.get("b")+"  "+skin.get("value"));


            if(skin.has("market_name") && !skin.isNull("market_name")){
                name = skin.get("market_name").toString();
            }
            if(skin.has("value") && !skin.isNull("value")){

                price = (float) Double.parseDouble(String.format("%.4s%n", (( Float.parseFloat(skin.get("value").toString()) ))));

            }
            if(skin.has("b") && !skin.isNull("b")){
                numberBot = Integer.parseInt(skin.get("b").toString());
            }

            if (mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1) {
                mapSkins.get(name).setPrice(price);
                mapSkins.get(name).setListBotsCSDEALSHasSkin(numberBot);
            } else if(!mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                skins = new SkinValue(name, price);
                skins.setListBotsCSDEALSHasSkin(numberBot);
                mapSkins.put(name, skins);
            }

            name = null;
            skins = null;
            skin = null;
            numberBot = -1;
            price = -1;

        }

        if (mapSkins.size() > 100){
            System.out.println("PARSE CSDEALS FINISH("+mapSkins.size()+")");
            if(callToParsersDB.isAllowGetCSDEALSSkins()){
                callToParsersDB.CSDEALSNewSkins(mapSkins);
                mapSkins = null;
                parser = null;
                skin = null;
                skins = null;
                jsonAnswer = null;
                jsonArray = null;
                jsonObjectALL = null;
                readWriteFile = new ReadWriteFile();
            }

        }else {
            System.out.println("PARSE CSDEALS CRASH:(");
            parse();
        }

    }

}
