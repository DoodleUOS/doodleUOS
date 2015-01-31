package team.udacity.uos.doodle.adapter;

import android.content.Context;
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
import team.udacity.uos.doodle.model.Member;

public class MemberAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Member> mMemberList;

    public MemberAdapter(Context mContext, ArrayList<Member> memberList) {
        this.mContext = mContext;
        this.mMemberList = memberList;
    }

    @Override
    public int getCount() {
        return mMemberList.size();
    }

    @Override
    public Member getItem(int position) {
        return mMemberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addAll(List<Member> list){
        mMemberList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Member member = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_member, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.profilePictureView.setProfileId(member.getMemFbNo());
        holder.name.setText(member.getMemName());

        return view;
    }

    public class ViewHolder {
        @InjectView(R.id.member_img)
        ProfilePictureView profilePictureView;
        @InjectView(R.id.member_name)
        TextView name;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
            profilePictureView.setCropped(true);
        }

    }
}
