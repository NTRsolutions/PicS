package com.justdoit.pics.adapater;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justdoit.pics.R;
import com.justdoit.pics.bean.Content;

import java.util.ArrayList;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ljz on 2015/10/29.
 */
public class mRecyclerViewAdapter extends RecyclerView.Adapter {
    int[] testphotodata = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,
            R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o};

    private int lastPosition = -1;
    private Context mcontext;
    private ArrayList<Content> mcontents;

    public mRecyclerViewAdapter(Context context ,ArrayList<Content> contents){
        mcontext = context;
        mcontents = contents;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_cardview,parent,false);

        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((mViewHolder)holder).mDisplayNameTextView.setText("test");
        ((mViewHolder)holder).mTimestampTextView.setText("test");
        ((mViewHolder)holder).mMessageTextView.setText("test");
        ((mViewHolder)holder).mPlusOneView.setText("10");

        ((mViewHolder)holder).mUserImageView.setImageResource(testphotodata[position]);
        ((mViewHolder)holder).mPostImageView.setImageResource(testphotodata[position]);


        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
        }
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return testphotodata.length;
    }


    class mViewHolder extends RecyclerView.ViewHolder{
        ImageView mUserImageView;
        TextView mDisplayNameTextView;
        TextView mPlusOneView;
        TextView mAddCommentTextView;
        TextView mTimestampTextView;
        TextView mMessageTextView;
        ImageView mPostImageView;
        public mViewHolder(View V){
            super(V);
            mUserImageView = (ImageView)V.findViewById(R.id.user_iv) ;
            mDisplayNameTextView = (TextView)V.findViewById(R.id.display_name_tv) ;
            mPlusOneView = (TextView)V.findViewById(R.id.plus_one_tv);
            mAddCommentTextView = (TextView)V.findViewById(R.id.add_comment_tv);
            mTimestampTextView = (TextView)V.findViewById(R.id.timestamp_tv);
            mMessageTextView = (TextView)V.findViewById(R.id.message_tv);
            mPostImageView = (ImageView)V.findViewById(R.id.post_iv);
        }
    }
}
