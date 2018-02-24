package dimi.com.tweetexplorer;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by User on 22.2.2018 г..
 */

public class TweetApp extends Application {

    public static Twitter twitter;

    @Override
    public void onCreate() {
        super.onCreate();
        if(isAppConnectedToInternet(this)) {
            configureTwitter(this);
        }
    }

    public static boolean isAppConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static void configureTwitter(Context context) {
        if(twitter == null) {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(context.getResources().getString(R.string.twitter_KEY))
                    .setOAuthConsumerSecret(context.getResources().getString(R.string.twitter_SECRET))
                    .setOAuthAccessToken(context.getResources().getString(R.string.twitter_AccessToken))
                    .setOAuthAccessTokenSecret(context.getResources().getString(R.string.twitter_AccessTokenSecret));
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();
        }
    }
}
