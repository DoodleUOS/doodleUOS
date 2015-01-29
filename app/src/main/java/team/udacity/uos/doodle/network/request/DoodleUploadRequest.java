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
public class DoodleUploadRequest extends BaseRequest{
    Response.Listener<Doodle> mListener;
    Context mContext;

    private Doodle mDoodle;

    public DoodleUploadRequest(Context context, Response.Listener<Doodle> listener, Response.ErrorListener errorListener) {
        super(context, Urls.SERVER_URL, errorListener);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q","20");
        params.put("memno", String.valueOf(mDoodle.getDooMemNo()));
        params.put("loca", String.valueOf(mDoodle.getDooLoca()));
        params.put("con", String.valueOf(mDoodle.getDooCon()));
        params.put("lat", String.valueOf(mDoodle.getDooLat()));
        params.put("long", String.valueOf(mDoodle.getDooLong()));
        params.put("file", String.valueOf("empty"));  // 임시
        params.put("tag", String.valueOf("empty"));   // 임시

        return params;
    }

    public void setParameter(Doodle doodle) {
        mDoodle = doodle;
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
