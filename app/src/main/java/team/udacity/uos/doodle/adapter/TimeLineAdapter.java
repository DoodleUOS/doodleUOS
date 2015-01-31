package team.udacity.uos.doodle.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.model.TimeLine;
import team.udacity.uos.doodle.util.Constants;

public class TimeLineAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TimeLine> mTimeLineList;

    public TimeLineAdapter(Context mContext, ArrayList<TimeLine> mTimeLineList) {
        this.mContext = mContext;
        this.mTimeLineList = mTimeLineList;
    }

    @Override
    public int getCount() {
        return mTimeLineList.size();
    }

    @Override
    public TimeLine getItem(int position) {
        return mTimeLineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addAll(List<TimeLine> list){
        mTimeLineList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeLine timeline = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_timeline, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int myNo = prefs.getInt(Constants.USER_NO, 0);
        // 자료가 없어서 인행이껄로 잠시 대체
        String messege = "";

        if (timeline.getDooMemNo() == myNo){
            if(timeline.getTagTar() != 0 ){
                messege = String.format("%s 에서 %s 님에게 낙서를 남겼습니다.", timeline.getDooLoca(), timeline.getMemNameT());
            } else {
                messege = String.format("%s 에 낙서를 남겼습니다.", timeline.getDooLoca());
            }
            holder.profilePictureView.setProfileId(timeline.getMemFbNo());
        } else {
            if(timeline.getTagTar() == myNo){
                messege = String.format("%s 님이 %s 에서 나에게 낙서를 남겼습니다." ,timeline.getMemNameT() ,timeline.getDooLoca());
                holder.profilePictureView.setProfileId(timeline.getMemFbNoT());
            }
        }
        holder.message.setText(messege);
        holder.date.setText(timeline.getDooDate());

        return view;
    }

    public class ViewHolder {
        public TimeLine timeline;
        @InjectView(R.id.timeline_img)
        ProfilePictureView profilePictureView;
        @InjectView(R.id.timeline_messege)
        TextView message;
        @InjectView(R.id.timeline_date)
        TextView date;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
            profilePictureView.setCropped(true);
        }

    }
}
