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
        actionBar.setTitle(getResources().getString(R.string.app_title));
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

    public void showOrHideActionBarBackButton(boolean showBackArrow) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_title));
        actionBar.setHomeButtonEnabled(showBackArrow);
        actionBar.setDisplayShowHomeEnabled(showBackArrow);
        actionBar.setDisplayHomeAsUpEnabled(showBackArrow);
        actionBar.show();
    }
}
