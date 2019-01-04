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

    private static URL getEmptyRoomsURL(String hall){
        URL requestURL = null;
        try {
            String phpURI = "get_empty_rooms.php?";
            //build requestURL with Uri
            Uri builtURI = Uri.parse(BASE_URL + phpURI).buildUpon()
                    .appendQueryParameter(QUERY_PARAM_EMPTY_ROOM, hall)
                    .build();

            requestURL = new URL(builtURI.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return  requestURL;
    }

    static String getEmptyRooms(String phpFile, String[] args){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String roomJSONSTtring = null;

        try{
            URL requestURL = null;
            switch (phpFile){
                case "get_empty_rooms.php":
                    requestURL = getEmptyRoomsURL(args[0]);
            }

            if(requestURL == null){
                throw new IllegalArgumentException();
            }
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
            while ((line = reader.readLine()) != null){
                builder.append(line);

                //Append \n to make it more readable during debugging
                //Doesn't affect JSON parsing
                builder.append("\n");
            }

            if(builder.length() == 0){
                //Stream was empty, no need to parse
                return null;
            }

            roomJSONSTtring = builder.toString();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //Close connection and BufferReader
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, roomJSONSTtring);
        return roomJSONSTtring;
    }

    static void addOccupiedRoom(int room_id, String net_id, String end_time){}
}
