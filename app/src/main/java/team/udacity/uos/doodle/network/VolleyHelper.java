package team.udacity.uos.doodle.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyHelper {

    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    public static void init(Context context){
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());

    }

    public static RequestQueue getRequestQueue(){
        if (mRequestQueue == null) {
            throw new IllegalStateException("Volley isn't initialized.");
        }
        return mRequestQueue;
    }

}
