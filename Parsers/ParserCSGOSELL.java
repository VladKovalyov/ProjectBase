package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public class ParserCSGOSELL extends ClassParser implements InterfaceParserMethod{

    private PostRequest postRequest = new PostRequest();

    public ParserCSGOSELL(){

    }
    public ParserCSGOSELL(ParsersDATABASE ParsersBD){
        regestrationCallBackCSSELL(ParsersBD);
        startMainThread();

    }

    interface callToParsersDATABASE{
        void CSSELLNewSkins(Map<String,SkinValue> mapInput);
        boolean isAllowGetCSSELLSkins();
        void parseJsonCSGOSELL(boolean ParseJson);
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackCSSELL( ParsersDATABASE callToParsersDB){
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


        String jsonAnswer = null;
        JSONArray jsonArrayFullInventories = null;

        try {
            jsonAnswer = postRequest.sendPostRequest("https://csgosell.com/phpLoaders/getInventory/getInventory.php", "stage=botAll&steamId=76561198364873979&hasBonus=false&coins=0");
            readWriteFile.writeJsonToFile("src/JsonSitesSaveded/CSSELL.docx",jsonAnswer);
            callToParsersDB.parseJsonCSGOSELL(true);
            System.out.println("parseJsonCSGOSELL = true");

        } catch (Exception e) {
            // e.printStackTrace();
            jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/CSSELL.docx");
            callToParsersDB.parseJsonCSGOSELL(false);
            System.out.println("parseJsonCSGOSELL = false");
        }

        String name = null;
        float price = -1;
        int numberBot = -1;
        SkinValue skin;
        org.json.JSONObject skinsObject ;
        Map<String,SkinValue> mapSkins = new HashMap<>();

        if (jsonAnswer != null) {
            try {
                jsonArrayFullInventories = new JSONArray(jsonAnswer.toString());
            } catch (Exception e){
              //  e.printStackTrace();
            }

        }
        if (jsonArrayFullInventories != null)
            for (int i = 0; i < jsonArrayFullInventories.length(); i++) {
                // System.out.println(doc1.select("h") +" "+ jsonArrayFullInventories.get(i).attr("p") + "botNum= " +link.get(i).attr("bt"));

                skinsObject = (JSONObject) jsonArrayFullInventories.get(i);

                if (!skinsObject.isNull("h")) {
                    name = skinsObject.get("h").toString();
                }
                if (!skinsObject.isNull("bt")) {
                    numberBot = Integer.parseInt(skinsObject.get("bt").toString());
                }
                if (!skinsObject.isNull("p")){
                    price = Float.parseFloat(skinsObject.get("p").toString());
                }

                if (mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                    mapSkins.get(name).setPrice(price);
                    mapSkins.get(name).setListBotsCSSEELHasSkin(numberBot);
                } else if (!mapSkins.containsKey(name) && name != null && price > 0 && numberBot != -1){
                    skin = new SkinValue(name,price);
                    skin.setListBotsCSSEELHasSkin(numberBot);
                    mapSkins.put(name,skin);
                }

                // System.out.println(name +" "+ skinsObject.get("p").toString() + "botNum= " +botNum);

                name = null;
                skin = null;
                skinsObject = null;
                price = -1;
                numberBot = -1;

            }

        if (mapSkins.size() > 100) {
            System.out.println("PARSE CSGOSELL FINISH(" + mapSkins.size() + ")");
            if(callToParsersDB.isAllowGetCSSELLSkins()){
                callToParsersDB.CSSELLNewSkins(mapSkins);
                mapSkins.clear();
                mapSkins = null;
                jsonAnswer = null;
                jsonArrayFullInventories = null;
                skin = null;
                skinsObject = null;
                readWriteFile = new ReadWriteFile();
                postRequest = new PostRequest();
            }
        } else {
            System.out.println("PARSE CSGOSELL CRASH:(");
            parse();
        }


    }

}
