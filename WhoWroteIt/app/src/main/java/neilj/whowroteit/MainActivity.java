package neilj.whowroteit;

import android.content.Context;
import android.hardware.input.InputManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mBookInput;
    private TextView mAuthorText;
    private TextView mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookInput = findViewById(R.id.bookInput);
        mAuthorText = findViewById(R.id.authorText);
        mTitleText = findViewById(R.id.titleText);
    }

    public void searchBooks(View view) {
        //get the search string from editText
        String queryString = mBookInput.getText().toString();

        //hide the keyboard
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        //check network connection
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if( connMgr != null ) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        //handle cases if not connected to network or if queryString is empty
        if( networkInfo != null && networkInfo.isConnected() && queryString.length() != 0){
            //start asyncTask to fetch info
            new FetchBook(mTitleText, mAuthorText).execute(queryString);
            mAuthorText.setText("");
            mTitleText.setText(R.string.loading);
        } else {
            if (queryString.length() == 0){
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_search_terms);
            } else {
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_network);
            }
        }
    }
}
