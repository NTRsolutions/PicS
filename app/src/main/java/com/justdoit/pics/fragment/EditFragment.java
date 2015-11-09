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
public class EditFragment extends DialogFragment {
    public EditFragment() {
        super();
    }

    static EditFragment newInstance() {
        return new EditFragment();
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
