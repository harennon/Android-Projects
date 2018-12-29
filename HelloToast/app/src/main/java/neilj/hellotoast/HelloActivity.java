package neilj.hellotoast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends AppCompatActivity {
    private static final int DEFAULT_COUNT = -1;
    private TextView mCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Intent i = getIntent();
        int count = i.getIntExtra(MainActivity.EXTRA_COUNT, DEFAULT_COUNT);
        mCountView = findViewById(R.id.text_count);
        mCountView.setText(Integer.toString(count));
    }
}
