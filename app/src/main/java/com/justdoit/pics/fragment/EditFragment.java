package com.justdoit.pics.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.justdoit.pics.R;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.FormJsonObjRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljz on 2015/11/3.
 */
public class EditFragment extends DialogFragment implements View.OnClickListener {

    String username ;
    String useravatar_url;
    String imagepath;
    private NetworkImageView avatar_iv;
    private TextView username_tv;
    private ImageView send_iv;
    private EditText content_et;
    private ImageView photo_iv;
    private ImageView position_iv;

    private static final int CHOOSE_CODE = 0;

    public EditFragment() {
        super();
    }

    static EditFragment newInstance(String username,String useravatar_url) {
        EditFragment instance = new EditFragment();
        Bundle b = new Bundle();
        b.putString("username",username);
        b.putString("useravatar_url",useravatar_url);
        instance.setArguments(b);
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        avatar_iv = (NetworkImageView)v.findViewById(R.id.avatar_iv);
        username_tv = (TextView)v.findViewById(R.id.username_tv);
        send_iv = (ImageView)v.findViewById(R.id.send_iv);
        content_et = (EditText)v.findViewById(R.id.content_et);
        photo_iv = (ImageView)v.findViewById(R.id.photo_iv);
        position_iv = (ImageView)v.findViewById(R.id.position_iv);

        username_tv.setText(username);
        avatar_iv.setImageUrl(useravatar_url, NetSingleton.getInstance(this.getActivity()).getImageLoader());
        avatar_iv.setDefaultImageResId(R.mipmap.ic_launcher);

        photo_iv.setOnClickListener(this);
        send_iv.setOnClickListener(this);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            username = getArguments().getString("username");
            useravatar_url = getArguments().getString("useravatar_url");
        }
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.photo_iv:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i,CHOOSE_CODE);
                break;
            case R.id.send_iv:
                if(imagepath==null){
                    break;
                }
                Map<String,String> params = new HashMap<String,String>();
                params.put("title","test");
                params.put("article",content_et.getText().toString());
                params.put("article_status","1");
                Map<String,String> fileparams = new HashMap<String,String>();
                fileparams.put("cover_image",imagepath);
                FormJsonObjRequest request = new FormJsonObjRequest(
                        this.getActivity(),
                        Constant.HOME_URL + Constant.CREATE_TOPIC,
                        params, fileparams, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        MainFragment  mainFragment= (MainFragment)((AppCompatActivity)getActivity()).getSupportFragmentManager().findFragmentByTag("MainFragment");
                        mainFragment.getDataFormServer();
                        dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT);
                        dismiss();
                    }
                });
                NetSingleton.getInstance(getActivity()).addToRequestQueue(request);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_CODE:
                if(resultCode == Activity.RESULT_OK){
                    photo_iv.setImageURI(data.getData());

                    String[] proj = { MediaStore.Images.Media.DATA };
                    CursorLoader mCursorLoader = new CursorLoader(getActivity(),data.getData(), proj, null, null, null);
                    Cursor c = mCursorLoader.loadInBackground();

                    int actual_image_column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    c.moveToFirst();
                    imagepath = c.getString(actual_image_column_index);
                    c.close();
                }
                break;

        }
    }
}
