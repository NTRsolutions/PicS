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
import com.justdoit.pics.dao.UserList;
import com.justdoit.pics.dao.UserStarCollect;
import com.justdoit.pics.dao.impl.UserImpl;
import com.justdoit.pics.dao.impl.UserListImpl;
import com.justdoit.pics.dao.impl.UserStarCollectImpl;
import com.justdoit.pics.global.App;
import com.justdoit.pics.global.Constant;
import com.justdoit.pics.model.ItemClickHelper;
import com.justdoit.pics.model.NetSingleton;
import com.justdoit.pics.model.PostFormJsonObjRequest;
import com.justdoit.pics.util.NetUtil;
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

    private static final String ARG_PARAM1 = "contenttype";
    private static final String ARG_PARAM2 = "username";
    private static final String ARG_PARAM3 = "userid";



    public static final int RECENT = 1;
    public static final int RELATION = 2;
    public static final int BEST = 3;
    public static final int SEARCH = 4;
    public static final int USERINFO = 5;
    public static final int COLLECT = 6;


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
     * @param contenttype type of fragment.
     * @param username username who display on this list.
     * @param userid userid who display on this list.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(int contenttype,String username,int userid) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, contenttype);
        args.putString(ARG_PARAM2, username);
        args.putInt(ARG_PARAM3,userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contenttype = getArguments().getInt(ARG_PARAM1);
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

        initcontainer(v);
        initHeaderAndFooter(v);
        initSwipeRefreshLayout(v);

    }

    /**
     * 加载container
     * container:RecyclerView id:content_container,显示各种话题列表,
     * @param v View
     */
    private void initcontainer(View v){
        content_container = (RecyclerView)v.findViewById(R.id.content_container);
        content_container.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new mRecyclerViewAdapter(this.getActivity(),contents,this,contenttype);
        content_container.setAdapter(mAdapter);
    }

    /**
     * 加载header,footer组件
     * header:Spinner,选择fragment显示的列表
     * footer:TextView + ImageView,ImagetView id:edit_iv,点击显示发布话题的编辑对话fragment TODO:TextView id:footer_tv,点击跳转至页面顶部并刷新
     *
     * @param v View
     */
    private void initHeaderAndFooter(View v){

        FrameLayout footer = (FrameLayout)v.findViewById(R.id.footer);
        TextView footer_tv = (TextView)v.findViewById(R.id.footer_tv);
        footer_tv.setOnClickListener(this);

        Spinner header_tv = (Spinner)v.findViewById(R.id.header_tv);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.headerarray, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        header_tv.setAdapter(adapter);
        header_tv.setOnItemSelectedListener(this);//加载header,一个Spinner

        ImageView edit_iv = (ImageView)v.findViewById(R.id.edit_iv);
        edit_iv.setOnClickListener(this);//加载页面右下角的话题发布按钮

        //contenttype为COLLECT或者USERINFO时,不显示header,footer组件
        if(contenttype == COLLECT||contenttype == USERINFO){
            footer.setVisibility(View.INVISIBLE);
            footer.setEnabled(false);
            header_tv.setVisibility(View.INVISIBLE);
            header_tv.setEnabled(false);
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
    }

    private void initSwipeRefreshLayout(View v){
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) v.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setChildViews(R.id.content_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 点击事件监听器
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_iv:
                if(App.isLogin()){
                    EditFragment EditFragment = com.justdoit.pics.fragment.EditFragment.newInstance(username,((MainActivity)getActivity()).userinfo.getAvatar());
                    EditFragment.show(this.getActivity().getFragmentManager().beginTransaction(),"EditFragment");
                }
                break;
            case R.id.footer_tv:
                content_container.smoothScrollToPosition(0);
            default:
                break;
        }
    }


    Response.Listener<JSONObject> oklistener;
    Response.ErrorListener errorListener;

    /**
     * 初始化网络listener
     */
    private void initListener(){
        if(contenttype == COLLECT){
            //TODO:用户收藏列表网络返回数据类型监听器
        }else{
            oklistener = new Response.Listener<JSONObject>(){
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
            };

            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), R.string.errormsg, Toast.LENGTH_LONG);
                }
            };
        }
    }

    /**
     * 访问服务器获取数据
     */
    public void getDataFormServer(){
        if(!NetUtil.isNetworkAvailable(getActivity())){
            Toast.makeText(getActivity(),R.string.netstatcmsg,Toast.LENGTH_LONG);
        }
        initListener();
        mSwipeRefreshLayout.setRefreshing(true);
        UserListImpl userlistnetimpl = new UserListImpl();
        userlistnetimpl.getList(getActivity(),getUrl(),oklistener,errorListener);
    }

    /**
     * 根据contenttype获取服务器Url
     * @return Url
     */
    private String getUrl(){
        switch (contenttype){
            case COLLECT:
                return Constant.HOME_URL + Constant.USER_COLLECT_LIST;
            case USERINFO:
                return Constant.HOME_URL + Constant.USER_TOPIC_LIST + "?user_id="+userid;
            case RECENT:
                return Constant.HOME_URL + Constant.RECENT_LIST;
            case RELATION:
                return Constant.HOME_URL + Constant.RELATION_LIST;
            case BEST:
                return Constant.HOME_URL + Constant.BEST_LIST;
        }
        return null;
    }


    @Override
    public void onRefresh() {
        getDataFormServer();
    }

    @Override
    public void onItemClick(final View item, int position, int which) {
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
            item.setEnabled(false);
            Response.Listener oklistener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    ((TextView)item).setText((Integer.parseInt(((TextView) item).getText().toString()) + 1) + "");
                    ((TextView)item).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_18dp, 0, 0, 0);
                    Toast.makeText(getActivity(),R.string.successmsg,Toast.LENGTH_SHORT);
                }
            };
            Response.ErrorListener errorlistener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(),R.string.errormsg,Toast.LENGTH_SHORT);
                    item.setEnabled(true);
                }
            };
            UserStarCollectImpl userstar = new UserStarCollectImpl();
            userstar.Star(getActivity(),contents.get(position).getPk(),oklistener,errorlistener);
        }else if(id == R.id.plus_one_tv){
            item.setEnabled(false);

            Response.Listener oklistener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    ((TextView)item).setText((Integer.parseInt(((TextView)item).getText().toString())+1)+"");
                    ((TextView)item).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_18dp, 0, 0, 0);
                    Toast.makeText(getActivity(),R.string.successmsg,Toast.LENGTH_SHORT).show();
                }
            };
            Response.ErrorListener errorlistener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(),R.string.errormsg,Toast.LENGTH_SHORT).show();
                    item.setEnabled(true);
                }
            };
            UserStarCollectImpl usercollect = new UserStarCollectImpl();
            usercollect.Collect(getActivity(), contents.get(position).getPk(), oklistener, errorlistener);
        }
    }

    @Override
    public void onItemClick(View item, int position, int which, String content) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(contenttype == USERINFO || contenttype == COLLECT){
            return ;
        }
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
