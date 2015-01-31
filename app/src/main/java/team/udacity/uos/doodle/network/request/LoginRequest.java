package team.udacity.uos.doodle.network.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import team.udacity.uos.doodle.model.Member;
import team.udacity.uos.doodle.network.Urls;

/**
 * Created by JYPark on 2015. 1. 26..
 */
public class LoginRequest extends BaseRequest{
    Response.Listener<Member> mListener;
    Context mContext;

    private String mId;
    private String mName;
    private String mLink;
    private String mRegid;


    public LoginRequest(Context context, Response.Listener<Member> listener, Response.ErrorListener errorListener) {
        super(context, Urls.SERVER_URL, errorListener);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q","10");
        params.put("fbno", mId);
        params.put("name", mName);
        params.put("fburl", mLink);
        params.put("id", mRegid);
        return params;
    }

    public void setParameter(String id, String name, String link, String regId) {
        mId = id;
        mName = name;
        mLink = link;
        mRegid = regId;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        Member member = new Member();
        try {
            member.setMemNo(Integer.parseInt(response.getJSONObject("data").getString("memNo")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        member.setMemFbNo(mId);
        member.setMemName(mName);
        member.setMemFbUrl(mLink);
        member.setMemId("qwerty");

        mListener.onResponse(member);
    }
}
