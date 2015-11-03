package com.justdoit.pics.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justdoit.pics.R;

/**
 * Created by ljz on 2015/11/3.
 */
public class edit_fragment extends DialogFragment {
    public edit_fragment() {
        super();
    }

    static edit_fragment newInstance() {
        return new edit_fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit,container,false);
        return v;
    }

    @Override
    public Dialog getDialog() {
        return super.getDialog();
    }
}
