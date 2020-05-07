package com.appsnipp.news.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.appsnipp.news.model.NewsDetails;
import com.appsnipp.news.utils.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsDetails>> {

    private String mUrl;

    public NewsLoader(Context context, String url){
        super(context);
        mUrl=url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<NewsDetails> loadInBackground() {
        if(mUrl==null){
            return null;
        }
        ArrayList<NewsDetails> news= QueryUtils.fetchNewsData(mUrl);
        return news;
    }
}
