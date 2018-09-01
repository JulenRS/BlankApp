package com.jross.blankapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jross.blankapp.R;
import com.jross.blankapp.adapters.MainListAdapter;
import com.jross.blankapp.domains.Post;
import com.jross.blankapp.utils.ScrollListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private ScrollListener scrollListener;
    private MainListAdapter mAdapter;
    private ArrayList<Post> myList = new ArrayList<>();

    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;

    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private int currentPage = 0;

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Log.i(TAG, "onCreate: user is: " + user.getEmail());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mRecycler = findViewById(R.id.mainList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);

        mAdapter = new MainListAdapter(myList);
        mRecycler.setAdapter(mAdapter);
        //addDataToList(0);

        scrollListener = new ScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //load next batch of items
                loadMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecycler.addOnScrollListener(scrollListener);

        loadData();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        TextView txtEmail = navHeader.findViewById(R.id.txtEmail);
        txtEmail.setText(user.getEmail());
        TextView txtEmail2 = navHeader.findViewById(R.id.txtEmail2);
        txtEmail2.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Toast.makeText(this, "Open Profile activity!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Open Settings activity!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Share this app!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, AuthenticationActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void addDataToList(int totalItemsCount) {
//        for (int i = totalItemsCount; i <= totalItemsCount + 30; i++) {
//            myList.add(new Post("Post " + i, "asd"));
//        }
//        mRecycler.post(() -> mAdapter.notifyDataSetChanged());
//    }

    private void loadData() {
        // example
        // at first load : currentPage = 0 -> we startAt(0 * 10 = 0)
        // at second load (first loadmore) : currentPage = 1 -> we startAt(1 * 10 = 10)
        mDatabase = FirebaseDatabase.getInstance().getReference("posts");
        /*for (int i = 0; i < 10; i++) {
            Post post = new Post("Hello World "+i+"!", "https://www.hbt-akademie.de/wp-content/uploads/2017/03/avatar-mini.png");
            String postId = mDatabase.push().getKey();
            mDatabase.child(postId).setValue(post);
        }*/

        mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myList.clear();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Post post = postSnapshot.getValue(Post.class);
                            myList.add(post);
                            mAdapter.notifyDataSetChanged();
                        }
                        //Post post = dataSnapshot.getValue(Post.class);
                        //Log.d(TAG, "User name: " + post.getTitle() + ", email " + post.getPicUrl());
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}});
    }

    private void loadMoreData(){
        //currentPage++;
        //loadData();
    }
}
