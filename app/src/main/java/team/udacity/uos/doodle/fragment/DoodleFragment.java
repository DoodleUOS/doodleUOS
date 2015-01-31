package team.udacity.uos.doodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.activity.DetailViewActivity;
import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.DoodleUploadRequest;
import team.udacity.uos.doodle.util.Constants;
import team.udacity.uos.doodle.util.DBHelper;

/**
 * Created by include on 2015. 1. 21..
 */
public class DoodleFragment extends Fragment {

    @InjectView(R.id.editTextLocation)
    EditText mEditTextLocation;
    @InjectView(R.id.editTextContext)
    EditText mEditTextContext;
    @InjectView(R.id.buttonDoodleUpload)
    ImageView mButtonDoodleUpload;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_doodle, container, false);

        ButterKnife.inject(this, layout);

        mButtonDoodleUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mLocation = String.valueOf(mEditTextLocation.getText());
                String mContext = String.valueOf(mEditTextContext.getText());

                if(mLocation.length() == 0 || mContext.length() == 0){
                    Toast.makeText(getActivity(), "장소와 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();

                    return;
                }

                SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                Doodle mDoodle = new Doodle();
                mDoodle.setDooMemNo(prefs.getInt(Constants.USER_NO, -1));  // 임시
                mDoodle.setDooLoca(mLocation);
                mDoodle.setDooCon(mContext);
                mDoodle.setDooLat(37.570267);    // 임시
                mDoodle.setDooLong(126.987517);   // 임시

                // 메모 하나 받아오는 통신
                Response.Listener<Doodle> listener = new Response.Listener<Doodle>() {
                    @Override
                    public void onResponse(Doodle response) {
                        if(response.getDooNo() == 0){
                            Toast.makeText(getActivity(), "[실패]", Toast.LENGTH_SHORT).show();
                            return;
                        } else{
                            Toast.makeText(getActivity(), "[성공] 글번호 : " + response.getDooNo(), Toast.LENGTH_SHORT).show();


                            new AsyncTask<Void, Void, List<Doodle>>(){
                                @Override
                                protected List<Doodle> doInBackground(Void... params) {
                                    DBHelper dbHelper = OpenHelperManager.getHelper(getActivity(), DBHelper.class);
                                    List<Doodle> items = null;
                                    try {
                                        final Dao<Doodle, Long> dao = dbHelper.getDao(Doodle.class);
                                        items = dao.queryForAll();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    OpenHelperManager.releaseHelper();
                                    return items;
                                }

                                @Override
                                protected void onPostExecute(List<Doodle> doodles) {
                                    // 여기서 하고싶은거 하면 됨
                                    Log.i("Help222","Database size : " + doodles.size());
                                    Log.i("MyTag","Database size : " + doodles.size());
                                }
                            }.execute();

                            Intent mDetailIntent = new Intent(getActivity(), DetailViewActivity.class);
                            mDetailIntent.putExtra("doodleNo", response.getDooNo());
                            startActivity(mDetailIntent);
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "통신상태가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                };

                DoodleUploadRequest doodleRequest = new DoodleUploadRequest(getActivity(), listener, errorListener);
                doodleRequest.setParameter(mDoodle, "empty", "empty");      // 임시
                VolleyHelper.getRequestQueue().add(doodleRequest);
            }
        });

        return layout;
    }
}
