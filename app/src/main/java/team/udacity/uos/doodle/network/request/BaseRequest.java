package team.udacity.uos.doodle.network.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import team.udacity.uos.doodle.BaseApplication;
import team.udacity.uos.doodle.util.Constants;

public class BaseRequest extends Request<JSONObject> {


    private Response.Listener<JSONObject> mListener;
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public BaseRequest(Context context, String url, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(Constants.VOLLEY_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mSharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mContext = context;

    }

    public BaseRequest(Context context, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(Constants.VOLLEY_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mSharedPreferences = ((BaseApplication) context.getApplicationContext()).getLocalPreference();
        mListener = listener;
        mContext = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>(5);
//        headers.put("X-APP-ID", Constants.OFFICE_APP_ID);
//        headers.put("X-HW-USERID", mSharedPreferences.getString(Constants.PREF_USER_ID, ""));
//        headers.put("X-HW-DOMAIN", mSharedPreferences.getString(Constants.PREF_DOMAIN, ""));
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "utf-8");
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response, true));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
