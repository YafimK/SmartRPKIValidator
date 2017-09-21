package com.kazak.safebgpcontroller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * the networking client to forward requests to the smart validator and pass
 */

public class SmartValidatorClient {
    private static SmartValidatorClient instance = null;
    private RequestQueue mRequestQueue;
    private static Context currentContext;
    private SmartValidatorClient(Context context){
        currentContext = context;
        mRequestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(currentContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public static synchronized SmartValidatorClient getInstance(Context context){
        if(instance == null){
            instance = new SmartValidatorClient(context);
        }
        return  instance;
    }
}
