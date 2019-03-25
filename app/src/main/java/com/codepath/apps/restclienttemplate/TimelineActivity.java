package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;

    private TwitterClient twitterClient;
    private RecyclerView rvTweets;
    private TweetsAdapter tweetsAdapter;
    private List<Tweet> tweets;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        twitterClient = TwitterApplication.getRestClient(this);

        //setup pull to refresh
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                                                android.R.color.holo_green_light,
                                                android.R.color.holo_orange_light,
                                                android.R.color.holo_red_light);


        //find the recycler view
        rvTweets = findViewById(R.id.rvTweets);

        //init a list of tweets and adapter form the data source
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);

        //REcyclerView Setup: layoutManager, set adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);

        populateHomeTimeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterClient", "content is being refreshed");
                populateHomeTimeline();
            }
        });
    }

    private void populateHomeTimeline() {
        twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                List<Tweet> tweetsToAdd = new ArrayList<>();

                //iterate through the JSON array
                //convert each object into a tweet
                //add the tweet to the data source ie list
                //notify the adapter
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonTweet = response.getJSONObject(i);
                        Tweet aTweet = Tweet.fromJason(jsonTweet);
//                        tweets.add(aTweet);
//                        tweetsAdapter.notifyItemInserted(tweets.size() - 1 );
                        tweetsToAdd.add(aTweet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//for

                tweetsAdapter.clear();
                tweetsAdapter.addTweets(tweetsToAdd);
                swipeContainer.setRefreshing(false);

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
    }//populateHomeTimeline


    /*
    * Adding methods for the action bar
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //INflate the menu ; this adds items to the actionbar if it is present
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //show we tapped the edit icon
//        Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show();
        //navigate to the new activity
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            //pull info out of the data Intent object
            Tweet newTweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));

            //update the recycler view with this tweet
            List<Tweet> listTweet = new ArrayList<>();
            tweets.add(0, newTweet);
            tweetsAdapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
    }
}
