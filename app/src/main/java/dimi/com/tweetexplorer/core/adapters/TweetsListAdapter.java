package dimi.com.tweetexplorer.core.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

import dimi.com.tweetexplorer.utils.HttpUtils;
import dimi.com.tweetexplorer.R;
import twitter4j.Status;

/**
 * Created by User on 23.2.2018 г..
 */

public class TweetsListAdapter extends RecyclerView.Adapter<TweetsListAdapter.ViewHolder> {

    private Context context;
    private List<Status> tweetsList;
    private ProgressDialog progressDialog;

    public TweetsListAdapter(Context context, List<Status> tweetsList) {
        this.context = context;
        this.tweetsList = tweetsList;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.loading_result_dialog_title));
        progressDialog.setCancelable(true);
    }

    @Override
    public TweetsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Status tweet = tweetsList.get(position);
        holder.source.loadData(context.getResources().getString(R.string.tweet_source) + tweet.getSource(),
                               "text/html; charset=utf-8", "utf-8");

        String url= HttpUtils.TWITTER_BASE_URL + tweet.getUser().getScreenName()
                + HttpUtils.TWITTER_STATUS_RESOURCE + tweet.getId();

        holder.tweet.getSettings().setJavaScriptEnabled(true);
        holder.tweet.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
                return super.onRenderProcessGone(view, detail);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadData("<html><body><b>Unable to open tweet</b></body></html>",
                              "text/html; charset=utf-8", "utf-8");
            }
        });
        holder.tweet.loadUrl(url);//loadData(tweet.getText(), "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        if(tweetsList != null) {
            return tweetsList.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private WebView tweet;
        private WebView source;


        public ViewHolder(View itemView) {
            super(itemView);
            tweet = itemView.findViewById(R.id.tweet);
            source = itemView.findViewById(R.id.source);
        }
    }
}
