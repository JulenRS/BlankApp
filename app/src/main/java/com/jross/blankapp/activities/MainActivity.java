package com.jross.blankapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jross.blankapp.R;
import com.jross.blankapp.adapters.PageAdapter;
import com.jross.blankapp.domains.Post;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private static String TAG = "MainActivity";
    private FirebaseAuth firebaseAuth;
    private ArrayList<Post> myList = new ArrayList<>();
    private ArrayList<Post> myList2 = new ArrayList<>();
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private DatabaseReference mDatabase;
    private OnAboutDataReceivedListenerClassic mAboutDataListener;
    private OnAboutDataReceivedListenerSwipe mAboutDataListener2;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem tabClassic;
    private TabItem tabSwipe;
    private ImageView toolbarBackground;
    private int currentPage = 0;

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

        toolbarBackground = findViewById(R.id.toolbarBackground);

        Glide.with(getApplicationContext())
                .load("https://images.pexels.com/photos/371633/pexels-photo-371633.jpeg?auto=compress&cs=tinysrgb&h=350")
                .into(toolbarBackground);

        tabLayout = findViewById(R.id.tablayout);
        tabClassic = findViewById(R.id.tabClassic);
        tabSwipe = findViewById(R.id.tabSwipe);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        TextView txtEmail = navHeader.findViewById(R.id.txtEmail);
        txtEmail.setText(user.getEmail());
        TextView txtEmail2 = navHeader.findViewById(R.id.txtEmail2);
        txtEmail2.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                if (tab.getPosition() == 1) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            R.color.colorAccent));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            R.color.colorAccent));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
//                                R.color.colorAccent));
//                    }
//                } else if (tab.getPosition() == 2) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            android.R.color.darker_gray));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            android.R.color.darker_gray));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
//                                android.R.color.darker_gray));
//                    }
//                } else {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            R.color.colorPrimary));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
//                            R.color.colorPrimary));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,
//                                R.color.colorPrimaryDark));
//                    }
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //loadData();
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
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    myList.add(post);
                    Log.i(TAG, "Size: " + postSnapshot.toString());
                    //send the data to the fragment
                }
                mAboutDataListener.onDataReceived(myList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadData2() {
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
                myList2.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    myList2.add(post);
                    Log.i(TAG, "Size: " + postSnapshot.toString());
                    //send the data to the fragment
                }
                mAboutDataListener2.onDataReceived(myList2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadMoreData() {
        //currentPage++;
        //loadData();
    }

    public void onMoreData() {
        loadData();
    }

    public void onMoreData2() {
        loadData2();
    }

    public void setAboutDataListener(OnAboutDataReceivedListenerClassic listener) {
        this.mAboutDataListener = listener;
    }

    public void setAboutDataListener2(OnAboutDataReceivedListenerSwipe listener2) {
        this.mAboutDataListener2 = listener2;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }


    }

    public interface OnAboutDataReceivedListenerClassic {
        void onDataReceived(ArrayList<Post> myList);
    }

    public interface OnAboutDataReceivedListenerSwipe {
        void onDataReceived(ArrayList<Post> myList2);
    }
}
