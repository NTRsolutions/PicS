package com.justdoit.pics.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.DetailAdapter;
import com.justdoit.pics.adapater.mRecyclerViewAdapter;
import com.justdoit.pics.bean.Comment;
import com.justdoit.pics.bean.Content;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.ItemClickHelper;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.PostFormJsonObjRequest;
import com.justdoit.pics.widget.MultiSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetialActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,ItemClickHelper {

    private static final String ARG_PARAM1 = "pk";

    private RecyclerView content_container;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private DetailAdapter mAdapter;
    private Content content;
    private ArrayList<Comment> comments = new ArrayList<Comment>();

    private int pk = -1;

    public DetialActivityFragment() {
    }
    public static DetialActivityFragment newInstance(int pk) {
        DetialActivityFragment fragment = new DetialActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pk);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pk = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_detial, container, false);
        initView(v);
        getDataFormServer();
        return v;
    }

    private void initView(View v){
        content_container = (RecyclerView)v.findViewById(R.id.content_container);
        content_container.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new DetailAdapter(this.getActivity(),content,comments,this);
        content_container.setAdapter(mAdapter);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) v.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setChildViews(R.id.content_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_detial, menu);
//        if(((App)getActivity().getApplication()).USER_ID != content.getAuthor().getId()){
//            menu.findItem(R.id.action_detele).setVisible(false);
//        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void getDataFormServer(){
        mSwipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest mrequest = new JsonObjectRequest(Constant.HOME_URL + Constant.TOPIC +pk, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    Gson mgson = new Gson();
                    content = mgson.fromJson(String.valueOf(response),Content.class);
                    mAdapter.mcontent = content;
                }finally {
                    getCommentFromServer();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mSwipeRefreshLayout.setRefreshing(false);
                Log.e("test", "error: " + error.toString());
            }
        });

        RequestQueue mRequest = NetSingleton.getInstance(getActivity()).getRequestQueue();
        mRequest.add(mrequest);
    }

    private void getCommentFromServer(){
        JsonObjectRequest mrequest1 = new JsonObjectRequest(Constant.HOME_URL + Constant.COMMENT_LIST +"?id="+pk, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    Gson mgson = new Gson();
                    JSONArray results = response.getJSONArray("results");
                    mAdapter.mcomments.clear();
                    for(int i =0 ;i<results.length();i++){
                        Comment comment = mgson.fromJson(String.valueOf(results.get(i)),Comment.class);
                        mAdapter.mcomments.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mSwipeRefreshLayout.setRefreshing(false);
                Log.e("test", "error: " + error.toString());
            }
        });

        RequestQueue mRequest = NetSingleton.getInstance(getActivity()).getRequestQueue();
        mRequest.add(mrequest1);
    }

    @Override
    public void onRefresh() {
        getDataFormServer();
    }

    @Override
    public void onItemClick(View item, int position, int which) {
        int id = item.getId();
        if(id == R.id.reply_btn){

        }
    }
    public void onItemClick(View item, int position, int which,String content) {
        int id = item.getId();
        if(id == R.id.reply_btn){
            Map<String,String> params = new HashMap<String,String>();
            params.put("content",content);
            params.put("topic",pk+"");
            PostFormJsonObjRequest request = new PostFormJsonObjRequest("http://demo.gzqichang.com:8001/api/topic/comment/create/", params, null, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT);
                    getDataFormServer();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT);
                }
            });
            NetSingleton.getInstance(getActivity()).addToRequestQueue(request);
        }else if(id == R.id.collect_tv){
            ((TextView)item).setText((Integer.parseInt(((TextView) item).getText().toString()) + 1) + "");
            ((TextView)item).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_18dp, 0, 0, 0);
            ((TextView)item).setEnabled(false);
            Map<String,String> params = new HashMap<String,String>();
            params.put("topic", pk + "");
            PostFormJsonObjRequest request = new PostFormJsonObjRequest("http://demo.gzqichang.com:8001/api/topic/collection/create/", params, null, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            NetSingleton.getInstance(getActivity()).addToRequestQueue(request);
        }else if(id == R.id.plus_one_tv){
            ((TextView)item).setText((Integer.parseInt(((TextView)item).getText().toString())+1)+"");
            ((TextView)item).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_18dp, 0, 0, 0);
            ((TextView)item).setEnabled(false);
            Map<String,String> params = new HashMap<String,String>();
            params.put("topic", pk + "");
            PostFormJsonObjRequest request = new PostFormJsonObjRequest("http://demo.gzqichang.com:8001/api/topic/star/create/", params, null, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            NetSingleton.getInstance(getActivity()).addToRequestQueue(request);
        }
    }
}
