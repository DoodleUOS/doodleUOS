package team.udacity.uos.doodle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gcm.GCMRegistrar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.model.Member;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.LoginRequest;
import team.udacity.uos.doodle.util.Constants;

public class LoginActivity extends Activity {
    private UiLifecycleHelper uiHelper;

    @InjectView(R.id.login_facebook_btn)
    LoginButton mLoginButton;

    String regId;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) { // 세션이 있을 경우

            if (GCMRegistrar.getRegistrationId(this).equals("")){
                try {
                    //registerDevice();
                    //regId = GCMRegistrar.getRegistrationId(this);

                } catch(Exception ex) {
                    ex.printStackTrace();
                }

            }


            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else { // 아닐 경우

        }

    }

    public void onResume() {
        super.onResume();
        uiHelper.onResume();
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

                        com.android.volley.Response.Listener<Member> listener = new com.android.volley.Response.Listener<Member>() {
                            @Override
                            public void onResponse(Member response) {
                                SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefEditor = prefs.edit();

                                prefEditor.putInt(Constants.USER_NO, response.getMemNo());
                                prefEditor.putString(Constants.USER_FB_NO, response.getMemFbNo());
                                prefEditor.putString(Constants.USER_FB_NAME, response.getMemName());
                                prefEditor.putString(Constants.USER_FB_LINK, response.getMemFbUrl());

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        };

                        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getBaseContext(), "통신상태가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                session.closeAndClearTokenInformation();
                            }
                        };






                        LoginRequest loginRequest = new LoginRequest(LoginActivity.this, listener, errorListener);
                        loginRequest.setParameter(user.getId(), user.getName(), user.getLink(), regId);
                        VolleyHelper.getRequestQueue().add(loginRequest);
                    }
                }
                if (response.getError() != null) {
                    // Handle errors, will do so later.
                }
            }
        });
        request.executeAsync();
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void registerDevice() {

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        regId = GCMRegistrar.getRegistrationId(this);
        Log.i("gcm","registrationId");
        if (regId.equals("")) {

            GCMRegistrar.register(getBaseContext(), BasicInfo.PROJECT_ID);
            Log.i("gcm","registrationId2");

        } else {

            if (GCMRegistrar.isRegisteredOnServer(this)) {

            } else {

                GCMRegistrar.register(getBaseContext(), BasicInfo.PROJECT_ID);

            }


        }
    }

}
