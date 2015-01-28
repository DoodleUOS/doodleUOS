package team.udacity.uos.doodle.network.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.network.Urls;

/**
 * Created by JYPark on 2015. 1. 26..
 */
public class DetailViewRequest extends BaseRequest{
    Response.Listener<Doodle> mListener;
    Context mContext;

    private int mNo;

    public DetailViewRequest(Context context, Response.Listener<Doodle> listener, Response.ErrorListener errorListener) {
        super(context, Urls.SERVER_URL, errorListener);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q","21");
        params.put("doono", String.valueOf(mNo));

        return params;
    }

    public void setParameter(int dooNo) {
        mNo = dooNo;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        Doodle doodle = new Doodle();
        try {
            doodle = new Gson().fromJson(response.getString("data"), Doodle.class);
        }  catch (JSONException e) {
            e.printStackTrace();
        }

        mListener.onResponse(doodle);
    }
}
