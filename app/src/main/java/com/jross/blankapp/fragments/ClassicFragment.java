package com.jross.blankapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jross.blankapp.R;
import com.jross.blankapp.activities.MainActivity;
import com.jross.blankapp.adapters.MainListAdapter;
import com.jross.blankapp.domains.Post;
import com.jross.blankapp.utils.EqualSpacingItemDecoration;
import com.jross.blankapp.utils.ScrollListener;

import java.util.ArrayList;

public class ClassicFragment extends Fragment implements MainActivity.OnAboutDataReceivedListener{

    private MainListAdapter mAdapter;
    private ScrollListener scrollListener;
    private ArrayList<Post> myList = new ArrayList<>();
    private RecyclerView mRecycler;

    private static String TAG = "ClassicFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        MainActivity mActivity = (MainActivity) getActivity();
        mActivity.setAboutDataListener(this);

        View myView = inflater.inflate(R.layout.fragment_classic, container, false);

        mRecycler = myView.findViewById(R.id.mainList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.addItemDecoration(new EqualSpacingItemDecoration(16));

        mAdapter = new MainListAdapter(myList);
        mRecycler.setAdapter(mAdapter);
        //addDataToList();

        scrollListener = new ScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //load next batch of items
                //if (myList.size()>0)
                //    mActivity.onMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecycler.addOnScrollListener(scrollListener);

        mActivity.onMoreData();

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
