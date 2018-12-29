package neilj.hellotoast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int mCount = 0;
    private TextView mShowCount;
    public static final String EXTRA_COUNT = "neilj.hellotoast.extra.COUNT";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowCount = (TextView) findViewById(R.id.show_count);
        Log.d(LOG_TAG, "onCreate");
    }

    public void sayHello(View view) {
        /*
        Toast toast = Toast.makeText(this, R.string.toast_message, Toast.LENGTH_SHORT);
        toast.show();
        */
        Intent i = new Intent(this, HelloActivity.class);
        i.putExtra(EXTRA_COUNT, mCount);
        startActivity(i);
    }

    public void countUp(View view) {
        mCount++;
        if (mShowCount != null){
            mShowCount.setText(Integer.toString(mCount));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("count", mCount);
    }

    @Override
    public void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        if(saveInstanceState != null){
            mCount = saveInstanceState.getInt("count");
            mShowCount.setText(Integer.toString(mCount));
        }
    }
}
