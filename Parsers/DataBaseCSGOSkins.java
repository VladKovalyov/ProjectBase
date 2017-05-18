package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 27.04.17.
 */
public class DataBaseCSGOSkins extends ClassParser implements InterfaceParserMethod {

    private ArrayList<String> nameSkins = new ArrayList<>();

    public DataBaseCSGOSkins(){}
    public DataBaseCSGOSkins(ParsersDATABASE ParsersBD){
        regestrationCallBackDataBaseCSGO(ParsersBD);
    }

    interface callToParsersDATABASE{
        void DataBaseCSGONewSkins(ArrayList<String> nameSkins);
        boolean isAllowGetDataBaseCSGO();
    }

    private callToParsersDATABASE callToParsersDB;

    public void regestrationCallBackDataBaseCSGO( ParsersDATABASE callToParsersDB){
        this.callToParsersDB = (callToParsersDATABASE) callToParsersDB;
    }

    @Override
    public void parse() throws JSONException, IOException {

        nameSkins.clear();
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        String jsonAnswer =  null;
        org.json.JSONObject jsonObjectALL = null;
        org.json.JSONArray jsonArray = null;

        JSONObject objectSkin = null;
        SkinValue skins;
        String name = null;
        int countSkins = 0;

        if(parser != null){
            try {
                jsonAnswer =  ""+parser.parse(getRequest.sendGetQuery("http://api.csgo.steamlytics.xyz/v1/items?key=52a4f314749ae66e59c202b0b29c02e3"));// get answer from query
                setParseJsonReturned(true);
            } catch (ParseException e) {
                // e.printStackTrace();
                jsonAnswer = readWriteFile.downloadContentJson("src/JsonSitesSaveded/RAFFLETRADES.docx");
                setParseJsonReturned(false);
            }
        }
        if(jsonAnswer != null){
            jsonObjectALL = new org.json.JSONObject(jsonAnswer);
        }
        if(jsonObjectALL != null){
            jsonArray =  (org.json.JSONArray) jsonObjectALL.get("items");
        }

        System.out.println("" + jsonArray.length());

        for (int i = 0 ; i < jsonArray.length(); i++){
            objectSkin = (JSONObject) jsonArray.get(i);

            name = objectSkin.get("market_hash_name").toString();

            if(!nameSkins.contains(name)){
                nameSkins.add(name);
            }

        }

        if(nameSkins.size() > 100){
            System.out.println("PARSE DataBaseCSGOSkins FINISH("+nameSkins.size()+")");
            if(callToParsersDB.isAllowGetDataBaseCSGO()){
                callToParsersDB.DataBaseCSGONewSkins(nameSkins);
            }
        }
        else {
            System.out.println("PARSE DataBaseCSGOSkins CRASH:(");
            parse();
        }
    }

    public static void main(String[] args) throws IOException, JSONException {
        new DataBaseCSGOSkins().parse();
    }

}
