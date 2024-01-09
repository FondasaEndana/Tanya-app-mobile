package com.example.tanya_app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class volleyConnection {
    private static volleyConnection vInstance;
    private RequestQueue requestQueue;
    private static Context vctx;

    private volleyConnection (Context context){
        vctx = context;
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(vctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized volleyConnection getInstance(Context context){
        if (vInstance == null){
            vInstance = new volleyConnection(context);
        }
        return vInstance;
    }

    public <T> void addToRequestQue (Request<T> request){
        getRequestQueue().add(request);
    }
}
