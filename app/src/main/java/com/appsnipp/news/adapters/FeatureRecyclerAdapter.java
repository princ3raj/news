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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsnipp.news.R;
import com.appsnipp.news.model.NewsDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeatureRecyclerAdapter extends RecyclerView.Adapter<FeatureRecyclerAdapter.ViewHolder>

{
    private static final String TAG = "FeatureRecyclerAdapter";
    private ArrayList<NewsDetails> mNewsDetails= new ArrayList<NewsDetails>();
    private Context mContext;

    public FeatureRecyclerAdapter(Context context) {
        mContext=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_featured_news,parent,false);
        ViewHolder holder= new ViewHolder(view);
        return  holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
//        Bitmap bitmap= mNewsDetails.get(i).getmUrlOfImage();
//
//        holder.mFeatureImageView.setImageBitmap(bitmap);
        Picasso.get().load(mNewsDetails.get(i).getmUrlOfImage()).into(holder.mFeatureImageView);



        holder.mTitleTextView.setText(mNewsDetails.get(i).getmTitle());
        holder.mContentTextView.setText(mNewsDetails.get(i).getmDescriptionOrContent());

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
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

        TextView mTitleTextView;
        TextView mContentTextView;
        ImageView mFeatureImageView;
        LinearLayout mLinearLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView=itemView.findViewById(R.id.featureTitle);
            mContentTextView=itemView.findViewById(R.id.featureContent);
            mFeatureImageView=itemView.findViewById(R.id.featureImage);
            mLinearLayout=itemView.findViewById(R.id.featured_news_linear_layout);


        }
    }

}
