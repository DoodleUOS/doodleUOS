package team.udacity.uos.doodle.network.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.network.Urls;
import team.udacity.uos.doodle.util.DBHelper;

/**
 * Created by JYPark on 2015. 1. 26..
 */
public class DoodleUploadRequest extends BaseRequest {
    Response.Listener<Doodle> mListener;
    Context mContext;

    private Doodle mDoodle;
    private String mfileUpload;
    private String mtagFriend;
    private String mImage = "";
    private int mtagFriendNo = 0;

    public DoodleUploadRequest(Context context, Response.Listener<Doodle> listener, Response.ErrorListener errorListener) {
        super(context, Urls.SERVER_URL, errorListener);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", "20");
        params.put("memno", String.valueOf(mDoodle.getDooMemNo()));
        params.put("loca", String.valueOf(mDoodle.getDooLoca()));
        params.put("con", String.valueOf(mDoodle.getDooCon()));
        params.put("lat", String.valueOf(mDoodle.getDooLat()));
        params.put("long", String.valueOf(mDoodle.getDooLong()));
        params.put("file", String.valueOf(mfileUpload));
        params.put("tag", String.valueOf(mtagFriend));
        params.put("uploadfile",String.valueOf(mImage));
        params.put("tagNo", String.valueOf(mtagFriendNo));
        // 친구 태그 추가

        return params;
    }

    // 이미지 X, 태그 X
    public void setParameter(Doodle doodle, String fileUpload, String tagFriend) {
        mDoodle = doodle;
        mfileUpload = fileUpload;
        mtagFriend = tagFriend;
    }

    // 이미지 O, 태그 X
    public void setParameter(Doodle doodle, String fileUpload, String tagFriend, String base64Content) {
        mDoodle = doodle;
        mfileUpload = fileUpload;
        mtagFriend = tagFriend;
        mImage = base64Content;
    }

    // 이미지 X, 태그 O
    public void setParameter(Doodle doodle, String fileUpload, String tagFriend, int tagFriendNo) {
        mDoodle = doodle;
        mfileUpload = fileUpload;
        mtagFriend = tagFriend;
        mtagFriendNo = tagFriendNo;
    }

    // 이미지 O, 태그 O
    public void setParameter(Doodle doodle, String fileUpload, String tagFriend, String base64Content, int tagFriendNo) {
        mDoodle = doodle;
        mfileUpload = fileUpload;
        mtagFriend = tagFriend;
        mImage = base64Content;
        mtagFriendNo = tagFriendNo;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        Doodle doodle = new Doodle();
        try {
            doodle = new Gson().fromJson(response.getString("data"), Doodle.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DBHelper dbHelper = OpenHelperManager.getHelper(mContext, DBHelper.class);

        try {
            Dao<Doodle, Integer> weatherDao = dbHelper.getDao(Doodle.class);
            weatherDao.createOrUpdate(doodle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        OpenHelperManager.releaseHelper();

        mListener.onResponse(doodle);
    }
}
