package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.PrettyDate;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    public String body;
    public long uid;
    public String createdAt;
    public User user;

    public static Tweet fromJason(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        return tweet;
    }

    public String getTimeSince() {
        return PrettyDate.getTimeDifference(createdAt);
    }
}
