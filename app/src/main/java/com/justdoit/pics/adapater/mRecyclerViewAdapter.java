package com.justdoit.pics.adapater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justdoit.pics.R;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ljz on 2015/10/29.
 */
public class mRecyclerViewAdapter extends RecyclerView.Adapter {
    int[] testphotodata = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,
            R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o};

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_cardview,parent,false);

        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((mViewHolder)holder).username.setText("test");
        ((mViewHolder)holder).content_text.setText("test");
        ((mViewHolder)holder).userphoto.setImageResource(testphotodata[position]);
        ((mViewHolder)holder).testphoto.setImageResource(testphotodata[position]);
    }

    @Override
    public int getItemCount() {
        return testphotodata.length-1;
    }


    class mViewHolder extends RecyclerView.ViewHolder{
        CircleImageView userphoto;
        TextView username;
        TextView content_text;
        ImageView testphoto;
        public mViewHolder(View V){
            super(V);
            userphoto = (CircleImageView)V.findViewById(R.id.content_userphoto);
            username = (TextView)V.findViewById(R.id.content_username);
            content_text = (TextView)V.findViewById(R.id.content_text);
            testphoto = (ImageView)V.findViewById(R.id.testphoto);
        }
    }
}
