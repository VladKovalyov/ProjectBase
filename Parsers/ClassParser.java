package com.company.Parsers;

import com.company.SkinValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 25.04.17.
 */
public class ClassParser {


    volatile protected Map<String,SkinValue> mapSkins = new ConcurrentHashMap<>();
    protected GetRequest getRequest = new GetRequest();
    protected ReadWriteFile readWriteFile = new ReadWriteFile();

    protected boolean parseJsonReturned = true;
    private boolean liveThread = true;
    private boolean parseFlag = false;

    public boolean isParseJsonReturned() {
        return parseJsonReturned;
    }

    public void setParseJsonReturned(boolean parseJsonReturned) {
        this.parseJsonReturned = parseJsonReturned;
    }

    public boolean isLiveThread() {
        return liveThread;
    }

    public void setLiveThread(boolean liveThread) {
        this.liveThread = liveThread;
    }

    public boolean isParseFlag() {
        return parseFlag;
    }

    public void setParseFlag(boolean parseFlag) {
        this.parseFlag = parseFlag;
    }

    public Map<String, SkinValue> getMapSkins() {
        if (this.isParseFlag())return this.mapSkins;

        return null;
    }

}
