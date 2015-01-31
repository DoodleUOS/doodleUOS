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
import team.udacity.uos.doodle.network.Urls;

/**
 * Created by JYPark88 on 2015-01-28.
 */
public class MemberRequest extends BaseRequest{
    Response.Listener<List<Member>> mListener;
    Context mContext;
    private int mMemNo;

    public MemberRequest(Context context, Response.Listener<List<Member>> listener, Response.ErrorListener errorListener) {
        super(context, Urls.SERVER_URL, errorListener);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q","12");
        params.put("memno", String.valueOf(mMemNo));

        return params;
    }

    public void setParameter(int memNo) {
        mMemNo = memNo;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        List<Member> list = new ArrayList<Member>();

        Type listType = new TypeToken<ArrayList<Member>>(){}.getType();
        try {
            list = new Gson().fromJson(response.getString("data"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mListener.onResponse(list);
    }

}
