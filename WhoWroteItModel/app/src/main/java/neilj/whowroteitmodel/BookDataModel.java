package neilj.whowroteitmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

public class BookDataModel extends ViewModel {
    private MutableLiveData<String> bookLiveData;
    private String mQuery;

    public void setQuery(String query) {
        mQuery = query;
        loadBook();
    }

    private void loadBook(){
        if(mQuery != null) {
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... strings) {
                    return NetworkUtils.getBookInfo(mQuery);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    bookLiveData.setValue(s);
                }
            }.execute(mQuery);
        }
    }

    public LiveData<String> getBookLiveData(){
        if(bookLiveData == null){
            bookLiveData = new MutableLiveData<>();
        }
        loadBook();
        return bookLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
