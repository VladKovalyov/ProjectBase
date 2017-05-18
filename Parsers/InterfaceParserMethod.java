package com.company.Parsers;

import com.company.SkinValue;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by admin on 25.04.17.
 */
public interface InterfaceParserMethod {

    void parse() throws JSONException, IOException;

    default int getRealNumberBot(String number) {
        return -1;
    }
    default void startMainThread(){

    }

}
