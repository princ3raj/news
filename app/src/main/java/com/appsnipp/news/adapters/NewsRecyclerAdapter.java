package com.appsnipp.news.adapters;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsnipp.news.R;
import com.appsnipp.news.model.NewsDetails;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>

{
    private ArrayList<NewsDetails> mNewsDetails= new ArrayList<NewsDetails>();









    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news,parent,false);
        ViewHolder holder= new ViewHolder(view);
        return  holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Bitmap bitmap= mNewsDetails.get(i).getmUrlOfImage();

        holder.mNewsImageView.setImageBitmap(bitmap);
        holder.mContentTextView.setText(mNewsDetails.get(i).getmDescriptionOrContent());
        holder.mTitleTextView.setText(mNewsDetails.get(i).getmTitle());




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



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView=itemView.findViewById(R.id.newsTitleTextView);
            mContentTextView=itemView.findViewById(R.id.newsDetailTextView);
            mNewsImageView=itemView.findViewById(R.id.newsThumbnailImageView);


        }
    }

}
