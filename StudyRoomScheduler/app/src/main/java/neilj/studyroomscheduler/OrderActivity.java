package neilj.studyroomscheduler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class OrderActivity extends AppCompatActivity {
    private int mSeconds;
    private int mRoomId;
    private TextView mTimeTextView;
    private TextView mRoomTextView;
    public static final String EXTRA_BOOLEAN =
            "neilj.studyroomscheduler.OrderActivity.extra.BOOLEAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //instantiate views
        mTimeTextView = findViewById(R.id.time_text);
        mRoomTextView = findViewById(R.id.room_text);
        //get intent data
        Intent i = getIntent();
        mRoomTextView.setText(i.getStringExtra(SingleHallActivity.EXTRA_HALL_ROOM_NUM));

        //get saved roomId
        mRoomId = i.getIntExtra(SingleHallActivity.EXTRA_ROOM_ID, -1);
        if(mRoomId == -1){
            throw new IllegalStateException();
        }

        mSeconds = -1;

        //onRestoreInstanceState
        if(savedInstanceState != null){
            mSeconds = savedInstanceState.getInt("seconds");
            mTimeTextView.setText(savedInstanceState.getString("time_string"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mSeconds > -1){
            //if seconds was set before
            outState.putInt("seconds", mSeconds);
            outState.putString("time_string", mTimeTextView.getText().toString());
        }
    }

    public void showTimePicker(View view) {
        DialogFragment timeFrag = new TimePickerFragment();
        timeFrag.show(getSupportFragmentManager(), "timePicker");
    }

    void processTimePickerResult(int hour, int minute, int secDifference){
        //update seconds
        setSeconds(secDifference);

        //update views to show time picked
        String timeMessage = hour + ":" + minute;
        mTimeTextView.setText(timeMessage);
    }

    void setSeconds(int seconds){
        mSeconds = seconds;
    }

    public void requestRoom(View view) {
        //make intent
        if(mSeconds > -1 && mTimeTextView.getText().toString() != ""){
            EditText mNetIdEditText = findViewById(R.id.netid_text);
            String[] inputs = new String[]{
                    Integer.toString(mRoomId),
                    mNetIdEditText.getText().toString(),
                    Integer.toString(mSeconds)};
            new AsyncRequestRoom(this).execute(inputs);
        }
    }


}
