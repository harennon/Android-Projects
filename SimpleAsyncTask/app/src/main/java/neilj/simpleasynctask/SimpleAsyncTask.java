package neilj.simpleasynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsyncTask extends AsyncTask<Void, Integer, String> {
    private WeakReference<TextView> mTextView;
    private WeakReference<ProgressBar> mProgressBar;
    private WeakReference<Button> mButton;
    private final int interval = 31;

    SimpleAsyncTask(TextView tv, ProgressBar p, Button b) {
        mTextView = new WeakReference<>(tv);
        mProgressBar = new WeakReference<>(p);
        mButton = new WeakReference<>(b);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar.get().setVisibility(View.VISIBLE);
        mButton.get().setEnabled(false);
    }

    @Override
    protected String doInBackground(Void... voids) {
        //generate random number to sleep
        Random r = new Random();
        int n = r.nextInt(100);


        //sleep
        for (int i = 0; i < n; i++) {
            try {
                Thread.sleep(interval);
                publishProgress(i * interval, (i*100/n));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //return text after performing async task
        return "Awake at last after sleeping for " + n * interval + " milliseconds!";
    }

    @Override
    protected void onPostExecute(String result) {
        mTextView.get().setText(result);
        mProgressBar.get().setVisibility(View.GONE);
        mButton.get().setEnabled(true);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        String update = values[0] + " ms";
        mTextView.get().setText(update);
        mProgressBar.get().setProgress(values[1]);
    }
}
