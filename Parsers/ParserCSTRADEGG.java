package com.company.Parsers;

import com.company.SkinValue;
import com.sun.management.OperatingSystemMXBean;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public class ParserCSTRADEGG extends ClassParser implements InterfaceParserMethod {


    public ParserCSTRADEGG(){}
    public ParserCSTRADEGG(ParsersDATABASE ParsersBD){
        regestrationCallBackCSTRADEGG(ParsersBD);
        startMainThread();
    }

    interface callToParsersDATABASE{
        void CSTRADEGGNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetCSTRADEGGSkins();
        void parseJsonCSTRADEGG(boolean parseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackCSTRADEGG( ParsersDATABASE callToParsersDB){
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
                        Thread.sleep(50000);

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
        Map<String,SkinValue> mapSkins = new HashMap<>();
        SkinValue skin;
        org.json.JSONObject object;
        String name;
        float price = 0;
        int numberBot;
        int countSkinsONE = 0;


        if(parser != null){
            try {
                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("https://cstrade.gg/loadBotInventory?order_by=price_desc&bot=all"));// get answer from query
                readWriteFile.writeJsonToFile("src/JsonSitesSaveded/TRADESKINS.docx",jsonAnswer);
                callToParsersDB.parseJsonCSTRADEGG(true);
                System.out.println("parseJsonCSTRADEGG = true");
            } catch (ParseException e) {
                //e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/TRADESKINS.docx");
                callToParsersDB.parseJsonCSTRADEGG(false);
                System.out.println("parseJsonCSTRADEGG = false");
            }
        }
        if(jsonAnswer != null){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            jsonArray = jsonObjectALL.getJSONArray("inventory");
        }

        if(jsonArray != null)
        for(int i = 0; i < jsonArray.length(); i++) {

            object = (org.json.JSONObject) jsonArray.get(i);
            name = object.get("market_hash_name").toString();
            numberBot = Integer.parseInt(object.get("bot").toString());
            price = Float.parseFloat(object.get("price").toString());

            //  System.out.println(object.get("market_hash_name") + " " + object.get("price") + "bot: " + object.get("bot"));
            if (mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1) {
                mapSkins.get(name).setPrice(price);
                mapSkins.get(name).setListBotsCSTGGHasSkin(numberBot);
                countSkinsONE = mapSkins.get(name).getCountItemsCSTGG();
                countSkinsONE++;
                mapSkins.get(name).setCountItemsCSTGG(countSkinsONE);
            } else if(!mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                skin = new SkinValue(name, price);
                skin.setListBotsCSTGGHasSkin(numberBot);
                skin.setCountItemsCSTGG(1);
                mapSkins.put(name, skin);
            }

            name = null;
            price = -1;
            numberBot = -1;
            countSkinsONE = 0;
            skin = null;
            object = null;


        }

        if(mapSkins.size() > 100){
            System.out.println("PARSE CSTRADEGG FINISH("+mapSkins.size()+")");
            if(callToParsersDB.isAllowGetCSTRADEGGSkins())
            {
                callToParsersDB.CSTRADEGGNewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                parser = null;
                skin = null;
                jsonAnswer = null;
                jsonArray = null;
                jsonObjectALL = null;
                getRequest = new GetRequest();
                readWriteFile = new ReadWriteFile();

            }

        }else{
            System.out.println("PARSE CSTRADEGG CRASH:(");
            parse();
        }

    }

    public static void main(String[] args) {
        new ParserCSTRADEGG().startMainThread();
    }

}
