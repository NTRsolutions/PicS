package com.justdoit.pics.fragment;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.SpeedyQuickReturnRecyclerViewOnScrollListener;
import com.justdoit.pics.R;
import com.justdoit.pics.activity.MainActivity;
import com.justdoit.pics.adapater.mRecyclerViewAdapter;
import com.justdoit.pics.bean.Content;
import com.justdoit.pics.widget.MultiSwipeRefreshLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnClickListener {

    private static final String ARG_PARAM1 = "type";
    public static final int NO_HEADER = 0;
    public static final int NO_FOOTER = 1;
    public static final int NO_FOOTERANDHEADER = 2 ;
    public static final int NORMAL = 3;
    private int type;



    private RecyclerView content_container;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

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
    public static MainFragment newInstance(int type) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        initView(v);

        return v;
    }

    /**
     * 注册组件
     * @param v
     */
    public void initView(View v){
        content_container = (RecyclerView)v.findViewById(R.id.content_container);
        content_container.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        content_container.setAdapter(new mRecyclerViewAdapter(this.getActivity(),new ArrayList<Content>()));

        TextView footer_tv = (TextView)v.findViewById(R.id.footer_tv);
        ImageView edit_iv = (ImageView)v.findViewById(R.id.edit_iv);
        FrameLayout footer = (FrameLayout)v.findViewById(R.id.footer);
        View header_tv = v.findViewById(R.id.header_tv);

        //显示header,footer组件
        if(type != NORMAL){
            if(type == NO_FOOTER ||type == NO_FOOTERANDHEADER){
                footer.setVisibility(View.INVISIBLE);
            }
            if(type == NO_HEADER ||type == NO_FOOTERANDHEADER){
                header_tv.setVisibility(View.INVISIBLE);
            }
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
                EditFragment EditFragment = com.justdoit.pics.fragment.EditFragment.newInstance();
                EditFragment.show(this.getActivity().getFragmentManager().beginTransaction(),"EditFragment");
                break;
            default:
                break;
        }
    }

}
