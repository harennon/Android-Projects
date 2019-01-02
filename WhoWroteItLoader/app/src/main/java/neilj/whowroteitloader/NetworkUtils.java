package neilj.whowroteitloader;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    //Base url for Google Books API
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    //Parameter for the query string
    private static final String QUERY_PARAM = "q";
    //Parameter that limits the search result
    private static final String MAX_RESULTS = "maxResults";
    //Parameter to filter by print type
    private static final String PRINT_TYPE = "printType";

    static String getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try {
            //build requestURL with Uri
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();
            URL requestURL = new URL(builtURI.toString());

            //set up connection and make request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Get InputString
            InputStream inputStream = urlConnection.getInputStream();

            //create buffer reader from the input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //use StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            //read input line by line into string
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                //Since its a JSON, adding a newline is unnecessary (it won't affect parsing)
                //but it does make debugging *alot* easier if you print out the completed buffer
                //for debugging.
                builder.append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty, no point parsing
                return null;
            }

            bookJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close connection and BufferReader
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, bookJSONString);
        return bookJSONString;
    }
}
