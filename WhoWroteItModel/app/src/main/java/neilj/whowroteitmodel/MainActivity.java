package neilj.whowroteitmodel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{
    private TextView mBookInput;
    private TextView mAuthorText;
    private TextView mTitleText;
    private BookDataModel mBookDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookInput = findViewById(R.id.bookInput);
        mAuthorText = findViewById(R.id.authorText);
        mTitleText = findViewById(R.id.titleText);

        mBookDataModel = ViewModelProviders.of(this).get(BookDataModel.class);
        //update UI
        final Observer<String>  bookObserver = this::updateLiveData;
        mBookDataModel.getBookLiveData().observe(this, bookObserver);
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
            mBookDataModel.setQuery(queryString);
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

    private void updateLiveData(String s){
        try {
            //get the array of results returned as a JSONArray
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            int i = 0;
            String title = null;
            String authors = null;
            //find first item with both author and title listed
            while (i < itemsArray.length() && (authors == null && title == null)) {
                //get current item information
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                //Try to get author and title from current item, catch if either
                //field is empty and move on
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //move to next item
                i++;
            }

            if (title != null && authors != null) {
                mTitleText.setText(title);
                mAuthorText.setText(authors);
            } else {
                mTitleText.setText(R.string.no_results);
                mAuthorText.setText("");
            }

        } catch (JSONException e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results
            mTitleText.setText(R.string.no_results);
            mAuthorText.setText("");
            e.printStackTrace();
        }
    }
}
