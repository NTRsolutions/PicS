package com.justdoit.pics.model;

import android.view.View;

/**
 * Created by ljz on 2015/11/26.
 */
public interface ItemClickHelper {
    void onItemClick(View item, int position, int which);

    void onItemClick(View item, int position, int which,String content);
}
