package com.justdoit.pics.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.global.Constant;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengwen on 2015/11/27.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Map<String, String> params = new HashMap<String, String>();
        params.put("_method", "PUT");
        params.put("birthday", "");
//        new UserImpl().changeUserInfo();
    }
}