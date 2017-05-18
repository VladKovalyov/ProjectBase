package com.company.Parsers;

import com.company.SkinValue;
import com.company.TableModels.TableModelComparsion;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 26.04.17.
 */
public class ParsersDATABASE implements ParserLOOTFARM.callToParsersDATABASE,
                                            ParserCSTM.callToParsersDATABASE,
                                            ParserOPSKINS.callToParsersDATABASE,
                                            ParserCSMONEY.callToParsersDATABASE,
                                            ParserCSGOSELL.callToParsersDATABASE,
                                            ParserCSGOSWAP.callToParsersDATABASE,
                                            ParserCSTRADEGG.callToParsersDATABASE,
                                            ParserCSTRADEME.callToParsersDATABASE,
                                            ParserSKINSJAR.callToParsersDATABASE,
                                            ParserRAFFLETRADES.callToParsersDATABASE,
                                            ParserCSDEALS.callToParsersDATABASE,
                                            ParserTRADESKINS.callToParsersDATABASE,
                                            DataBaseCSGOSkins.callToParsersDATABASE{


    volatile private Map<String,SkinValue> mapSkinsLOOTFARM = new ConcurrentHashMap<>();
    private boolean allowGetLOOTFARMSkins = true;
    private boolean parseJsonLOOTFARM = false;
    volatile private Map<String,SkinValue> mapSkinsCSTM = new ConcurrentHashMap<>();
    private boolean allowGetCSTMSkins = true;
    private boolean parseJsonCSTM = false;
    volatile private Map<String,SkinValue> mapSkinsBUYNOW = new ConcurrentHashMap<>();
    private boolean allowGetBUY_NOWSkins = true;
    private boolean parseJsonBUY_NOW = false;
    volatile private Map<String,SkinValue> mapSkinsOPSKINS = new ConcurrentHashMap<>();
    private boolean allowGetOPSKINSSkins = true;
    private boolean parseJsonOPSKINS = false;
    volatile private Map<String,SkinValue> mapSkinsCSMONEY = new ConcurrentHashMap<>();
    private boolean allowGetCSMONEYSkins = true;
    private boolean parseJsonCSMONEY = false;
    volatile private Map<String,SkinValue> mapSkinsCSSELL = new ConcurrentHashMap<>();
    private boolean allowGetCSSELLSkins = true;
    private boolean parseJsonCSSELL = false;
    volatile private Map<String,SkinValue> mapSkinsCSGOSWAP = new ConcurrentHashMap<>();
    private boolean allowGetCSGOSWAPSkins = true;
    private boolean parseJsonCSGOSWAP = false;
    volatile private Map<String,SkinValue> mapSkinsCSTRADEGG = new ConcurrentHashMap<>();
    private boolean allowGetCSTRADEGGSkins = true;
    private boolean parseJsonCSTRADEGG = false;
    volatile private Map<String,SkinValue> mapSkinsCSTRADEME = new ConcurrentHashMap<>();
    private boolean allowGetCSTRADEMESkins = true;
    private boolean parseJsonCSTRADEME = false;
    volatile private Map<String,SkinValue> mapSkinsSKINSJAR = new ConcurrentHashMap<>();
    private boolean allowGetSKINSJARSkins = true;
    private boolean parseJsonSKINSJAR = false;
    volatile private Map<String,SkinValue> mapSkinsRAFFLETRADES = new ConcurrentHashMap<>();
    private boolean allowGetRAFFLETRADESSkins = true;
    private boolean parseJsonRAFLETRADES = false;
    volatile private Map<String,SkinValue> mapSkinsCSDEALS = new ConcurrentHashMap<>();
    private boolean allowGetCSDEALSSkins = true;
    volatile private Map<String,SkinValue> mapSkinsTRADESKINS = new ConcurrentHashMap<>();
    private boolean allowGetTRADESKINSSkins = true;
    private boolean parseJsonTRADESKINS = false;
    volatile private ArrayList<String>  mapSkinsDataBaseCSGO = new ArrayList<>();
    private boolean allowGetDataBaseCSGO = true;


    public ParsersDATABASE() {
//                parserLOOTFARM.startMainThread();
        //        parserCSMONEY.startMainThread();
        //        parserSKINSJAR.startMainThread();
        //        parserCSGOSELL.startMainThread();
        //        parserCSTRADEGG.startMainThread();
        //        parserCSGOSWAP.startMainThread();
        //        parserRAFFLETRADES.startMainThread();
        //        parserTRADESKINS.startMainThread();
        //        parserCSDEALS.parse();
        //        parserCSTRADEME.startMainThread();
        //        parserCSTM.parse();
        //        parserOPSKINS.parse();


    }

    public void dudos(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(100);
                        System.out.println("mapSkinsTRADESKINS = "+ mapSkinsTRADESKINS.size());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });thread.start();
    }


                                         /*LOOTFARM CHANGES*/
    @Override
    public void LootFarmNewSkins(Map<String,SkinValue> mapInput) {

        allowGetLOOTFARMSkins = false;
        System.out.println("allowGetLOOTFARMSkins =" +allowGetLOOTFARMSkins);

        mapSkinsLOOTFARM = null;
        mapSkinsLOOTFARM = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetLOOTFARMSkins = true;
        System.out.println("allowGetLOOTFARMSkins =" +allowGetLOOTFARMSkins);

    }

    @Override
    public boolean isAllowGetLOOTFARMSkins() {
        return this.allowGetLOOTFARMSkins;
    }

    @Override
    public void parseJsonLOOTFARM(boolean parseJson) {
        this.parseJsonLOOTFARM = parseJson;
    }

    public Map<String,SkinValue> getMapSkinsLOOTFARM() {
       while(!isAllowGetLOOTFARMSkins())
       {
           System.out.println("wait return LOOTFARM");
       }
        System.out.println("return LOOTFARM");
       return mapSkinsLOOTFARM;

    }

    public boolean isParseJsonLOOTFARM() {
        return parseJsonLOOTFARM;
    }



                                    /*CSTM BUY_NOW CHANGES*/
    @Override
    public void CSTMNewSkins(Map<String, SkinValue> mapInput) {

        allowGetCSTMSkins = false;
        System.out.println("allowGetCSTMSkins =" +allowGetCSTMSkins);

        mapSkinsCSTM = null;
        mapSkinsCSTM = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetCSTMSkins = true;
        System.out.println("allowGetCSTMSkins =" +allowGetCSTMSkins);
    }

    @Override
    public void BUY_NOWNewSkins(Map<String, SkinValue> mapInput) {

        allowGetBUY_NOWSkins = false;
        System.out.println("allowGetBUY_NOWSkins =" +allowGetBUY_NOWSkins);

        mapSkinsBUYNOW.putAll(mapInput);
        mapInput = null;

        allowGetBUY_NOWSkins = true;
        System.out.println("allowGetBUY_NOWSkins =" +allowGetBUY_NOWSkins);
    }

    @Override
    public boolean isAllowGetCSTMSkins() {
        return this.allowGetCSTMSkins;
    }

    @Override
    public boolean isAllowGetBUYNOWSkins() {
        return this.allowGetBUY_NOWSkins;
    }

    public Map<String, SkinValue> getMapSkinsCSTM() {
       while(!isAllowGetCSTMSkins())
       {
           System.out.println("wait return CSTM");
       }
        System.out.println("return CSTM");
        return mapSkinsCSTM;
    }

    public Map<String, SkinValue> getMapSkinsBUYNOW() {
        while(!isAllowGetBUY_NOWSkins())
        {
            System.out.println("wait return BUY_NOW");
        }
        System.out.println("return BUY_NOW");
        return mapSkinsBUYNOW;

    }

    public boolean isAllowGetBUY_NOWSkins() {
        return allowGetBUY_NOWSkins;
    }

    public boolean isParseJsonCSTM() {
        return parseJsonCSTM;
    }

    public boolean isParseJsonBUY_NOW() {
        return parseJsonBUY_NOW;
    }


                                    /*OPSKINS CHANGES*/
    @Override
    public void OPSKINSNewSkins(Map<String, SkinValue> mapInput) {
        allowGetOPSKINSSkins = false;
        System.out.println("allowGetOPSKINSSkins =" +allowGetOPSKINSSkins);

        mapSkinsOPSKINS = null;
        mapSkinsOPSKINS = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetOPSKINSSkins = true;
        System.out.println("allowGetOPSKINSSkins =" +allowGetOPSKINSSkins);
    }

    @Override
    public boolean isAllowGetOPSKINSSkins() {
        return this.allowGetOPSKINSSkins;
    }

    @Override
    public void parseJSONOPSKINS(boolean parseJson) {
        this.parseJsonOPSKINS = parseJson;
    }

    public Map<String, SkinValue> getMapSkinsOPSKINS() {
        while (!isAllowGetOPSKINSSkins())
        {
            System.out.println("wait return OPSKINS");
        }
        System.out.println("return OPSKINS");
        return mapSkinsOPSKINS;

    }

    public boolean isParseJsonOPSKINS() {
        return parseJsonOPSKINS;
    }

                                    /*CSMONEY CHANGES*/
    @Override
    public void CSMONEYNewSkins(Map<String, SkinValue> mapInput) {

        allowGetCSMONEYSkins = false;

        mapSkinsCSMONEY = null;
        mapSkinsCSMONEY = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetCSMONEYSkins = true;
        System.out.println("allowGetCSMONEYSkins =" +allowGetCSMONEYSkins);

    }

    @Override
    public boolean isAllowGetCSMONEYSkins() {
       return this.allowGetCSMONEYSkins;
    }

    @Override
    public void parseJsonCSMONEY(boolean ParseJson) {
        this.parseJsonCSMONEY = ParseJson;
    }

    public Map<String, SkinValue> getMapSkinsCSMONEY() {

        while(!isAllowGetCSMONEYSkins()) {
            System.out.println("wait return CSMONEY");
        }
        System.out.println("return CSMONEY");
        return mapSkinsCSMONEY;

    }

    public boolean isParseJsonCSMONEY() {
        return this.parseJsonCSMONEY;
    }

                                    /*CSSELL CHANGES*/
    @Override
    public void CSSELLNewSkins(Map<String, SkinValue> mapInput) {

        allowGetCSSELLSkins = false;
        System.out.println("allowGetCSSELLSkins =" +allowGetCSSELLSkins);

        mapSkinsCSSELL = null;
        mapSkinsCSSELL = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetCSSELLSkins = true;
        System.out.println("allowGetCSSELLSkins =" +allowGetCSSELLSkins);
    }

    @Override
    public boolean isAllowGetCSSELLSkins() {
        return this.allowGetCSSELLSkins;
    }

    @Override
    public void parseJsonCSGOSELL(boolean ParseJson) {
        this.parseJsonCSSELL = ParseJson;
    }

    public Map<String, SkinValue> getMapSkinsCSSELL() {
        while(!isAllowGetCSSELLSkins())
        {
            System.out.println("wait return CSGOSELL");
        }
        System.out.println("return CSGOSELL");
        return mapSkinsCSSELL;

    }

    public boolean isParseJsonCSSELL() {
        return parseJsonCSSELL;
    }

                                    /*CSGOSWAP CHANGES*/
    @Override
    public void CSGOSWAPNewSkins(Map<String, SkinValue> mapInput) {

        allowGetCSGOSWAPSkins = false;
        System.out.println("allowGetCSGOSWAPSkins =" +allowGetCSGOSWAPSkins);

        mapSkinsCSGOSWAP = null;
        mapSkinsCSGOSWAP = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetCSGOSWAPSkins = true;
        System.out.println("allowGetCSGOSWAPSkins =" +allowGetCSGOSWAPSkins);
    }

    @Override
    public boolean isAllowGetCSGOSWAPSkins() {
        return this.allowGetCSGOSWAPSkins;
    }

    @Override
    public void parseJsonCSGOSWAP(boolean ParseJson) {
        this.parseJsonCSGOSWAP = ParseJson;
    }

    public Map<String, SkinValue> getMapSkinsCSGOSWAP() {
       while(!isAllowGetCSGOSWAPSkins())
       {
           System.out.println("wait return CSGOSWAP");
       }
        System.out.println("return CSGOSWAP");
        return mapSkinsCSGOSWAP;

    }

    public boolean isParseJsonCSGOSWAP() {
        return parseJsonCSGOSWAP;
    }

                                    /*CSTRADEGG CHANGES*/
    @Override
    public void CSTRADEGGNewSkins(Map<String, SkinValue> mapInput) {

        allowGetCSTRADEGGSkins = false;
        System.out.println("allowGetCSTRADEGGSkins =" +allowGetCSTRADEGGSkins);

        mapSkinsCSTRADEGG = null;
        mapSkinsCSTRADEGG = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetCSTRADEGGSkins = true;
        System.out.println("allowGetCSTRADEGGSkins =" +allowGetCSTRADEGGSkins);
    }

    @Override
    public boolean isAllowGetCSTRADEGGSkins() {
        return this.allowGetCSTRADEGGSkins;
    }

    @Override
    public void parseJsonCSTRADEGG(boolean parseJson) {
        this.parseJsonCSTRADEGG = parseJson;
    }


    public Map<String, SkinValue> getMapSkinsCSTRADEGG() {
       while (!isAllowGetCSTRADEGGSkins())
       {
           System.out.println("wait return CSTRADEGG");
       }
        System.out.println("return CSTRADEGG");
        return mapSkinsCSTRADEGG;

    }

    public boolean isParseJsonCSTRADEGG() {
        return parseJsonCSTRADEGG;
    }

                                    /*CSTRADEME CHANGES*/
    @Override
    public void CSTRADEMENewSkins(Map<String, SkinValue> mapInput) {

        allowGetCSTRADEMESkins = false;
        System.out.println("allowGetCSTRADEMESkins =" +allowGetCSTRADEMESkins);

        mapSkinsCSTRADEME = null;
        mapSkinsCSTRADEME = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetCSTRADEMESkins = true;
        System.out.println("allowGetCSTRADEMESkins =" +allowGetCSTRADEMESkins);
    }

    @Override
    public boolean isAllowGetCSTRADEMESkins() {
        return this.allowGetCSTRADEMESkins;
    }

    @Override
    public void parseJsonCSTRADEME(boolean parseJson) {
        this.parseJsonCSTRADEME = parseJson;
    }

    public Map<String, SkinValue> getMapSkinsCSTRADEME() {
       while(!isAllowGetCSTRADEMESkins())
       {
           System.out.println("wait return CSTRADEME");
       }
        System.out.println("return CSTRADEME");
        return mapSkinsCSTRADEME;


    }

    public boolean isParseJsonCSTRADEME() {
        return parseJsonCSTRADEME;
    }


                                    /*SKINSJAR CHANGES*/
    @Override
    public void SKINSJARNewSkins(Map<String, SkinValue> mapInput) {
        allowGetSKINSJARSkins = false;

        System.out.println("allowGetSKINSJARSkins =" +allowGetSKINSJARSkins);

        mapSkinsSKINSJAR = null;
        mapSkinsSKINSJAR = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetSKINSJARSkins = true;
        System.out.println("allowGetSKINSJARSkins =" +allowGetSKINSJARSkins);
    }
    @Override
    public boolean isAllowGetSKINSJARSkins() {
        return this.allowGetSKINSJARSkins;
    }

    @Override
    public void parseJsonSKINSJAR(boolean parseJson) {
        this.parseJsonSKINSJAR = parseJson;
    }

    public Map<String, SkinValue> getMapSkinsSKINSJAR() {
        while(!isAllowGetSKINSJARSkins()) {
            System.out.println("wait return SKINSJAR");
        }
        System.out.println("return SKINSJAR");
        return mapSkinsSKINSJAR;

    }

    public boolean isParseJsonSKINSJAR() {
        return parseJsonSKINSJAR;
    }


                                    /*RAFFLETRADES CHANGES*/
    @Override
    public void RAFFLETRADESNewSkins(Map<String, SkinValue> mapInput) {

        allowGetRAFFLETRADESSkins = false;
        System.out.println("allowGetRAFFLETRADESSkins =" +allowGetRAFFLETRADESSkins);

        mapSkinsRAFFLETRADES = null;
        mapSkinsRAFFLETRADES = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetRAFFLETRADESSkins = true;
        System.out.println("allowGetRAFFLETRADESSkins =" +allowGetRAFFLETRADESSkins);
    }

    @Override
    public boolean isAllowGetRAFFLETRADESSkins() {
        return this.allowGetRAFFLETRADESSkins;
    }

    @Override
    public void paseJsonRAFFLETRADES(boolean parseJson) {
        this.parseJsonRAFLETRADES = parseJson;
    }

    public Map<String, SkinValue> getMapSkinsRAFFLETRADES() {
        while(!isAllowGetRAFFLETRADESSkins())
        {
            System.out.println("wait return RAFFLETRADES");
        }
        System.out.println("return RAFFLETRADES");
        return mapSkinsRAFFLETRADES;

    }

    public boolean isParseJsonRAFLETRADES() {
        return parseJsonRAFLETRADES;
    }


                                        /*CSDEALS CHANGES*/
    @Override
    public void CSDEALSNewSkins(Map<String, SkinValue> mapInput) {

        allowGetCSDEALSSkins = false;
        System.out.println("allowGetCSDEALSSkins =" +allowGetCSDEALSSkins);

        mapSkinsCSDEALS = null;
        mapSkinsCSDEALS = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetCSDEALSSkins = true;
        System.out.println("allowGetCSDEALSSkins =" +allowGetCSDEALSSkins);
    }

    @Override
    public boolean isAllowGetCSDEALSSkins() {
        return this.allowGetCSDEALSSkins;
    }

    public Map<String, SkinValue> getMapSkinsCSDEALS() {
       while (!isAllowGetCSDEALSSkins())
       {
           System.out.println("wait return CSDEALS");
       }
        System.out.println("return CSDEALS");
        return mapSkinsCSDEALS;


    }


                                        /*TRADESKINS CHANGES*/
    @Override
    public void TRADESKINSNewSkins(Map<String, SkinValue> mapInput) {

        allowGetTRADESKINSSkins = false;
        System.out.println("allowGetTRADESKINSSkins =" +allowGetTRADESKINSSkins);

        mapSkinsTRADESKINS = null;
        mapSkinsTRADESKINS = new ConcurrentHashMap<>(mapInput);
        mapInput = null;

        allowGetTRADESKINSSkins = true;
        System.out.println("allowGetTRADESKINSSkins =" +allowGetTRADESKINSSkins);
    }

    @Override
    public boolean isAllowGetTRADESKINSSkins() {
        return this.allowGetTRADESKINSSkins;
    }

    @Override
    public void parseJsonTRADESKINS(boolean parseJson) {
        this.parseJsonTRADESKINS = parseJson;
    }

    public Map<String, SkinValue> getMapSkinsTRADESKINS() {
       while(!isAllowGetTRADESKINSSkins())
       {
           System.out.println("wait return CSDEALS");
       }
       System.out.println("return CSDEALS");
       return mapSkinsTRADESKINS;

    }

    public boolean isParseJsonTRADESKINS() {
        return parseJsonTRADESKINS;
    }

                                        /*DataBaseCSGO CHANGES*/
    @Override
    public void DataBaseCSGONewSkins(ArrayList<String> nameSkins) {
        allowGetDataBaseCSGO = false;

        System.out.println("allowGetDataBaseCSGO =" +allowGetDataBaseCSGO);
        ArrayList<String>  mapCopy = new ArrayList<>();
        mapCopy.addAll(this.mapSkinsDataBaseCSGO);

        if(this.mapSkinsDataBaseCSGO.size() != 0)
            for (int i = 0 ; i < mapCopy.size(); i++){
                if(!nameSkins.contains(i)){
                    this.mapSkinsDataBaseCSGO.remove(i);
                } else if (nameSkins.contains(i) && !this.mapSkinsDataBaseCSGO.contains(i)){
                    this.mapSkinsDataBaseCSGO.add(nameSkins.get(i));
                }
            }
        else this.mapSkinsDataBaseCSGO.addAll(nameSkins);

        allowGetDataBaseCSGO = true;
        System.out.println("allowGetDataBaseCSGO =" +allowGetDataBaseCSGO);
    }
    @Override
    public boolean isAllowGetDataBaseCSGO() {
        return this.allowGetDataBaseCSGO;
    }

    public ArrayList<String> getMapSkinsDataBaseCSGO() {
       if(isAllowGetDataBaseCSGO()) return mapSkinsDataBaseCSGO;

       return null;
    }


//    public static void main(String[] args) throws JSONException, IOException {
//        ParsersDATABASE main = new ParsersDATABASE();
//
////        ParserLOOTFARM parserLOOTFARM = new ParserLOOTFARM(main);
////        parserLOOTFARM.startMainThread();
//
////        ParserCSTM parserCSTM = new ParserCSTM(main);
////        parserCSTM.startMainThread();
//
////          ParserOPSKINS parserOPSKINS = new ParserOPSKINS(main);
////          parserOPSKINS.startMainThread();
//
////            ParserCSMONEY parserCSMONEY = new ParserCSMONEY(main);
//
//           // parserCSMONEY.startMainThread();
////            ParserCSGOSELL parserCSGOSELL = new ParserCSGOSELL(main);
////            parserCSGOSELL.startMainThread();
////            ParserCSGOSWAP parserCSGOSWAP = new ParserCSGOSWAP(main);
////        parserCSGOSWAP.startMainThread();
////            ParserCSTRADEGG parserCSTRADEGG = new ParserCSTRADEGG(main);
////        parserCSTRADEGG.startMainThread();
//
////        ParserCSTRADEME parserCSTRADEME = new ParserCSTRADEME(main);
////        parserCSTRADEME.startMainThread();
////        ParserSKINSJAR parserSKINSJAR = new ParserSKINSJAR(main);
////        parserSKINSJAR.startMainThread();
//
////        ParserCSDEALS parserCSDEALS = new ParserCSDEALS(main);
////        parserCSDEALS.parse();
////        ParserTRADESKINS parserTRADESKINS = new ParserTRADESKINS(main);
////        parserTRADESKINS.startMainThread();
//
////        DataBaseCSGOSkins dataBaseCSGOSkins = new DataBaseCSGOSkins(main);
////        dataBaseCSGOSkins.parse();
//
////        ParserRAFFLETRADES parserRAFFLETRADES = new ParserRAFFLETRADES(main);
////        parserRAFFLETRADES.startMainThread();
////
////        try {
////            Thread.sleep(8000);
////            Map<String,Map<String,SkinValue>> inputMap = new HashMap<>();
////
////            inputMap.put("CSMONEY",main.getMapSkinsCSMONEY());
////            inputMap.put("RAFFLE",main.getMapSkinsRAFFLETRADES());
////            inputMap.put("CSGOSWAP",main.getMapSkinsCSGOSWAP());
////
////            TableModelComparsion tableModelComparsion = new TableModelComparsion(inputMap,1,1);
////
////            //System.out.println(tableModelComparsion.getValueAt(2,2) );
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//

//    }



}