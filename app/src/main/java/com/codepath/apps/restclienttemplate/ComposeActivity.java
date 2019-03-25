package com.codepath.apps.restclienttemplate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;

    private EditText etCompose;
    private Button btnTweet;
    private TextView tvCharCount;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btSubmit);
        tvCharCount = findViewById(R.id.tvCharCount);
        client = TwitterApplication.getRestClient(this);

        tvCharCount.setText(String.format("0/%d", MAX_TWEET_LENGTH));

        //set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();

                //err handling
                //of tweet is empty
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Give text yo...", Toast.LENGTH_LONG).show();
                    return;
                }//if

                //if tweet too long
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Tweet too long yo...", Toast.LENGTH_LONG).show();
                    return;
                }//if

                //tweet is good, so make API call
                Toast.makeText(ComposeActivity.this, "Tweet posted yo...", Toast.LENGTH_LONG).show();
                client.postTweet(tweetContent, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("TwitterClient", "Posted tweet yo" + response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TwitterClient", "Failed to post yo" + responseString);
                    }
                });



            }//onClick
        });//btn.SetOnClickListener
        //if button tapped, then make api call to publish the content in the tweet

        //making the charCount change as the tweet is being typed
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String compose = etCompose.getText().toString();
                String charCount = String.format("%d/%d",compose.length(), MAX_TWEET_LENGTH);
                tvCharCount.setText(charCount);
                if(compose.length() > MAX_TWEET_LENGTH){
                    btnTweet.setEnabled(false);
                    //turn tvCharCount red
                    tvCharCount.setTextColor(Color.RED);
                    return;
                }

                btnTweet.setEnabled(true);
                tvCharCount.setTextColor(Color.GRAY);


            }
        });
        //after a char is typed, inrement count and in count is above 140 then turn red, disable button, otherwise ggre with working button

    }//onCreate
}
