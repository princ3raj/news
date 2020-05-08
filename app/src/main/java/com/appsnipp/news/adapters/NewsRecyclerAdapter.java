package com.appsnipp.news.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsnipp.news.R;
import com.appsnipp.news.model.NewsDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>

{

    private static final String TAG = "NewsRecyclerAdapter";

    private Context mContext;

    public NewsRecyclerAdapter(Context context) {
        mContext=context;

    }

    private ArrayList<NewsDetails> mNewsDetails= new ArrayList<NewsDetails>();









    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news,parent,false);
        ViewHolder holder= new ViewHolder(view);
        return  holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
//        Bitmap bitmap= mNewsDetails.get(i).getmUrlOfImage();
//
//        holder.mNewsImageView.setImageBitmap(bitmap);

        Picasso.get().load(mNewsDetails.get(i).getmUrlOfImage()).into(holder.mNewsImageView);

        holder.mContentTextView.setText(mNewsDetails.get(i).getmDescriptionOrContent());
        holder.mTitleTextView.setText(mNewsDetails.get(i).getmTitle());

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: "+mNewsDetails.get(i).getmTitle());
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri NewsUri = Uri.parse(mNewsDetails.get(i).getmUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);

                // Send the intent to launch a new activity
                mContext.startActivity(websiteIntent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mNewsDetails.size();
    }


    public void addAll(ArrayList<NewsDetails> newsDetails)
    {
        mNewsDetails=   newsDetails;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView  mTitleTextView;
        TextView  mContentTextView;
        ImageView mNewsImageView;
        RelativeLayout mRelativeLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView=itemView.findViewById(R.id.newsTitleTextView);
            mContentTextView=itemView.findViewById(R.id.newsDetailTextView);
            mNewsImageView=itemView.findViewById(R.id.newsThumbnailImageView);
            mRelativeLayout=itemView.findViewById(R.id.news_relative_Layout);


        }
    }

}
