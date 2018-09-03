package com.jross.blankapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jross.blankapp.R;
import com.jross.blankapp.activities.MainActivity;
import com.jross.blankapp.adapters.MainListAdapter;
import com.jross.blankapp.domains.Post;
import com.jross.blankapp.utils.visuals.EqualSpacingItemDecoration;
import com.jross.blankapp.utils.ScrollListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassicFragment extends Fragment implements MainActivity.OnAboutDataReceivedListenerClassic {

    private static String TAG = "ClassicFragment";

    private MainListAdapter mAdapter;
    private ArrayList<Post> myList = new ArrayList<>();

    @BindView(R.id.mainList) RecyclerView mRecycler;

    public static ClassicFragment newInstance() {
        ClassicFragment classicFragment = new ClassicFragment();
        Bundle args = new Bundle();
        classicFragment.setArguments(args);
        return classicFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) Objects.requireNonNull(getActivity())).setAboutDataListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View myView = inflater.inflate(R.layout.fragment_classic, container, false);
        ButterKnife.bind(this, myView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.addItemDecoration(new EqualSpacingItemDecoration(16));
        mAdapter = new MainListAdapter(myList);
        mRecycler.setAdapter(mAdapter);
        ScrollListener scrollListener = new ScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            }
        };
        mRecycler.addOnScrollListener(scrollListener);
        ((MainActivity) Objects.requireNonNull(getActivity())).onMoreData();
        return myView;
    }

    @Override
    public void onDataReceived(ArrayList<Post> myList) {
        updateList(myList);
    }

    public void updateList(ArrayList<Post> myList) {
        this.myList.addAll(myList);
        mAdapter.notifyDataSetChanged();
        //mRecycler.post(() -> mAdapter.notifyDataSetChanged());
    }
}
