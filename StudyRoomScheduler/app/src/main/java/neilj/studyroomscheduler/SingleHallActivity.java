package neilj.studyroomscheduler;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SingleHallActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TextView mTitleTextView;
    private ImageView mHallImageView;
    private FreeRoomsAdapter mAdapter;
    private ArrayList<Room> freeRoomList;
    private RoomsViewModel mRoomsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_hall);

        //initialize views
        mHallImageView = findViewById(R.id.hall_image_single);
        mRecyclerView = findViewById(R.id.empty_rooms_recyclerview);
        mTitleTextView = findViewById(R.id.title_single);

        //get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.EXTRA_HALL_NAME) + " Hall";
        mTitleTextView.setText(title);
        Glide.with(this).load(getIntent().getIntExtra(
                MainActivity.EXTRA_IMAGE_RESOURCE, 0)).into(mHallImageView);

        //initialize freeRoomList
        freeRoomList = new ArrayList<>();

        //get col_count
        int gridColumnCount = getResources().getInteger(R.integer.room_grid_column_count);

        //Setup recylcerView LayoutManager
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

        mAdapter = new FreeRoomsAdapter(this, freeRoomList);

        mRecyclerView.setAdapter(mAdapter);

        //Use ViewModel to load in rooms that are free in hall
        mRoomsViewModel = ViewModelProviders.of(this).get(RoomsViewModel.class);
        String query = intent.getStringExtra(MainActivity.EXTRA_HALL_NAME).toLowerCase();

        final Observer<ArrayList<Room>> roomsObserver = new Observer<ArrayList<Room>>(){

            @Override
            public void onChanged(@Nullable ArrayList<Room> rooms) {
                //update freeRoomList
                freeRoomList = rooms;
                //Update data into adapter
                mAdapter.setData(freeRoomList);
                //update UI using RecyclerView Adapter
                mAdapter.notifyDataSetChanged();
            }
        };
        mRoomsViewModel.getRoomsLiveData().observe(this, roomsObserver);
        mRoomsViewModel.setQuery(query);
    }
}
