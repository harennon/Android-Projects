package neilj.studyroomscheduler;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class RoomsLiveData extends LiveData<ArrayList<Room>> {

    private String mQuery;

    RoomsLiveData(){
        loadRooms();
    }

    void setQuery(String q){
        mQuery = q;
        loadRooms();
    }

    private void loadRooms(){
        if(mQuery != null){
            new AsyncFetchRooms().execute(mQuery);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncFetchRooms extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return NetworkUtil.getEmptyRooms(strings[0]);
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            setValue(parseRooms(s));
        }

        private ArrayList<Room> parseRooms(String s){
            ArrayList<Room> rooms = new ArrayList<>();
            try{
                //get array of results returned as a JSONArray
                JSONArray roomsJSON = new JSONArray(s);

                //loop through JSONArray to instantiate Room objects
                for(int i = 0; i < roomsJSON.length(); i++){
                    JSONObject roomJSON = roomsJSON.getJSONObject(i);

                    //extra info from object
                    rooms.add(new Room(
                            roomJSON.getInt("room_id"),
                            roomJSON.getString("hall"),
                            roomJSON.getInt("room_num")));
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return rooms;
        }
    }
}
