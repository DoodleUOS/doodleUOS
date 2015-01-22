package team.udacity.uos.doodle.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import team.udacity.uos.doodle.R;

/**
 * Created by include on 2015. 1. 21..
 */
public class TimelineDoodleActivity extends android.support.v4.app.Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.activity_timelinedoodle, container, false);
        return layout;
    }
}

