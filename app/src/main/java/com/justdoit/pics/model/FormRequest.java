package com.justdoit.pics.model;

import android.content.Context;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单提交抽象类
 *
 * --注意--
 * 默认的提交方法是post，可以在构造函数设置为其他的提交方式
 * --注意--
 *
 * 包含了文件提交
 * 自动初始化token，initparams()
 * 在header上添加X-CSRFToken
 * 使用方法：
 * 通过构造方法或setParams()或者setFileParams()方法
 * <p>
 * 传入参数  Map<String, String> mParams
 * Map<String, String> fileParams   其中前面是参数，后面是文件路径，自动判断MIME类型
 * <p>
 * 另外，提交的文件名格式提交处理时决定:
 * 时间戳 + "." + 文件扩展名
 * <p>
 * Created by mengwen on 2015/10/28.
 */
public abstract class FormRequest<T> extends Request<T> {

    private final static String TAG = "FormRequest";
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private String BOUNDARY = "----FormBoundary"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public static final String HEADER_TOKEN = "X-CSRFToken";


    private Response.Listener mListener; // 请求正确时调用

    private Map<String, String> mParams = new HashMap<String, String>(); // 请求的参数表

    private Map<String, String> fileParams; // 上传图片文件


    public FormRequest(Context context, String url, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener) {
        this(context, url, params, null, okListener, errorListener);
    }

    /**
     * 默认提交方法为POST
     * @param url
     * @param params
     * @param fileParams    参数名和文件路径的map;文件名用时间戳
     * @param okListener
     * @param errorListener
     */
    public FormRequest(Context context, String url, Map<String, String> params, Map<String, String> fileParams, Response.Listener okListener, Response.ErrorListener errorListener) {
        this(context, Method.POST, url, params, fileParams, okListener, errorListener);
    }

    public FormRequest(Context context, int method, String url, Map<String, String> params, Map<String, String> fileParams, Response.Listener okListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        initParams(context, params);
        this.mListener = okListener;
        this.BOUNDARY += getTimeStamp();
        this.fileParams = fileParams;
    }

    /**
     * 初始化token param
     * 初始化传过来的param
     */
    private void initParams(Context context, Map<String, String> params) {

        this.mParams.put(Constant.FORM_TOKEN_NAME, App.getToken());

        if (params != null) {
            this.mParams.putAll(params);
        }
    }


    public Map<String, String> getFileParams() {
        return fileParams;
    }

    public void setFileParams(Map<String, String> fileParams) {
        this.fileParams = fileParams;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public void setParams(Map<String, String> mParams) {
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

    /**
     * 表单数据提交,包括文件的提交
     *
     * @param out
     * @param params
     * @param isFileParams true:params是文件参数
     *                     false:params不是文件参数
     */
    private void formParams(OutputStream out, Map<String, String> params, boolean isFileParams) {
        for (Map.Entry<String, String> item : params.entrySet()) {
            StringBuilder sb = new StringBuilder();

            sb.append("--" + BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; ");
            sb.append("name=\"" + item.getKey() + "\""); // 加上双引号

            // 如果是文件
            // 添加文件名和MIME类型
            // 首先获取文件的扩展名
            if (isFileParams) {
                String extension = FileUtil.getFileExtensionFromPath(item.getValue());

                sb.append("; filename=\"" + getTimeStamp() + "." + extension + "\"");
                sb.append("\r\n");

                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                sb.append("Content-Type:" + mimeTypeMap.getMimeTypeFromExtension(extension));
                sb.append("\r\n");
                sb.append("\r\n");

                try {
                    out.write(sb.toString().getBytes(PROTOCOL_CHARSET));

                    // 读取文件bytes
                    out.write(FileUtil.getBytesFromPath(item.getValue()));

                    out.write("\r\n".getBytes(PROTOCOL_CHARSET)); // 添加换行符

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "FormRequest formParams() failed");
                }


            } else {
                // 普通参数
                sb.append("\r\n");
                sb.append("\r\n");

                sb.append(item.getValue());
                sb.append("\r\n");

                try {
                    out.write(sb.toString().getBytes(PROTOCOL_CHARSET));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "sb getBytes() error");
                }
            }
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        if (mParams == null || mParams.isEmpty()) {
            return super.getBody();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 参数
        if (mParams != null && !mParams.isEmpty()) {
            formParams(bos, mParams, false);
        }

        // 文件参数
        // 设置上传时间
        if (fileParams != null && !fileParams.isEmpty()) {
            formParams(bos, fileParams, true);
            setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
    public Map<String, String> getHeaders() throws AuthFailureError {
        // 初始化token header
        Map<String, String> headers = new HashMap<String, String>(super.getHeaders());
        headers.put(HEADER_TOKEN, App.getToken());
        return headers;
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
