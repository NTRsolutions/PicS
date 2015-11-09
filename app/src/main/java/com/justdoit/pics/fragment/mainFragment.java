package com.justdoit.pics.fragment;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.SpeedyQuickReturnRecyclerViewOnScrollListener;
import com.justdoit.pics.R;
import com.justdoit.pics.adapater.mRecyclerViewAdapter;
import com.justdoit.pics.bean.Content;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainFragment extends Fragment implements OnClickListener {
    //
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private OnFragmentInteractionListener mListener;


    private RecyclerView content_container;

    public mainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment mainFragment.
     */
    public static mainFragment newInstance(String param1) {
        mainFragment fragment = new mainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        content_container = (RecyclerView)v.findViewById(R.id.content_container);
        content_container.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        content_container.setAdapter(new mRecyclerViewAdapter(this.getActivity(),new ArrayList<Content>()));

        TextView footer_tv = (TextView)v.findViewById(R.id.footer_tv);
        ImageView edit_iv = (ImageView)v.findViewById(R.id.edit_iv);
        SpeedyQuickReturnRecyclerViewOnScrollListener scrollListener = new SpeedyQuickReturnRecyclerViewOnScrollListener.Builder(this.getActivity(), QuickReturnViewType.BOTH)
                .header(v.findViewById(R.id.header_tv))
                .footer(v.findViewById(R.id.footer))
                .slideHeaderUpAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_header_up))
                .slideHeaderDownAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_header_down))
                .slideFooterUpAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_footer_up))
                .slideFooterDownAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_footer_down))
                .build();

        content_container.addOnScrollListener(scrollListener);

        edit_iv.setOnClickListener(this);

        return v;
    }

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
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
