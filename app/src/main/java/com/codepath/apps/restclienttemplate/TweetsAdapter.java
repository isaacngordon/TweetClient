package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;


//STEPS
//pass in context and list of tweets
//for each row inflate the layout
//bind values based on the position of the element
//define the ViewHolder

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    private Context context;
    private List<Tweet> tweetsList;

    //pass in context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweetsList) {
        this.context = context;
        this.tweetsList = tweetsList;
    }


    //for each row inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, viewGroup, false);
        return new ViewHolder(view);
    }

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tweet tweet = tweetsList.get(i);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvScreenName.setText(tweet.user.handle);
        Glide.with(context).load(tweet.user.profileImageUrl).into(viewHolder.ivProfileImage);
        viewHolder.tvTime.setText(tweet.getTimeSince());
    }

    //return number of tweets in list
    @Override
    public int getItemCount() {
        return tweetsList.size();
    }

    //clears all tweets from the data set. Meant to be used when refreshing dataset
    public void clear(){
        tweetsList.clear();
        notifyDataSetChanged();
    }

    public void addTweets(List<Tweet> listOfTweets){
        tweetsList.addAll(listOfTweets);
        notifyDataSetChanged();
    }

    //define the ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvScreenName;
        public TextView tvBody;
        public ImageView ivProfileImage;
        public TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvTweet);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
