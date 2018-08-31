package com.jross.blankapp.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class FirebasePostVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    View mView;
    Context mContext;

    public FirebasePostVH(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindpost(Post post) {
        ImageView postPic = mView.findViewById(R.id.postPic);
        TextView postTitle = mView.findViewById(R.id.postTitle);

        Glide.with(mContext)
                .load(post.getPicUrl())
                .into(postPic);

        postTitle.setText(post.getTitle());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Post> posts = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("firebase_posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    posts.add(snapshot.getValue(Post.class));
                }

                //int itemPosition = getLayoutPosition();

//                Intent intent = new Intent(mContext, postDetailActivity.class);
//                intent.putExtra("position", itemPosition + "");
//                intent.putExtra("posts", Parcels.wrap(posts));
//
//                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
