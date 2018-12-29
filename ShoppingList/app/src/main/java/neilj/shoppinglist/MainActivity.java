package neilj.shoppinglist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int TEXT_REQUEST = 1;
    private TextView mListHeader;
    private TextView mList;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListHeader = findViewById(R.id.list_header);
        mList = findViewById(R.id.list_text);
        clearButton = (Button) findViewById(R.id.clear_button);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save values in list and visibility
        if (mListHeader.getVisibility() == View.VISIBLE) {
            outState.putBoolean("list_visible", true);
            outState.putString("list_text", mList.getText().toString());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null &&
                savedInstanceState.getBoolean("list_visible")) {
            mListHeader.setVisibility(View.VISIBLE);
            mList.setText(savedInstanceState.getString("list_text"));
            mList.setVisibility(View.VISIBLE);
        }
    }

    public void gotoList(View view) {
        Intent i = new Intent(this, ItemsActivity.class);
        startActivityForResult(i, TEXT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String item = data.getStringExtra(ItemsActivity.EXTRA_LIST_ITEM);
                item += mList.getText();
                mList.setText(item);
                mListHeader.setVisibility(View.VISIBLE);
                mList.setVisibility(View.VISIBLE);

                //update clear button color
                clearButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void clearList(View view) {
        if (mList != null) {
            mList.setText("");
            mList.setVisibility(View.INVISIBLE);
            mListHeader.setVisibility(View.INVISIBLE);

            //hide clear button
            clearButton.setVisibility(View.INVISIBLE);
        }
    }
}
