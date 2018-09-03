package com.jross.blankapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwipeFragment extends Fragment implements MainActivity.OnAboutDataReceivedListenerSwipe{

    private static String TAG = "SwipeFragment";

    private ArrayList<Post> myList = new ArrayList<>();
    private SwipeAdapter mAdapter;

    @BindView(R.id.recyclerView) RecyclerView mRecycler;

    public static SwipeFragment newInstance() {
        SwipeFragment swipeFragment = new SwipeFragment();
        Bundle args = new Bundle();
        swipeFragment.setArguments(args);
        return swipeFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) Objects.requireNonNull(getActivity())).setAboutDataListener2(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View myView = inflater.inflate(R.layout.fragment_swipe, container, false);
        ButterKnife.bind(this, myView);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SwipeAdapter(myList);
        mRecycler.setAdapter(mAdapter);
        CardItemTouchHelperCallback<Post> cardCallback;
        cardCallback = new CardItemTouchHelperCallback<>(mRecycler.getAdapter(), myList);
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
                mRecycler.postDelayed(() -> {
                    mRecycler.getAdapter().notifyDataSetChanged();
                }, 3000L);
            }
        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(mRecycler, touchHelper);
        mRecycler.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(mRecycler);
        ((MainActivity) Objects.requireNonNull(getActivity())).onMoreData2();
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
