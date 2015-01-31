package team.udacity.uos.doodle.network.request;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.network.Urls;
import team.udacity.uos.doodle.util.DBHelper;

/**
 * Created by JYPark88 on 2015-01-28.
 */
public class SyncRequest extends BaseRequest{
    Response.Listener<JSONObject> mListener;
    Context mContext;
    private int mMemNo;


    public SyncRequest(Context context, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(context, Urls.SERVER_URL, errorListener);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q","31");
        params.put("memno", Integer.toString(mMemNo));
        return params;
    }

    public void setParameter(int memNo) {
        mMemNo = memNo;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        List<Doodle> list = new ArrayList<Doodle>();

        Type listType = new TypeToken<ArrayList<Doodle>>(){}.getType();
        try {
            list = new Gson().fromJson(response.getString("data"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final List<Doodle> finalList = list;
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                DBHelper dbHelper = OpenHelperManager.getHelper(mContext, DBHelper.class);

                try {
                    Dao<Doodle, Integer> dao = dbHelper.getDao(Doodle.class);
                    for(Doodle item : finalList){
                        dao.createOrUpdate(item);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                OpenHelperManager.releaseHelper();

                return null;
            }
        }.execute();

        mListener.onResponse(null);
    }

}
