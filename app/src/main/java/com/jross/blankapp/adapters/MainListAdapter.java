package com.jross.blankapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jross.blankapp.R;
import com.jross.blankapp.domains.Post;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.FirebasePostVH> {

    private ArrayList<Post> myList;

    public MainListAdapter(ArrayList<Post> myList) {
        this.myList = myList;
    }

    @NonNull
    @Override
    public FirebasePostVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        return new FirebasePostVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.FirebasePostVH holder, int position) {
        holder.bindPost(myList.get(position));
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class FirebasePostVH extends RecyclerView.ViewHolder {
        private static final int MAX_WIDTH = 200;
        private static final int MAX_HEIGHT = 200;

        View mView;
        Context mContext;

        public FirebasePostVH(View itemView) {
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
        }

        public void bindPost(Post post) {
            ImageView postPic = mView.findViewById(R.id.postPic);
            TextView postTitle = mView.findViewById(R.id.postTitle);

            Glide.with(mContext)
                    .load(post.getPicUrl())
                    .into(postPic);

            postTitle.setText(post.getTitle());
        }
    }
}
