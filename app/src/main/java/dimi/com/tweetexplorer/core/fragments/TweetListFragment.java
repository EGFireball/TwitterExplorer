package dimi.com.tweetexplorer.core.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dimi.com.tweetexplorer.R;
import dimi.com.tweetexplorer.core.activities.MainActivity;
import dimi.com.tweetexplorer.core.adapters.TweetsListAdapter;
import dimi.com.tweetexplorer.core.fragments.abstractions.TweetFragment;
import twitter4j.Status;

/**
 * Created by User on 23.2.2018 г..
 */

public class TweetListFragment extends TweetFragment {

    private String TAG = "TweetListFragment";
    public static final String TWEET_SEARCH_PHRASE_KEY = "SearchPhrase";
    public static final String TWEETS_LIST_KEY = "Tweets";

    private RecyclerView tweetsListView;
    private TweetsListAdapter tweetAdapter;
    private ArrayList<Status> tweetsList;
    private String searchPhrase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                                            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tweet_list_fragment, container, false);

        tweetsListView = view.findViewById(R.id.tweets_list);
        TextView emptyListTv = view.findViewById(R.id.emptyListView);
        if(tweetsList != null && tweetsList.size() > 0) {
            tweetAdapter = new TweetsListAdapter(getActivity(), tweetsList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            tweetsListView.setLayoutManager(layoutManager);
            tweetsListView.setAdapter(tweetAdapter);
            emptyListTv.setVisibility(View.GONE);
            tweetsListView.setVisibility(View.VISIBLE);
        } else {
            tweetsListView.setVisibility(View.GONE);
            emptyListTv.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        tweetsList = (ArrayList<Status>) args.getSerializable(TWEETS_LIST_KEY);
        searchPhrase = args.getString(TWEET_SEARCH_PHRASE_KEY);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).getSupportActionBar()
                .setTitle(getResources().getString(R.string.tweet_list_fragment_title, searchPhrase));
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
