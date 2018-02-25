package dimi.com.tweetexplorer.core.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import dimi.com.tweetexplorer.R;
import dimi.com.tweetexplorer.core.fragments.SearchTweetFragment;
import dimi.com.tweetexplorer.core.fragments.abstractions.TweetFragment;
import dimi.com.tweetexplorer.utils.TweetAppFragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Twitter Explorer");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        SearchTweetFragment searchTweetFragment = new SearchTweetFragment();
        TweetAppFragmentManager.getInstance().showFragment(this, searchTweetFragment,
                                                                           R.id.fragment_container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateBack();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    private void navigateBack() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.getBackStackEntryCount() < 2) {
            return;
        }

        TweetFragment currentFragment = TweetAppFragmentManager.getInstance().getCurrentFragment();

        TweetFragment previousFragment = ((TweetFragment) fragmentManager.getFragments()
                .get(fragmentManager.getBackStackEntryCount()-2));

        TweetAppFragmentManager.getInstance().removeFragment(this, currentFragment);
        TweetAppFragmentManager.getInstance().showFragment(this, previousFragment, R.id.fragment_container);
    }

    public void shorOrHideActionBarBackButton(boolean showBackArrow) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_title));
        actionBar.setHomeButtonEnabled(showBackArrow);
        actionBar.setDisplayShowHomeEnabled(showBackArrow);
        actionBar.setDisplayHomeAsUpEnabled(showBackArrow);
        actionBar.show();
    }
    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result to the login button.
//        loginButton.onActivityResult(requestCode, resultCode, data);
//    }

//    private class GetTweetListAsync extends AsyncTask {
//
//        public static final String BASE_URI = "https://api.twitter.com/";
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//
//            //loadJSON();
//            try {
//                //twitTimeline();
//                searchForTweets("google");
//            } catch (twitter4j.TwitterException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        private void loadJSON() {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URI)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            final ITweet request = retrofit.create(ITweet.class);
//
//            Call<TwitterItem> call = request.getTweets();
//            call.enqueue(new retrofit2.Callback<TwitterItem>() {
//                @Override
//                public void onResponse(Call<TwitterItem> call, Response<TwitterItem> response) {
//                    Log.e("THE RESPONSE::::. : ", response.toString());
//                    if (response.isSuccessful()) {
//                        TwitterItem tweet = response.body();
//                        Log.e("THE NAME::::. : ", tweet.getName());
//                    } else {
//                        Log.e("THE NAME :  ", " NOO RESPONSE .::: ");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<TwitterItem> call, Throwable t) {
//                    Log.e("THE NAME :  ", " onFAIL .::: ");
//                }
//            });
//        }
//
//        private void twitTimeline() throws twitter4j.TwitterException {
//            // The factory instance is re-useable and thread safe.
//            //Twitter twitter = TwitterFactory.getSingleton();
//            List<twitter4j.Status> statuses = TweetApp.twitter.getHomeTimeline();
////        System.out.println("Showing home timeline.");
//            for (twitter4j.Status status : statuses) {
////            System.out.println(status.getUser().getName() + ":" +
////                    status.getText());
//                Log.d("TEST", status.getUser().getName() + ":" +
//                        status.getText());
//            }
//        }
//
//        private void searchForTweets(String searchPhrase) throws twitter4j.TwitterException {
//            Query query = new Query(searchPhrase); // "source:twitter4j yusukey"
//            QueryResult result = TweetApp.twitter.search(query);
//            for (twitter4j.Status status : result.getTweets()) {
//                //System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
//                Log.d("TEST", "@" + status.getUser().getScreenName() + ":" + status.getText());
//            }
//        }
//    }
}
