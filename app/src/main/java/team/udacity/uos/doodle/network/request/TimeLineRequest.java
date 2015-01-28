package team.udacity.uos.doodle.network.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.udacity.uos.doodle.model.Member;
import team.udacity.uos.doodle.model.TimeLine;
import team.udacity.uos.doodle.network.Urls;

/**
 * Created by JYPark88 on 2015-01-28.
 */
public class TimeLineRequest extends BaseRequest{
    Response.Listener<List<TimeLine>> mListener;
    Context mContext;
    private int mMemNo;


    public TimeLineRequest(Context context, Response.Listener<List<TimeLine>> listener, Response.ErrorListener errorListener) {
        super(context, Urls.SERVER_URL, errorListener);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q","30");
//        params.put("memno", Integer.toString(mMemNo));
        params.put("memno", "2");
        return params;
    }

    public void setParameter(int memNo) {
        mMemNo = memNo;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        List<TimeLine> list = new ArrayList<TimeLine>();

        Type listType = new TypeToken<ArrayList<TimeLine>>(){}.getType();
        try {
            list = new Gson().fromJson(response.getString("data"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mListener.onResponse(list);
    }

}
