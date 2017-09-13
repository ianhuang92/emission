package monash.emission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

/**
 * Created by Ranger on 2017/9/11.
 */

public class MyResultReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            // success
            Log.i("Post status","Success");
            Toast.makeText(context,"Post to twitter successfully.", Toast.LENGTH_SHORT).show();
            final Long tweetId = intent.getExtras().getLong(TweetUploadService.EXTRA_TWEET_ID);
        } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
            // failure
            Log.i("Post status","Fail");
            Toast.makeText(context,"Post Failed.", Toast.LENGTH_SHORT).show();
            final Intent retryIntent = intent.getExtras().getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
        } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
            // cancel
            Toast.makeText(context,"Post Canceled.", Toast.LENGTH_SHORT).show();
            Log.i("Post status","Cancel");
        }
    }
}