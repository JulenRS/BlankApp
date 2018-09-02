package com.jross.blankapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jross.blankapp.R;
import com.jross.blankapp.activities.MainActivity;
import com.jross.blankapp.adapters.SwipeAdapter;
import com.jross.blankapp.domains.Post;
import com.jross.blankapp.utils.swipe.CardConfig;
import com.jross.blankapp.utils.swipe.CardItemTouchHelperCallback;
import com.jross.blankapp.utils.swipe.CardLayoutManager;
import com.jross.blankapp.utils.swipe.OnSwipeListener;

import java.util.ArrayList;

public class SwipeFragment extends Fragment implements MainActivity.OnAboutDataReceivedListenerSwipe{

    private ArrayList<Post> myList = new ArrayList<>();
    private RecyclerView mRecycler;
    private SwipeAdapter mAdapter;

    private static String TAG = "SwipeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        MainActivity mActivity = (MainActivity) getActivity();
        mActivity.setAboutDataListener2(this);

        View myView = inflater.inflate(R.layout.fragment_swipe, container, false);

        mRecycler = myView.findViewById(R.id.recyclerView);

        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SwipeAdapter(myList);
        mRecycler.setAdapter(mAdapter);

        mActivity.onMoreData2();

        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(mRecycler.getAdapter(), myList);
        cardCallback.setOnSwipedListener(new OnSwipeListener<Post>() {

            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                SwipeAdapter.MyViewHolder myHolder = (SwipeAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (direction == CardConfig.SWIPING_LEFT) {
                    myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
                } else if (direction == CardConfig.SWIPING_RIGHT) {
                    myHolder.likeImageView.setAlpha(Math.abs(ratio));
                } else {
                    myHolder.dislikeImageView.setAlpha(0f);
                    myHolder.likeImageView.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, Post o, int direction) {
                SwipeAdapter.MyViewHolder myHolder = (SwipeAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                myHolder.dislikeImageView.setAlpha(0f);
                myHolder.likeImageView.setAlpha(0f);
                Toast.makeText(getActivity(), direction == CardConfig.SWIPED_LEFT ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(getActivity(), "data clear", Toast.LENGTH_SHORT).show();
                mRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //initData();
                        mRecycler.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }

        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(mRecycler, touchHelper);
        mRecycler.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(mRecycler);

        return myView;
    }

    @Override
    public void onDataReceived(ArrayList<Post> myList) {
        updateList(myList);
    }

    public void updateList(ArrayList<Post> myList){
        this.myList.addAll(myList);
        Log.i(TAG, "Size: "+myList.size());
        mAdapter.notifyDataSetChanged();
        //mRecycler.post(() -> mAdapter.notifyDataSetChanged());
    }
}
