package com.justdoit.pics.model;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.justdoit.pics.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 表单提交抽象类
 * 包含了文件提交
 * 使用方法：
 * 通过构造方法或setParams()或者setFileParams()方法
 *
 * 传入参数  Map<String, String> mParams
 *          Map<String, String> fileParams   其中前面是参数，后面是文件路径，自动判断MIME类型
 *
 * 另外，提交的文件名格式提交处理时决定:
 *     时间戳 + "." + 文件扩展名
 *
 * Created by mengwen on 2015/10/28.
 */
public abstract class PostFormRequest<T> extends Request<T> {

    private final static String TAG = "PostFormRequest";
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private String BOUNDARY = "----FormBoundary"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";


    private Response.Listener mListener; // 请求正确时调用

    private Map<String, String> mParams; // 请求的参数表

    private Map<String, String> fileParams; // 上传图片文件


    public PostFormRequest(String url, Map<String, String> params, Response.Listener okListener, Response.ErrorListener errorListener) {
        this(url, params, null, okListener, errorListener);
    }

    /**
     * @param url
     * @param params
     * @param fileParams    参数名和文件路径的map;文件名用时间戳
     * @param okListener
     * @param errorListener
     */
    public PostFormRequest(String url, Map<String, String> params, Map<String, String> fileParams, Response.Listener okListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.mListener = okListener;
        this.mParams = params;
        this.BOUNDARY += getTimeStamp();
        this.fileParams = fileParams;
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
     * @param out
     * @param params
     * @param isFileParams
     *   true:params是文件参数
     *   false:params不是文件参数
     */
    private void formParams(OutputStream out, Map<String, String> params, boolean isFileParams) {
        for (Map.Entry<String, String> item : mParams.entrySet()) {
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
                if (mimeTypeMap.hasExtension(item.getValue())) {
                    sb.append("Content-Type:" + mimeTypeMap.getMimeTypeFromExtension(extension));
                    sb.append("\r\n");
                    sb.append("\r\n");
                }

                try {
                    out.write(sb.toString().getBytes(PROTOCOL_CHARSET));

                    // 读取文件bytes
                    out.write(FileUtil.getBytesFromPath(item.getValue()));

                    out.write("\r\n".getBytes(PROTOCOL_CHARSET)); // 添加换行符

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "PostFormRequest formParams() failed");
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
        if (fileParams != null && !fileParams.isEmpty()) {
            formParams(bos, fileParams, true);
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
