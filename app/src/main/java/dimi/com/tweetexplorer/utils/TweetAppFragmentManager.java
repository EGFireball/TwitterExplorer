package dimi.com.tweetexplorer.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import dimi.com.tweetexplorer.R;
import dimi.com.tweetexplorer.core.activities.MainActivity;
import dimi.com.tweetexplorer.core.fragments.SearchTweetFragment;
import dimi.com.tweetexplorer.core.fragments.abstractions.TweetFragment;

/**
 * Created by User on 23.2.2018 г..
 */

public class TweetAppFragmentManager {

    private static TweetAppFragmentManager instance;

    private TweetFragment currentFragment;

    private TweetAppFragmentManager() {

    }

    public static TweetAppFragmentManager getInstance() {
        if(instance == null) {
            instance = new TweetAppFragmentManager();
        }
        return instance;
    }

    public void showFragment(MainActivity activity, TweetFragment fragment, int containerId) {

        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if(currentFragment != null) {
            if(fragment instanceof SearchTweetFragment) {
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }

        if(activity.getSupportFragmentManager().findFragmentByTag(fragment.getFragmentTag()) != null) {
            transaction.show(fragment).commit();
        } else {
            transaction.add(containerId, fragment, fragment.getFragmentTag())
                       .addToBackStack(fragment.getFragmentTag())
                       .commit();
        }

        if(fragment instanceof SearchTweetFragment) {
            activity.showOrHideActionBarBackButton(false);
        } else {
            activity.showOrHideActionBarBackButton(true);
        }

        currentFragment = fragment;
    }

    public void hideFragment(MainActivity activity, TweetFragment fragment) {
            activity.getSupportFragmentManager()
                    .beginTransaction().hide(fragment)
                    .commit();
    }

    public void removeFragment(MainActivity activity, TweetFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        activity.getSupportFragmentManager().popBackStack();
    }

    public TweetFragment getCurrentFragment() {
        return currentFragment;
    }
}
