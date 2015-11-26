package com.justdoit.pics.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.SpeedyQuickReturnRecyclerViewOnScrollListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.justdoit.pics.R;
import com.justdoit.pics.activity.DetialActivity;
import com.justdoit.pics.activity.MainActivity;
import com.justdoit.pics.activity.UserInfoActivity;
import com.justdoit.pics.adapater.mRecyclerViewAdapter;
import com.justdoit.pics.bean.Content;
import com.justdoit.pics.dao.User;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.ItemClickHelper;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.PostFormJsonObjRequest;
import com.justdoit.pics.widget.MultiSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener,ItemClickHelper, AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "username";
    private static final String ARG_PARAM3 = "userid";

    public static final int NO_HEADER = 0;
    public static final int NO_FOOTER = 1;
    public static final int NO_FOOTERANDHEADER = 2 ;
    public static final int NORMAL = 3;

    public static final int RECENT = 1;
    public static final int RELATION = 2;
    public static final int BEST = 3;
    public static final int SEARCH = 4;
    public static final int USERINFO = 5;
    public static final int COLLECT = 6;

    private int type;
    private int contenttype;
    private String username;
    private int userid;


    private RecyclerView content_container;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private mRecyclerViewAdapter mAdapter;
    private ArrayList<Content> contents = new ArrayList<Content>();

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(int type,String username,int userid) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, username);
        args.putInt(ARG_PARAM3,userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
            username = getArguments().getString(ARG_PARAM2);
            userid = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        initView(v);
//        getDataFormServer();
        return v;
    }

    /**
     * 注册组件
     * @param v
     */
    public void initView(View v){
        content_container = (RecyclerView)v.findViewById(R.id.content_container);
        content_container.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new mRecyclerViewAdapter(this.getActivity(),contents,this);
        content_container.setAdapter(mAdapter);

        TextView footer_tv = (TextView)v.findViewById(R.id.footer_tv);
        ImageView edit_iv = (ImageView)v.findViewById(R.id.edit_iv);
        FrameLayout footer = (FrameLayout)v.findViewById(R.id.footer);
        Spinner header_tv = (Spinner)v.findViewById(R.id.header_tv);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.headerarray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        header_tv.setAdapter(adapter);
        header_tv.setOnItemSelectedListener(this);
        //显示header,footer组件
        if(contenttype == COLLECT||contenttype == USERINFO){
            footer.setVisibility(View.INVISIBLE);
            header_tv.setVisibility(View.INVISIBLE);
        }

        //添加header,footer滑动监听器
        SpeedyQuickReturnRecyclerViewOnScrollListener scrollListener = new SpeedyQuickReturnRecyclerViewOnScrollListener.Builder(this.getActivity(), QuickReturnViewType.BOTH)
                .header(header_tv)
                .footer(footer)
                .slideHeaderUpAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_header_up))
                .slideHeaderDownAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_header_down))
                .slideFooterUpAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_footer_up))
                .slideFooterDownAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_footer_down))
                .build();
        content_container.addOnScrollListener(scrollListener);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) v.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setChildViews(R.id.content_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        edit_iv.setOnClickListener(this);
    }


    /**
     * 点击事件监听器
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_iv:
                EditFragment EditFragment = com.justdoit.pics.fragment.EditFragment.newInstance(username,((MainActivity)getActivity()).userinfo.getAvatar());
                EditFragment.show(this.getActivity().getFragmentManager().beginTransaction(),"EditFragment");
                break;
            default:
                break;
        }
    }



    public void getDataFormServer(){
        mSwipeRefreshLayout.setRefreshing(true);
        String url = null;
        switch (contenttype){
            case COLLECT:
                url = "http://demo.gzqichang.com:8001/api/topic/collection/list/";
                break;
            case USERINFO:
                url = Constant.HOME_URL + Constant.USER_TOPIC_LIST + "?user_id="+userid;
                break;
            case RECENT:
                url = "http://demo.gzqichang.com:8001/api/topic/recentlist/";
                break;
            case RELATION:
                url = "http://demo.gzqichang.com:8001/api/topic/relationlist/";
                break;
            case BEST:
                url = "http://demo.gzqichang.com:8001/api/topic/bestlist/";
                break;
        }
        JsonObjectRequest mrequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    JSONArray results = response.getJSONArray("results");
                    Gson mgson = new Gson();
                    mAdapter.mcontents.clear();
                    for(int i =0 ;i<results.length();i++){
                        Content content = mgson.fromJson(String.valueOf(results.get(i)),Content.class);
                        mAdapter.mcontents.add(content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mSwipeRefreshLayout.setRefreshing(false);
                Log.e("test", "error: " + error.toString());
                getDataFormServer();
            }
        });

        RequestQueue mRequest = NetSingleton.getInstance(getActivity()).getRequestQueue();
        mRequest.add(mrequest);

    }

    @Override
    public void onRefresh() {
        getDataFormServer();
    }

    @Override
    public void onItemClick(View item, int position, int which) {
        int id = item.getId();
        if(id == R.id.add_comment_tv || id == R.id.post_iv || id == R.id.message_tv ){
            Intent i = new Intent(getActivity(), DetialActivity.class);
            i.putExtra("pk",contents.get(position).getPk());
            startActivity(i);
        }else if(id == R.id.display_name_tv || id == R.id.user_iv){
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.USER_ID_NAME, contents.get(position).getAuthor().getId());
            bundle.putString(Constant.USERNAME_NAME, contents.get(position).getAuthor().getUsername());
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(id == R.id.collect_tv){
            ((TextView)item).setText((Integer.parseInt(((TextView) item).getText().toString()) + 1) + "");
            ((TextView)item).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_18dp, 0, 0, 0);
            ((TextView)item).setEnabled(false);
            Map<String,String> params = new HashMap<String,String>();
            params.put("topic", contents.get(position).getPk() + "");
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
            params.put("topic", contents.get(position).getPk() + "");
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

    @Override
    public void onItemClick(View item, int position, int which, String content) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String swi = adapterView.getItemAtPosition(i).toString();
        switch (swi){
            case "最近":
                contenttype = RECENT;
                break;
            case "好友":
                contenttype = RELATION;
                break;
            case "精选":
                contenttype = BEST;
                break;
            case "查询":
                contenttype = SEARCH;
                break;
        }

        getDataFormServer();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
