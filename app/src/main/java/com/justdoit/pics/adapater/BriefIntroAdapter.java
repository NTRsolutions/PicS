package com.justdoit.pics.adapater;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justdoit.pics.R;
import com.justdoit.pics.bean.UserInfo;
import com.justdoit.pics.global.App;
import com.justdoit.pics.widget.PersonalIntroItemView;

/**
 * Created by mengwen on 2015/11/10.
 */
public class BriefIntroAdapter extends RecyclerView.Adapter {

    private final String TAG = "BriefIntroAdapter";
    private UserInfo userInfo = null;
    private boolean isUserOwn = true;
    private int NUM_ITEM = 0;
    private Context context;

    // item类型
    private enum ITEM_TYPE {
        ITEM_PERSONAL_INTRO, // 基本信息
        ITEM_CONNECTIONS // 人脉
    }

    public BriefIntroAdapter(Context context) {
        this(context, true);
    }

    public BriefIntroAdapter(Context context, boolean isUserOwn) {

        this.isUserOwn = isUserOwn;
        this.context = context;
        userInfo = new UserInfo();
        NUM_ITEM = 2; // 基本信息和人脉
    }

    /**
     * 设置user info和更新item数量
     *
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        if (viewType == ITEM_TYPE.ITEM_PERSONAL_INTRO.ordinal()) {
            holder = new PersonalHolder((LayoutInflater.from(parent.getContext())).inflate(R.layout.brief_intro_personal, parent, false));
        } else {
            holder = new ConnectionsHolder((LayoutInflater.from(parent.getContext())).inflate(R.layout.brief_intro_connections, parent, false));
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Resources res = context.getResources();

        if (holder instanceof PersonalHolder) {
            // 名称 + 值 + 可否编辑(Y/N)
            // 昵称 Y
            ((PersonalHolder) holder).nicknamePIV.update(res.getString(R.string.nick_name), userInfo.getNickname(), true);
            // email Y
            ((PersonalHolder) holder).emailPIV.update(res.getString(R.string.prompt_email), userInfo.getEmail(), true);
            // 性别 Y
            if ("1".equals(userInfo.getSex())) {
                ((PersonalHolder) holder).sexPIV.update(res.getString(R.string.sex), res.getString(R.string.man), true);
            } else if ("0".equals(userInfo.getSex())) {
                ((PersonalHolder) holder).sexPIV.update(res.getString(R.string.sex), res.getString(R.string.female), true);
            } else {
                ((PersonalHolder) holder).sexPIV.update(res.getString(R.string.sex), res.getString(R.string.unknown), true);
            }
            // 居住地 Y
            ((PersonalHolder) holder).locationPIV.update(res.getString(R.string.user_info_location), userInfo.getCountry() + " " + userInfo.getProvince() + " " + userInfo.getCity(), true);
            // 生日 Y TODO:没有进行时间格式转换
            if (userInfo.getBirthday() == null) {
                ((PersonalHolder) holder).birthdayPIV.update(res.getString(R.string.birthday), "", true);
            } else {
                ((PersonalHolder) holder).birthdayPIV.update(res.getString(R.string.birthday), String.valueOf(userInfo.getBirthday()), true);
            }

        } else if (holder instanceof ConnectionsHolder) {

        }

    }

    @Override
    public int getItemViewType(int position) {
        // 根据位置返回item类型
        // 默认返回个人基本信息类型
        switch (position) {
            case 0:
                return ITEM_TYPE.ITEM_PERSONAL_INTRO.ordinal();
            case 1:
                return ITEM_TYPE.ITEM_CONNECTIONS.ordinal();
            default:
                return ITEM_TYPE.ITEM_PERSONAL_INTRO.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_ITEM;
    }

    // 人脉view holder
    class ConnectionsHolder extends RecyclerView.ViewHolder {

        TextView titleTv;

        public ConnectionsHolder(View itemView) {
            super(itemView);

            titleTv = (TextView) itemView.findViewById(R.id.brief_connections_intro_title);
        }
    }

    // 个人基本信息view holder
    class PersonalHolder extends RecyclerView.ViewHolder {

        TextView titleTv; // 标题

        PersonalIntroItemView sexPIV; // 性别栏
        PersonalIntroItemView birthdayPIV; // 生日栏
        PersonalIntroItemView locationPIV; // 居住地址
        PersonalIntroItemView emailPIV; // 邮箱
        PersonalIntroItemView nicknamePIV; // 用户名

        public PersonalHolder(View itemView) {
            super(itemView);

            titleTv = (TextView) itemView.findViewById(R.id.brief_personal_intro_title);
            sexPIV = (PersonalIntroItemView) itemView.findViewById(R.id.personal_intro_sex);
            birthdayPIV = (PersonalIntroItemView) itemView.findViewById(R.id.personal_intro_birthday);
            locationPIV = (PersonalIntroItemView) itemView.findViewById(R.id.personal_intro_location);
            emailPIV = (PersonalIntroItemView) itemView.findViewById(R.id.personal_intro_email);
            nicknamePIV = (PersonalIntroItemView) itemView.findViewById(R.id.personal_intro_nickname);
        }
    }
}
