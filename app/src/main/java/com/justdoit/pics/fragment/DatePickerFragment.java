package com.justdoit.pics.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.global.App;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 直接网络请求
 * Created by mengwen on 2015/11/27.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(final DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Map<String, String> params = new HashMap<String, String>();
        params.put("_method", "PUT");
        params.put("birthday", "" + year + "-" + month + "-" + day);
        new UserImpl().changeUserInfo(getContext(), App.getUserId(), params, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Toast.makeText(view.getContext(), "修改成功", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "修改失败", Toast.LENGTH_LONG).show();
                    }
                });
    }
}