package team.udacity.uos.doodle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.model.Member;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.LoginRequest;
import team.udacity.uos.doodle.network.request.SyncRequest;
import team.udacity.uos.doodle.util.Constants;


public class LoginActivity extends Activity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private UiLifecycleHelper uiHelper;

    String SENDER_ID = "6764424943751";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();

    @InjectView(R.id.login_facebook_btn)
    LoginButton mLoginButton;

    String regId = "qwerty";
    Context mcontext;
    String myId;
    String myName;
    String myLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        ButterKnife.inject(this);
        mcontext = getApplicationContext();

        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) { // 세션이 있을 경우




            SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            syncDoodle(prefs.getInt(Constants.USER_NO, 0));

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else { // 아닐 경우

        }

    }

    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        checkPlayServices();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            makeMeRequest(session);
        }
    }

    private void makeMeRequest(final Session session) {

        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (session == Session.getActiveSession()) {
                    if (user != null) {

                        myId = user.getId();
                        myName = user.getName();
                        myLink = user.getLink();

//                        registerInBackground();
                        /******GCM기기체크*******/

                        if(checkPlayServices()){


                            gcm = GoogleCloudMessaging.getInstance(mcontext);
                            regId = getRegistrationId(mcontext);
                            if(regId.isEmpty()){
                                Log.i("gom","registerinbackground()");
                                registerInBackground();
                            }
                        } else {
                            Log.i("gcm","No calid Google Play Services APK found");
                        }
//
                    }
                }
                if (response.getError() != null) {
                    // Handle errors, will do so later.
                }
            }
        });
        request.executeAsync();
    }

    private void syncDoodle(int memNo){

        com.android.volley.Response.Listener<JSONObject> listener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        SyncRequest syncRequest = new SyncRequest(LoginActivity.this, listener, errorListener);
        syncRequest.setParameter(memNo);
        VolleyHelper.getRequestQueue().add(syncRequest);
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private boolean checkPlayServices(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {
                Log.i("gcm","this device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID,"");
        if(registrationId.isEmpty()){
            Log.i("gcm","Registration not found");
            return "";
        }


        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion){
            Log.i("gcm","App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context){
        return getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context){
        try{
            PackageInfo packageInfo = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch(PackageManager.NameNotFoundException e){
            throw new RuntimeException("Could not get package name : " + e);
        }
    }

    public void registerInBackground(){
        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... params) {
                String regID = null;
                try {

                    Log.i("gom","gcm = " + gcm);
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(mcontext);
                    }
                    regID = gcm.register(SENDER_ID);
                    Log.i("gom","regId = " + regID);
                } catch (IOException ex){
                    ex.printStackTrace();
                }
                return regID;
            }

            @Override
            protected void onPostExecute(String result) {
                com.android.volley.Response.Listener<Member> listener = new com.android.volley.Response.Listener<Member>() {
                    @Override
                    public void onResponse(Member response) {
                        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefEditor = prefs.edit();

                        prefEditor.putInt(Constants.USER_NO, response.getMemNo());
                        prefEditor.putString(Constants.USER_FB_NO, response.getMemFbNo());
                        prefEditor.putString(Constants.USER_FB_NAME, response.getMemName());
                        prefEditor.putString(Constants.USER_FB_LINK, response.getMemFbUrl());
                        prefEditor.apply();

                        syncDoodle(response.getMemNo());

                    }
                };

                com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "통신상태가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        Session.getActiveSession().closeAndClearTokenInformation();
                    }
                };

                LoginRequest loginRequest = new LoginRequest(LoginActivity.this, listener, errorListener);
                loginRequest.setParameter(myId, myName, myLink, result);
                VolleyHelper.getRequestQueue().add(loginRequest);
            }
        }.execute();
    }

    private void storeRegistrationId(Context context, String regId){
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("gcm","Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }




}
