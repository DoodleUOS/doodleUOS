package team.udacity.uos.doodle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.adapter.MemberAdapter;
import team.udacity.uos.doodle.model.Member;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.MemberRequest;
import team.udacity.uos.doodle.util.Constants;

/**
 * Created by JYPark on 2015. 1. 31..
 */
public class MemberActivity extends ActionBarActivity {

    @InjectView(R.id.listview_member)
    ListView mListView;

    MemberAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        ButterKnife.inject(this);

        mAdapter = new MemberAdapter(this, new ArrayList<Member>());
        mListView.setAdapter(mAdapter);

        Response.Listener<List<Member>> listener = new Response.Listener<List<Member>>() {
            @Override
            public void onResponse(List<Member> response) {
                mAdapter.addAll(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        MemberRequest memberRequest = new MemberRequest(this, listener, errorListener);
        memberRequest.setParameter(prefs.getInt(Constants.USER_NO, 0));
        VolleyHelper.getRequestQueue().add(memberRequest);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = mAdapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra(Constants.MEMBER_NO, member.getMemNo());
                intent.putExtra(Constants.MEMBER_NAME, member.getMemName());
                intent.putExtra(Constants.MEMBER_FB_ID, member.getMemFbNo());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
