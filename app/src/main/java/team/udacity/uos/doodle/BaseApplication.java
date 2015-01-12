package team.udacity.uos.doodle;

import android.app.Application;
import android.content.SharedPreferences;

import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.util.Constants;

public class BaseApplication extends Application{

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);

        VolleyHelper.init(this);
    }

    public SharedPreferences getLocalPreference() {
        return mSharedPreferences;
    }
}
