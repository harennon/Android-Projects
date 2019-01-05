package neilj.studyroomscheduler;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        //init views
        TextView status = findViewById(R.id.request_status);

        //get intent info
        if(getIntent().getBooleanExtra(OrderActivity.EXTRA_BOOLEAN, false)){
            //set views
            status.setText("Request: Success!");
        } else {
            status.setText("Request: Failure...");
        }
    }

}
