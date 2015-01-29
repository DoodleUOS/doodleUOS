package team.udacity.uos.doodle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.activity.DetailViewActivity;
import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.DoodleUploadRequest;

/**
 * Created by include on 2015. 1. 21..
 */
public class DoodleFragment extends Fragment {

    EditText mEditTextLocation;
    EditText mEditTextContext;
    Button mButtonDoodleUpload;

    EditText mEditTextDetailView;
    Button mButtonDetailView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.activity_doodle, container, false);

        mEditTextLocation = (EditText) layout.findViewById(R.id.editTextLocation);
        mEditTextContext = (EditText) layout.findViewById(R.id.editTextContext);

        mButtonDoodleUpload = (Button) layout.findViewById(R.id.buttonDoodleUpload);
        mButtonDoodleUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doodle mDoodle = new Doodle();

                mDoodle.setDooMemNo(1);  // 임시
                mDoodle.setDooLoca(String.valueOf(mEditTextLocation.getText()));
                mDoodle.setDooCon(String.valueOf(mEditTextContext.getText()));
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
                doodleRequest.setParameter(mDoodle);
                VolleyHelper.getRequestQueue().add(doodleRequest);
            }
        });


        // 임시 테스트용
        mEditTextDetailView = (EditText) layout.findViewById(R.id.editTextDetailView);
        mButtonDetailView = (Button) layout.findViewById(R.id.buttonDetailView);
        mButtonDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doodleNoTemp = String.valueOf(mEditTextDetailView.getText());
                if(doodleNoTemp.equals("")){
                    Toast.makeText(getActivity(),"숫자를 입력해주세요(임시 테스트용)",Toast.LENGTH_LONG);
                } else{
                    int doodleNo = Integer.parseInt(doodleNoTemp);

                    Intent mDetailIntent = new Intent(getActivity(), DetailViewActivity.class);
                    mDetailIntent.putExtra("doodleNo", doodleNo);
                    startActivity(mDetailIntent);
                }

            }
        });


        return layout;
    }
}
