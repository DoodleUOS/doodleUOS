package team.udacity.uos.doodle.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.model.Doodle;

public class SimpleDoodleView extends LinearLayout{

    @InjectView(R.id.simple_doodle_loca)
    TextView locaText;
    @InjectView(R.id.simple_doodle_content)
    TextView contentText;

    Context mContext;

    public SimpleDoodleView(Context context, Doodle doodle) {
        super(context);
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_simple_doodle, this, true);

        ButterKnife.inject(this);

        locaText.setText(doodle.getDooLoca());
        contentText.setText(doodle.getDooCon());
    }
}
