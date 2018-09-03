package com.jross.blankapp.utils.swipe;

import android.support.v7.widget.RecyclerView;

import com.jross.blankapp.domains.Post;

public interface OnSwipeListener<T> {

    void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction);

    void onSwiped(RecyclerView.ViewHolder viewHolder, T t, int direction);

    //void onSwiped(RecyclerView.ViewHolder viewHolder, Post o, int direction);

    void onSwipedClear();

}
