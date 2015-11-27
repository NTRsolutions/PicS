package com.justdoit.pics.adapater;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.justdoit.pics.R;
import com.justdoit.pics.bean.Content;
import com.justdoit.pics.fragment.MainFragment;
import com.justdoit.pics.model.ItemClickHelper;
import com.justdoit.pics.model.LruBitmapCache;
import com.justdoit.pics.model.NetSingleton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ljz on 2015/10/29.
 */
public class mRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private int lastPosition = -1;
    private Context mcontext;
    public ArrayList<Content> mcontents;
    private static final int TOP = 0;
    private static final int NORMAL = 1;
    private int type = 0;

    ItemClickHelper mcallback;
    ImageLoader imageLoader ;

    public mRecyclerViewAdapter(Context context ,ArrayList<Content> contents , ItemClickHelper callback){
        mcontext = context;
        mcontents = contents;
        imageLoader = NetSingleton.getInstance(context).getImageLoader();
        mcallback = callback;
    }

    public mRecyclerViewAdapter(Context context ,ArrayList<Content> contents , ItemClickHelper callback,int type){
        mcontext = context;
        mcontents = contents;
        imageLoader = NetSingleton.getInstance(context).getImageLoader();
        mcallback = callback;
        this.type = type;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == NORMAL){
            View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_cardview,parent,false);
            return new mViewHolder(v);
        }else{
            ImageView topview = new ImageView(mcontext);
            topview.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
            return new mViewHolder(topview);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int mposition) {

        if(mposition!=0) {
            final int position = mposition - 1;
            ((mViewHolder) holder).mDisplayNameTextView.setText(mcontents.get(position).getAuthor().getUsername());
            ((mViewHolder) holder).mTimestampTextView.setText(mcontents.get(position).getUpdate_time());
            ((mViewHolder) holder).mMessageTextView.setText(mcontents.get(position).getArticle());
            ((mViewHolder) holder).mPlusOneView.setText(mcontents.get(position).getStar_count() + "");
            ((mViewHolder) holder).mAddCommentTextView.setText(mcontents.get(position).getComment_count() + "");
            ((mViewHolder) holder).mCollectTextView.setText(mcontents.get(position).getCollect_count() + "");

            ((mViewHolder) holder).mUserImageView.setImageUrl(mcontents.get(position).getAuthor().getAvatar(), imageLoader);
            ((mViewHolder) holder).mUserImageView.setDefaultImageResId(R.mipmap.ic_launcher);
            ((mViewHolder) holder).mPostImageView.setImageUrl(mcontents.get(position).getCover_image(), imageLoader);
            ((mViewHolder) holder).mPostImageView.setDefaultImageResId(R.mipmap.ic_launcher);


            ((mViewHolder) holder).mMessageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view, position, ((mViewHolder) holder).mMessageTextView.getId());
                }
            });
            ((mViewHolder) holder).mPostImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view,position,((mViewHolder) holder).mPostImageView.getId());
                }
            });
            ((mViewHolder) holder).mAddCommentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view,position,((mViewHolder) holder).mAddCommentTextView.getId());
                }
            });

            ((mViewHolder) holder).mDisplayNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view,position,((mViewHolder) holder).mDisplayNameTextView.getId());
                }
            });
            ((mViewHolder) holder).mUserImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view,position,((mViewHolder) holder).mUserImageView.getId());
                }
            });

            ((mViewHolder) holder).mPlusOneView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view,position,((mViewHolder) holder).mPlusOneView.getId());
                }
            });
            ((mViewHolder) holder).mCollectTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view,position,((mViewHolder) holder).mCollectTextView.getId());
                }
            });

        }else{
            if(type == MainFragment.NO_HEADER || type == MainFragment.NO_FOOTERANDHEADER)
                ((mViewHolder) holder).top.setVisibility(View.GONE);
        }

        if(mposition > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
        }
        lastPosition = mposition;
    }

    @Override
    public int getItemCount() {
        return mcontents.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TOP;
        }else{
            return NORMAL;
        }
    }

    @Override
    public void onClick(View view) {

    }

    class mViewHolder extends RecyclerView.ViewHolder{
        NetworkImageView mUserImageView;
        TextView mDisplayNameTextView;
        TextView mPlusOneView;
        TextView mAddCommentTextView;
        TextView mTimestampTextView;
        TextView mMessageTextView;
        TextView mCollectTextView;
        NetworkImageView mPostImageView;

        ImageView top;
        public mViewHolder(ImageView top){
            super(top);
            this.top = top;
        }

        public mViewHolder(View V){
            super(V);
            mUserImageView = (NetworkImageView)V.findViewById(R.id.user_iv) ;
            mDisplayNameTextView = (TextView)V.findViewById(R.id.display_name_tv) ;
            mPlusOneView = (TextView)V.findViewById(R.id.plus_one_tv);
            mAddCommentTextView = (TextView)V.findViewById(R.id.add_comment_tv);
            mTimestampTextView = (TextView)V.findViewById(R.id.timestamp_tv);
            mMessageTextView = (TextView)V.findViewById(R.id.message_tv);
            mCollectTextView = (TextView)V.findViewById(R.id.collect_tv);
            mPostImageView = (NetworkImageView)V.findViewById(R.id.post_iv);
        }
    }
}
