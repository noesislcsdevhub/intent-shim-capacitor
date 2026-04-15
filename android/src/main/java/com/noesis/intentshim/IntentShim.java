package com.noesis.intentshim;

import com.getcapacitor.Logger;

public class IntentShim {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
