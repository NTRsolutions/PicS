package com.justdoit.pics.model;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 表单提交抽象类
 * 使用方法：
 *   通过构造方法或setmParams()方法
 *   传入参数  Map<String, String> mParams
 * Created by mengwen on 2015/10/28.
 */
public abstract class PostFormRequest<T> extends Request<T> {

    private final static String TAG = "PostFormRequest";
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private String BOUNDARY = "----FormBoundary"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";


    private Response.Listener mListener; // 请求正确时调用

    private Map<String, String> mParams; // 请求的参数表


    public PostFormRequest(String url, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.mListener = okListener;
        this.mParams = params;
        this.BOUNDARY += getTimeStamp();
    }

    public Map<String, String> getmParams() {
        return mParams;
    }

    public void setmParams(Map<String, String> mParams) {
        this.mParams = mParams;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    private String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        if (mParams == null || mParams.isEmpty()) {
            return super.getBody();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();


        for (Map.Entry<String, String> item : mParams.entrySet()) {
            StringBuilder sb = new StringBuilder();

            sb.append("--" + BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; ");
            sb.append("name=\"" + item.getKey() + "\""); // 加上双引号
            sb.append("\r\n");
            sb.append("\r\n");

            sb.append(item.getValue());
            sb.append("\r\n");

            try {
                bos.write(sb.toString().getBytes(PROTOCOL_CHARSET));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "sb getBytes() error");
            }

        }

        String endLine = "--" + BOUNDARY + "--" + "\r\n";
        try {
            bos.write(endLine.toString().getBytes(PROTOCOL_CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "endLine getBytes() error");
        }

        return bos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
    }

    @Override
    protected void deliverResponse(Object response) {
        if (mListener != null) {
            mListener.onResponse(response);
        } else {
            Log.e(TAG, "mListener is null");
        }
    }
}
