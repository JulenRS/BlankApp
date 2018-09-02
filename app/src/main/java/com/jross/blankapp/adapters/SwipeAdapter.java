package com.jross.blankapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jross.blankapp.R;
import com.jross.blankapp.domains.Post;

import java.util.ArrayList;

public class SwipeAdapter extends RecyclerView.Adapter {

    private ArrayList<Post> myList;
    private Context mContext;

    public SwipeAdapter(ArrayList<Post> myList) {
        this.myList = myList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView avatarImageView = ((MyViewHolder) holder).avatarImageView;
        Glide.with(mContext)
                .load(myList.get(position).getPicUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(avatarImageView);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImageView;
        public ImageView likeImageView;
        public ImageView dislikeImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            avatarImageView = itemView.findViewById(R.id.iv_avatar);
            likeImageView = itemView.findViewById(R.id.iv_like);
            dislikeImageView = itemView.findViewById(R.id.iv_dislike);
        }

    }
}