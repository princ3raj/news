package com.appsnipp.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsnipp.news.adapters.FeatureRecyclerAdapter;
import com.appsnipp.news.adapters.NewsRecyclerAdapter;
import com.appsnipp.news.adapters.ScrollRecyclerAdapter;
import com.appsnipp.news.loaders.NewsLoader;
import com.appsnipp.news.model.NewsDetails;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<NewsDetails>> {

    private static final String TAG = "MainActivity";
  

    //URL and Network Related Work

    private String primaryUrlForEverythingAndSelectAllOption = "https://newsapi.org/v2/everything?";
    private String primaryUrl = "https://newsapi.org/v2/top-headlines?";
    private String apikey = "cdd3d4d6f6e34bd48b61e0bcd8dfdb0e";
    private String requestUrl = "";
    private boolean isConnected;
    private int LOADER_ID = 1;
    private LoaderManager loaderManager;
    private String query;


   //RecyclerView for Scrolling Category
    private RecyclerView mScrollRecyclerView;
    private ScrollRecyclerAdapter mScrollRecyclerAdapter;
    private RecyclerView.LayoutManager mScrollLayoutManager;

    //RecyclerView For Feature News

    private RecyclerView mFeatureRecyclerView;
    private RecyclerView.LayoutManager mFeatureLayoutManager;
    private FeatureRecyclerAdapter mFeatureRecyclerAdapter;

    //Recycler view for News view

    private RecyclerView mNewsRecyclerView;
    private NewsRecyclerAdapter mNewsRecyclerAdapter;
    private RecyclerView.LayoutManager mNewsLayoutManager;

    //Empty text View and progressbar
    private TextView emptyTextView;
    private ProgressBar progressBar;










    private BottomNavigationView bottomNavigationView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    return true;
                case R.id.navigationMyCourses:
                    return true;
                case R.id.navigationHome:
                    return true;
                case  R.id.navigationSearch:
                    return true;
                case  R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new DarkModePrefManager(this).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);


        //Scroll Recycler View Setup
        mScrollRecyclerView=(RecyclerView)findViewById(R.id.scroll_view);
        mScrollRecyclerView.setHasFixedSize(true);
        mScrollLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mScrollRecyclerView.setLayoutManager(mScrollLayoutManager);
        mScrollRecyclerAdapter= new ScrollRecyclerAdapter(this);
        mScrollRecyclerView.setAdapter(mScrollRecyclerAdapter);

        //Feature Recycler View Setup

        mFeatureRecyclerView=(RecyclerView)findViewById(R.id.featureRecyclerView);
        mFeatureRecyclerView.setHasFixedSize(true);
        mFeatureLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mFeatureRecyclerView.setLayoutManager(mFeatureLayoutManager);
        mFeatureRecyclerAdapter=new FeatureRecyclerAdapter(this);
        mFeatureRecyclerView.setAdapter(mFeatureRecyclerAdapter);

        //News RecyclerView set up

        mNewsRecyclerView=(RecyclerView)findViewById(R.id.news_view);
        mNewsRecyclerView.setHasFixedSize(true);
        mNewsLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mNewsRecyclerView.setLayoutManager(mNewsLayoutManager);
        mNewsRecyclerAdapter=new NewsRecyclerAdapter(this);
        mNewsRecyclerView.setAdapter(mNewsRecyclerAdapter);

        //reference of empty TextView and progressbar


        emptyTextView=(TextView)findViewById(R.id.empty_text_view);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);


        //checking NetWork Connectivity
        isConnected = checkInternetConnectivity();
        if (!isConnected) {
            emptyTextView.setText("Check Your Intenet Connection!");
        } else if (isConnected) {
            progressBar.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            emptyTextView.setText("No results found!");
        }


        if (savedInstanceState == null) {
            if (!isConnected) {
                emptyTextView.setText("Check Your Intenet Connection!");
            } else if (isConnected) {
                progressBar.setVisibility(View.VISIBLE);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(LOADER_ID, null, this);
            }
        }








//        //Loader KickOff
//        LoaderManager loaderManager = getLoaderManager();
//        loaderManager.initLoader(LOADER_ID, null, this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_dark_mode) {
            //code for setting dark mode
            //true for dark mode, false for day mode, currently toggling on each click
            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public Loader<ArrayList<NewsDetails>> onCreateLoader(int id, Bundle args) {

        Uri uri = Uri.parse(primaryUrl);
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("country","in");
//        builder.appendQueryParameter("q", query);
//        builder.appendQueryParameter("language", "en");
        builder.appendQueryParameter("apiKey", apikey);

        requestUrl = builder.toString();

        Log.i(TAG, "onCreateLoader: "+requestUrl);

        return new NewsLoader(this, requestUrl);

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsDetails>> loader, ArrayList<NewsDetails> data) {

        Log.i(TAG, "onLoadFinished: "+data);
        progressBar.setVisibility(View.GONE);

        //scrollRecyclerView
       mScrollRecyclerAdapter.addAll(data);
       mScrollRecyclerAdapter.notifyDataSetChanged();

       //FeatureRecyclerView
       mFeatureRecyclerAdapter.addAll(data);
       mFeatureRecyclerAdapter.notifyDataSetChanged();

       //NewsRecyclerView

        mNewsRecyclerAdapter.addAll(data);
        mNewsRecyclerAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsDetails>> loader) {
//        mFeatureRecyclerAdapter=null;
//        mScrollRecyclerAdapter=null;

    }

    private Boolean checkInternetConnectivity() {
        // Code to check the network connectivity status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
