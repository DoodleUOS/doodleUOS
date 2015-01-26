package team.udacity.uos.doodle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.activity.MapActivity;

/**
 * Created by include on 2015. 1. 21..
 */
public class TimelineDoodleFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_timelinedoodle, container, false);

        Button mButtonMap = (Button) layout.findViewById(R.id.button_map);
        mButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getActivity(),MapActivity.class);
                startActivity(mapIntent);
            }
        });
        return layout;
    }
}

