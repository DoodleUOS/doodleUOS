package team.udacity.uos.doodle.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.DetailViewRequest;

public class DetailViewActivity extends ActionBarActivity {

    @InjectView(R.id.imageViewDetailContext)
    ImageView mImageViewDetailContext;
    @InjectView(R.id.textViewDetailLocation)
    TextView mTextViewDetailLocation;
    @InjectView(R.id.imageViewDetailAuth)
    ImageView mImageViewDetailAuth;
    @InjectView(R.id.textViewDetailDate)
    TextView mTextViewDetailDate;
    @InjectView(R.id.textViewDetailAuth)
    TextView mTextViewDetailAuth;
    @InjectView(R.id.textViewDetailContext)
    TextView mTextViewDetailContext;

    private int doodleNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        doodleNo = bundle.getInt("doodleNo");

        // 메모 하나 받아오는 통신
        Response.Listener<Doodle> listener = new Response.Listener<Doodle>() {
            @Override
            public void onResponse(Doodle response) {
                if(response.getDooNo() == 0){
                    Toast.makeText(getBaseContext(), "[실패]", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getBaseContext(), "[성공] 글번호 : " + response.getDooNo() + " 작성자번호 : " + response.getDooMemNo() + " 글 내용 : " + response.getDooCon()
                        + " 작성자 이름 : " + response.getMemName(), Toast.LENGTH_SHORT).show();
                mTextViewDetailLocation.setText(response.getDooLoca());
                mTextViewDetailAuth.setText(response.getMemName());
                mTextViewDetailContext.setText(response.getDooCon());
                mTextViewDetailDate.setText(response.getDooDate());
                if(response.getDooUrl().length() != 0){
                    mImageViewDetailContext.setVisibility(View.VISIBLE);
                    Glide.with(DetailViewActivity.this).load("http://codingsroom.com/doodleuos/upload/" + response.getDooUrl()).into(mImageViewDetailContext);
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "통신상태가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        DetailViewRequest detailRequest = new DetailViewRequest(DetailViewActivity.this, listener, errorListener);
        detailRequest.setParameter(doodleNo);
        VolleyHelper.getRequestQueue().add(detailRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
