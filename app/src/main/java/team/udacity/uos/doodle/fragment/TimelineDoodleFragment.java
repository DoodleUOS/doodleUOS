package team.udacity.uos.doodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.activity.MapActivity;
import team.udacity.uos.doodle.adapter.TimeLineAdapter;
import team.udacity.uos.doodle.model.TimeLine;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.TimeLineRequest;
import team.udacity.uos.doodle.util.Constants;

/**
 * Created by include on 2015. 1. 21..
 */
public class TimelineDoodleFragment extends Fragment {

    @InjectView(R.id.timeline_listView)
    ListView mListView;
    @InjectView(R.id.button_map)
    Button mButtonMap;
    @InjectView(R.id.button_map2)
    Button mButtonMap2;

    TimeLineAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_timelinedoodle, container, false);

        ButterKnife.inject(this, layout);
        init();

        return layout;
    }

    private void init() {
        mAdapter = new TimeLineAdapter(getActivity(), new ArrayList<TimeLine>());
        mListView.setAdapter(mAdapter);

        Response.Listener<List<TimeLine>> listener = new Response.Listener<List<TimeLine>>() {
            @Override
            public void onResponse(List<TimeLine> response) {
                mAdapter.addAll(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        TimeLineRequest timeLineRequest = new TimeLineRequest(getActivity(), listener, errorListener);
        timeLineRequest.setParameter(prefs.getInt(Constants.USER_NO, -1));
        VolleyHelper.getRequestQueue().add(timeLineRequest);

        mButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                mapIntent.putExtra("lat",0);
                mapIntent.putExtra("long",0);
                startActivity(mapIntent);
            }
        });
        mButtonMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                mapIntent.putExtra("lat",37.58342);
                mapIntent.putExtra("long",127.054958);
                startActivity(mapIntent);
            }
        });
    }
}

