package com.justdoit.pics.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.justdoit.pics.R;

/**
 * Created by ljz on 2015/11/3.
 */
public class EditFragment extends DialogFragment {


    public EditFragment() {
        super();
        this.setStyle(STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
    }

    static EditFragment newInstance() {
        return new EditFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        Toolbar mtb = (Toolbar)v.findViewById(R.id.mToolbar);
        mtb.setTitle("test");
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager wm = getActivity().getWindowManager();
        Point mPoint = new Point();
        wm.getDefaultDisplay().getSize(mPoint);


        Window window = getDialog().getWindow();
        window.setLayout(mPoint.x, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public Dialog getDialog() {
        return super.getDialog();
    }
}
