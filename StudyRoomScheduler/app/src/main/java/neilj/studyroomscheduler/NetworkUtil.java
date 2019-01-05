package neilj.studyroomscheduler;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class NetworkUtil {
    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    //Base url for API (for now my IP since the database is running on my
    //local computer)
    private static final String BASE_URL = "http://172.20.2.64/";
    //Query parameter for getting empty rooms in a hall
    private static final String QUERY_PARAM_EMPTY_ROOM = "hall";
    //Query parameter for roomID to add
    private static final String QUERY_PARAM_ROOM_ID = "roomId";
    //Query parameter for user netID to add
    private static final String QUERY_PARAM_NET_ID = "netId";
    //Query parameter for duration of request in seconds
    private static final String QUERY_PARAM_DURATION = "sec";


    static boolean addOccupiedRoom(String roomId, String netId, String sec) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            String phpURI = "add_occupied_room.php?";
            //build requestURL with Uri
            Uri builtURI = Uri.parse(BASE_URL + phpURI).buildUpon()
                    .appendQueryParameter(QUERY_PARAM_ROOM_ID, roomId)
                    .appendQueryParameter(QUERY_PARAM_NET_ID, netId)
                    .appendQueryParameter(QUERY_PARAM_DURATION, sec)
                    .build();
            URL requestURL = new URL(builtURI.toString());

            //set up connection and make request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Get InputStream
            InputStream inputStream = urlConnection.getInputStream();

            //Create BufferReader from the inputStream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Use StringBuilder to hold incoming response
            StringBuilder builder = new StringBuilder();
            //read input line by line
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                //Append \n to make it more readable during debugging
                //Doesn't affect JSON parsing
                builder.append("\n");
            }

            switch(builder.toString()){
                case "Success!\n":
                    return true;
                case "Error inserting request.\n":
                    return false;
                default: return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Close connection and BufferReader
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
        return false;
    }

    static String getEmptyRooms(String hall) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String roomJSONString = null;

        try {
            String phpURI = "get_empty_rooms.php?";
            //build requestURL with Uri
            Uri builtURI = Uri.parse(BASE_URL + phpURI).buildUpon()
                    .appendQueryParameter(QUERY_PARAM_EMPTY_ROOM, hall)
                    .build();

            URL requestURL = new URL(builtURI.toString());

            //set up connection and make request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Get InputStream
            InputStream inputStream = urlConnection.getInputStream();

            //Create BufferReader from the inputStream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Use StringBuilder to hold incoming response
            StringBuilder builder = new StringBuilder();

            //read input line by line into string
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                //Append \n to make it more readable during debugging
                //Doesn't affect JSON parsing
                builder.append("\n");
            }

            if (builder.length() == 0) {
                //Stream was empty, no need to parse
                return null;
            }

            roomJSONString = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Close connection and BufferReader
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
        Log.d(LOG_TAG, roomJSONString);
        return roomJSONString;
    }
}
