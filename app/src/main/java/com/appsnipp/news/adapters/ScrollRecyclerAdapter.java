package com.appsnipp.news.adapters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsnipp.news.MainActivity;
import com.appsnipp.news.R;
import com.appsnipp.news.model.NewsDetails;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScrollRecyclerAdapter extends RecyclerView.Adapter<ScrollRecyclerAdapter.ViewHolder>

{
    private ArrayList<NewsDetails> mNewsDetails= new ArrayList<NewsDetails>();









    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_topics,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return  holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
      Bitmap bitmap= mNewsDetails.get(i).getmUrlOfImage();
      holder.mCategoryImageView.setImageBitmap(bitmap);



    }

    @Override
    public int getItemCount() {
        return mNewsDetails.size();
    }


    public void addAll(ArrayList<NewsDetails> newsDetails)
    {
        mNewsDetails=   newsDetails;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mCategoryImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryImageView=itemView.findViewById(R.id.profile_image);

        }
    }

}
