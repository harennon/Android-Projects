package neilj.studyroomscheduler;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class AsyncRequestRoom extends AsyncTask<String[], Void, Boolean> {
    WeakReference<Context> mContext;

    AsyncRequestRoom(Context context){
        mContext = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String[]... strings) {
        return NetworkUtil.addOccupiedRoom(strings[0][0], strings[0][1], strings[0][2]);
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        Intent receiptIntent = new Intent(mContext.get(), ReceiptActivity.class);
        receiptIntent.putExtra(OrderActivity.EXTRA_BOOLEAN, b);
        mContext.get().startActivity(receiptIntent);
    }
}
