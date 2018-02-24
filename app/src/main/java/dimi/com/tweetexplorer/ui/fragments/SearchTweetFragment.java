package dimi.com.tweetexplorer.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import dimi.com.tweetexplorer.R;
import dimi.com.tweetexplorer.TweetApp;
import dimi.com.tweetexplorer.TweetAppFragmentManager;
import dimi.com.tweetexplorer.ui.MainActivity;
import twitter4j.Query;
import twitter4j.QueryResult;

/**
 * Created by User on 23.2.2018 г..
 */

public class SearchTweetFragment extends TweetFragment {

    private String TAG = "SearchTweetFragment";

    public static final int DEFAULT_SPINNER_ITEM_POSITION = 4;

    private AutoCompleteTextView searchTv;
    private AppCompatButton searchBtn;
    private Spinner maximumResultsSpinner;
    private ToggleButton filterRetweetTb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.search_tweet_fragment_new, container, false);

        searchTv = view.findViewById(R.id.search_textView);
        searchTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performTweetSearch(view);
                    return true;
                }
                return false;
            }
        });

        searchBtn = view.findViewById(R.id.search_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performTweetSearch(view);
            }
        });

        filterRetweetTb = view.findViewById(R.id.filterRetweetsToggle);

        maximumResultsSpinner = view.findViewById(R.id.results_number);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.number_of_results, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        maximumResultsSpinner.setAdapter(adapter);
        maximumResultsSpinner.setSelection(DEFAULT_SPINNER_ITEM_POSITION);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    private void performTweetSearch(View view) {

        hideSoftKeyboard(view);

        if(TweetApp.isAppConnectedToInternet(getContext())) {
            if(searchTv.getText() != null && !searchTv.getText().toString().equals("")) {
                String searchPhrase = searchTv.getText().toString();
                int maximumResults = Integer.valueOf(maximumResultsSpinner.getSelectedItem().toString());
                boolean reTweetsFilterEnabled = filterRetweetTb.isChecked();
                new GetTweetListAsync(searchPhrase, maximumResults, reTweetsFilterEnabled)
                                        .executeOnExecutor(Executors.newSingleThreadExecutor());
            } else {
                Toast.makeText(getContext(),
                        getResources().getString(R.string.empty_search_text), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class GetTweetListAsync extends AsyncTask<Void, Void, ArrayList<twitter4j.Status>> {

        private static final String FILTER_RETWEETS_QUERY_PARAM = " -filter:retweets";

        private String searchPhrase;
        private int maximumResults;
        private ProgressDialog progressDialog;
        private boolean reTweetsFilterEnabled;

        public GetTweetListAsync(String searchPhrase, int maximumResults, boolean reTweetsFilterEnabled) {
            this.searchPhrase = searchPhrase;
            this.maximumResults = maximumResults;
            this.reTweetsFilterEnabled = reTweetsFilterEnabled;

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.searching_dialog_title));
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<twitter4j.Status> doInBackground(Void... voids) {
            try {
                return searchForTweets();
            } catch (twitter4j.TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);

            TweetListFragment tweetListFragment = new TweetListFragment();

            Bundle bundle = new Bundle();
            bundle.putString(TweetListFragment.TWEET_SEARCH_PHRASE_KEY, searchPhrase);
            bundle.putSerializable(TweetListFragment.TWEETS_LIST_KEY, statuses);
            tweetListFragment.setArguments(bundle);

            TweetAppFragmentManager.getInstance().hideFragment((MainActivity) getActivity(),
                                                                SearchTweetFragment.this);

            TweetAppFragmentManager.getInstance().showFragment((MainActivity) getActivity(),
                                                        tweetListFragment, R.id.fragment_container);
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
            Toast.makeText(getContext(),
                    getResources().getString(R.string.twitter_search_error), Toast.LENGTH_SHORT).show();
        }

        private ArrayList<twitter4j.Status> searchForTweets() throws twitter4j.TwitterException {

            String queryString = generateQueryString();
            Query query = new Query(queryString); //+ searchPhrase); // "source:twitter4j yusukey"
            query.setCount(maximumResults);

            if(TweetApp.twitter == null) {
                TweetApp.configureTwitter(getActivity());
                this.cancel(true);
                return null;
            }

            QueryResult result = TweetApp.twitter.search(query);

            return new ArrayList<>(result.getTweets());
        }

        private String generateQueryString() {
            StringBuffer queryBuffer = new StringBuffer().append("#").append(searchPhrase);
            if(reTweetsFilterEnabled) {
                queryBuffer.append(FILTER_RETWEETS_QUERY_PARAM);
            }
            return queryBuffer.toString();
        }
    }
}
