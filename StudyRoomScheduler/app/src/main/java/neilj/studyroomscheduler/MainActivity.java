package neilj.studyroomscheduler;

import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Hall> mResidentHalls;
    private HallAdapter mAdapter;
    private Parcelable mListState;
    public static final String EXTRA_HALL_NAME =
            "neilj.studyroomscheduler.extra.hall.NAME";
    public static final String EXTRA_IMAGE_RESOURCE =
            "neilj.studyroomscheduler.extra.image.RESOURCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get col_count
        int gridColumnCount = getResources().getInteger(R.integer.hall_grid_column_count);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.halls_recyclerview);

        //Set layoutmanager
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

        //initialize ArrayList that will contain the data
        mResidentHalls = new ArrayList<>();

        //initialize adapter and set it to the RecyclerView
        mAdapter = new HallAdapter(this, mResidentHalls);
        mRecyclerView.setAdapter(mAdapter);

        //get data
        initializeData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("saved_state", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mListState = savedInstanceState.getParcelable("saved_state");
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mListState != null){
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    private void initializeData(){
        //get resources from xml files
        String[] titleList = getResources().getStringArray(R.array.hall_titles);
        TypedArray imageList = getResources().obtainTypedArray(R.array.hall_images);

        //reset existing data
        mResidentHalls.clear();

        //create Arraylist of Hall objects
        for(int i = 0; i < titleList.length; i++){
                mResidentHalls.add(new Hall(titleList[i],
                        imageList.getResourceId(i, 0)));
        }

        //recycle TypedArray
        imageList.recycle();

        //notify adapter of the change
        mAdapter.notifyDataSetChanged();
    }

}
