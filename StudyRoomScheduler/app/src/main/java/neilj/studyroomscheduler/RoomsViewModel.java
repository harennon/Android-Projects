package neilj.studyroomscheduler;

import android.arch.lifecycle.ViewModel;

public class RoomsViewModel extends ViewModel {
    private RoomsLiveData roomsLiveData;

    public void setQuery(String query){
        roomsLiveData.setQuery(query);
    }

    public RoomsLiveData getRoomsLiveData() {
        if(roomsLiveData == null){
            roomsLiveData = new RoomsLiveData();
        }
        return roomsLiveData;
    }
}
