package com.justdoit.pics.adapater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.justdoit.pics.R;
import com.justdoit.pics.bean.Comment;
import com.justdoit.pics.bean.Content;
import com.justdoit.pics.model.ItemClickHelper;
import com.justdoit.pics.model.NetSingleton;

import java.util.ArrayList;

/**
 * Created by ljz on 2015/11/26.
 */
public class DetailAdapter extends RecyclerView.Adapter {


    private int lastPosition = -1;
    private Context mcontext;
    public Content mcontent;
    public ArrayList<Comment> mcomments;
    private static final int TOPIC = 0;
    private static final int REPLY = 1;
    private static final int NORMAL = 2;
    ImageLoader imageLoader;
    ItemClickHelper mcallback;

    public DetailAdapter(Context context ,Content content,ArrayList<Comment> comments , ItemClickHelper callback){
        mcontext = context;
        mcontent = content;
        mcomments = comments;
        imageLoader = NetSingleton.getInstance(context).getImageLoader();
        mcallback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mcontext);
        View v = null;
        int Viewid = 0 ;
        if(viewType == TOPIC){
            v = mLayoutInflater.inflate(R.layout.index_cardview,parent,false);
            Viewid = R.layout.index_cardview;
        }else if(viewType == REPLY){
            v = mLayoutInflater.inflate(R.layout.item_replay,parent,false);
            Viewid = R.layout.item_replay;
        }else{
            v = mLayoutInflater.inflate(R.layout.item_comment,parent,false);
            Viewid = R.layout.item_comment;
        }
        return new mViewHolder(v,Viewid);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(position == 0){
            ((mViewHolder) holder).mDisplayNameTextView.setText(mcontent.getAuthor().getUsername());
            ((mViewHolder) holder).mTimestampTextView.setText(mcontent.getUpdate_time());
            ((mViewHolder) holder).mMessageTextView.setText(mcontent.getArticle());
            ((mViewHolder) holder).mPlusOneView.setText(mcontent.getStar_count() + "");
            ((mViewHolder) holder).mCollectTextView.setText(mcontent.getCollect_count() + "");

            ((mViewHolder) holder).mAddCommentTextView.setVisibility(View.INVISIBLE);

            ((mViewHolder) holder).mUserImageView.setImageUrl(mcontent.getAuthor().getAvatar(), imageLoader);
            ((mViewHolder) holder).mUserImageView.setDefaultImageResId(R.mipmap.ic_launcher);
            ((mViewHolder) holder).mPostImageView.setImageUrl(mcontent.getCover_image(), imageLoader);
            ((mViewHolder) holder).mPostImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        }else if(position == 1 ){
            ((mViewHolder) holder).comment_ev.setHint(R.string.reply);
            ((mViewHolder) holder).reply_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcallback.onItemClick(view,position,((mViewHolder) holder).reply_btn.getId(),((mViewHolder) holder).comment_ev.getText().toString());
                    ((mViewHolder) holder).comment_ev.setText("");
                }
            });
        }else{
            ((mViewHolder) holder).mDisplayNameTextView.setText(mcomments.get(position-2).getAuthor().getUsername());
            ((mViewHolder) holder).commentcontent_tv.setText(mcomments.get(position-2).getContent());
            ((mViewHolder) holder).mTimestampTextView.setText(mcomments.get(position-2).getCreate_time());

            ((mViewHolder) holder).mUserImageView.setImageUrl(mcomments.get(position-2).getAuthor().getAvatar(), imageLoader);
            ((mViewHolder) holder).mUserImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        }

        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
        }
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        if(mcontent == null){
            return 0;
        }
        return mcomments.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TOPIC;
        }else if(position == 1){
            return REPLY;
        }else{
            return NORMAL;
        }
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

        EditText comment_ev;
        Button reply_btn;

        TextView commentcontent_tv;

        public mViewHolder(View V, int Viewid){
            super(V);
            switch (Viewid){
                case R.layout.index_cardview:
                    mUserImageView = (NetworkImageView)V.findViewById(R.id.user_iv) ;
                    mDisplayNameTextView = (TextView)V.findViewById(R.id.display_name_tv) ;
                    mPlusOneView = (TextView)V.findViewById(R.id.plus_one_tv);
                    mAddCommentTextView = (TextView)V.findViewById(R.id.add_comment_tv);
                    mTimestampTextView = (TextView)V.findViewById(R.id.timestamp_tv);
                    mMessageTextView = (TextView)V.findViewById(R.id.message_tv);
                    mCollectTextView = (TextView)V.findViewById(R.id.collect_tv);
                    mPostImageView = (NetworkImageView)V.findViewById(R.id.post_iv);
                    break;
                case R.layout.item_replay:
                    comment_ev = (EditText)V.findViewById(R.id.comment_et);
                    reply_btn = (Button)V.findViewById(R.id.reply_btn);
                    break;
                case R.layout.item_comment:
                    mUserImageView = (NetworkImageView)V.findViewById(R.id.user_iv);
                    mDisplayNameTextView = (TextView)V.findViewById(R.id.display_name_tv);
                    commentcontent_tv = (TextView)V.findViewById(R.id.commentcontent_tv);
                    mTimestampTextView = (TextView)V.findViewById(R.id.timestamp_tv);
            }
        }
    }
}
