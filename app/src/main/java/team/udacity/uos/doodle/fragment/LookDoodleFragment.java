package team.udacity.uos.doodle.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.InjectView;
import team.udacity.uos.doodle.R;

/**
 * Created by include on 2015. 1. 21..
 */
public class LookDoodleFragment extends Fragment {

    @InjectView(R.id.no_doodle_text)
    TextView noDoodleText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_lookdoodle, container, false);

        return layout;
    }
}