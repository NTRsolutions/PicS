package com.justdoit.pics.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justdoit.pics.R;

/**
 * 用户信息页面->简介页面->基本信息的一个组合view
 * Created by mengwen on 2015/11/12.
 */
public class PersonalIntroItemView extends RelativeLayout {

    private TextView titleTv; // 标题
    private TextView contentTv; // 内容
    private ImageView editIv; // 修改图片
    private boolean editable = true; // 是否能够修改信息,默认可以编辑

    public PersonalIntroItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonalIntroItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // attach to root 必须为true
        LayoutInflater.from(context).inflate(R.layout.personal_intro_view_item, this, true);

        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
        editIv = (ImageView) findViewById(R.id.edit_iv);

        titleTv.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG); // 加粗字体
    }

    /**
     * 更新view
     * @param title 标题
     * @param content 内容
     * @param editable 是否可编辑，true:显示编辑图片，false:隐藏编辑图片
     */
    public void update(String title, String content, boolean editable) {
        this.editable = editable;

        // 是否可编辑，改变编辑图片是否要显示
        if (!isEditable()) {
            editIv.setVisibility(View.INVISIBLE);
        } else {
            editIv.setVisibility(View.VISIBLE);
        }

        titleTv.setText(title);
        contentTv.setText(content);

    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
