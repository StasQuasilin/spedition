package ua.svasilina.spedition.utils.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Connector {
    private static Connector connector;
    private RequestQueue requestQueue;


    public static synchronized Connector getConnector() {
        if (connector == null){
            connector = new Connector();
        }
        return connector;
    }

    public <T> void addRequest(Context context, Request<T> req){
        getRequest(context).add(req);
    }

    public RequestQueue getRequest(Context context){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }

        return requestQueue;
    }
}
