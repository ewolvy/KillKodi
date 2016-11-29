package com.mooo.ewolvy.killkodi;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

class KKState {
    // Constant for logs
    final String LOG_TAG = "KKState";

    // Variables
    private boolean IsKilled;
    private String KodiAddress;
    private int KodiPort;
    private URL KodiURL;

    // Constructor
    KKState(String Address, int Port){
        KodiAddress = Address;
        KodiPort = Port;
        IsKilled = false;
        String addressConstruction = "http://";
        addressConstruction = addressConstruction + KodiAddress;
        addressConstruction = addressConstruction + ":";
        addressConstruction = addressConstruction + KodiPort;
        addressConstruction = addressConstruction + "/killkodi";
        try {
            KodiURL = new URL(addressConstruction);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
        }
    }

    // Getters and setters
    public String getKodiAddress() {
        return KodiAddress;
    }

    public int getKodiPort() {
        return KodiPort;
    }

    public URL getKodiURL() {
        return KodiURL;
    }

    public boolean getIsKilled(){
        return IsKilled;
    }

    public void setKodiAddress(String kodiAddress) {
        KodiAddress = kodiAddress;
    }

    public void setKodiPort(int kodiPort) {
        KodiPort = kodiPort;
    }

    public void setKodiURL(URL kodiURL) {
        KodiURL = kodiURL;
    }

    public void setIsKilled(boolean isKilled) {
        IsKilled = isKilled;
    }
}
