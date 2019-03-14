package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient twitterClient;
    private RecyclerView rvTweets;
    private TweetsAdapter tweetsAdapter;
    private List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        twitterClient = TwitterApplication.getRestClient(this);

        //find the recycler view
        rvTweets = findViewById(R.id.rvTweets);

        //init a list of tweets and adapter form the data source
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);

        //REcyclerView Setup: layoutManager, set adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);

        populateHomeTimeline();
    }

    private void populateHomeTimeline() {
        twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

                //iterate through the JSON array
                //convert each object into a tweet
                //add the tweet to the data source ie list
                //notify the adapter
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonTweet = response.getJSONObject(i);
                        Tweet aTweet = Tweet.fromJason(jsonTweet);
                        tweets.add(aTweet);
                        tweetsAdapter.notifyItemInserted(tweets.size() - 1 );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//for



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TwitterClient", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TwitterClient", errorResponse.toString());
            }
        });
    }
}
