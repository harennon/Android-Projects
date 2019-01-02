package neilj.simpleasynctask;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextview;
    private static final String TEXT_STATE = "current_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize globals
        mTextview = findViewById(R.id.textView1);

        //onRestoreInstanceState
        if(savedInstanceState != null){
            mTextview.setText(savedInstanceState.getString(TEXT_STATE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save state of textview
        outState.putString(TEXT_STATE, mTextview.getText().toString());
    }

    public void startTask(View view) {
        mTextview.setText(R.string.napping);
        ProgressBar p = findViewById(R.id.progress_horizontal);
        SimpleAsyncTask task = new SimpleAsyncTask(mTextview, p, (Button)view);
        task.execute();
    }
}
