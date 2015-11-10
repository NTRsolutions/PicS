package com.justdoit.pics.adapater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justdoit.pics.R;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.global.App;

import java.lang.reflect.Field;

/**
 * Created by mengwen on 2015/11/10.
 */
public class BriefIntroAdapter extends RecyclerView.Adapter<BriefIntroAdapter.MyViewHolder> {

    private final String TAG = "BriefIntroAdapter";
    private UserInfo userInfo = null;
    private int NUM_ITEM = 0;

    public BriefIntroAdapter() {
        userInfo = new UserInfo();

        Field [] fields = userInfo.getClass().getDeclaredFields();

        NUM_ITEM = fields.length - 4; // 去掉主键，头像;而国家、城市、省合并
    }

    /**
     * 设置user info和更新item数量
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.brief_intro_item, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        switch (position) {
            case 0:
                holder.nameTv.setText(R.string.prompt_username);
                holder.valueTv.setText(App.getUserName());
                break;
            case 1:
                holder.nameTv.setText(R.string.prompt_email);
                holder.valueTv.setText(userInfo.getEmail());
                break;
            case 2:
                holder.nameTv.setText(R.string.sex);
                holder.valueTv.setText(userInfo.getSex());
                break;
            case 3:
                holder.nameTv.setText(R.string.residence);
                holder.valueTv.setText(userInfo.getCountry() + "," + userInfo.getProvince() + "," + userInfo.getCity());
                break;
            case 4:
                holder.nameTv.setText(R.string.birthday);
                holder.valueTv.setText(userInfo.getBirthday() + "");
                break;
            case 5:
                holder.nameTv.setText(R.string.followers);
                holder.valueTv.setText(userInfo.getFollowers_count() + "");
                break;
            case 6:
                holder.nameTv.setText(R.string.grade);
                holder.valueTv.setText(userInfo.getGrade() + "");
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return NUM_ITEM;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView valueTv;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameTv = (TextView) itemView.findViewById(R.id.brief_intro_name);
            valueTv = (TextView) itemView.findViewById(R.id.brief_intro_value);
        }
    }
}
