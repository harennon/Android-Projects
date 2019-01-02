package neilj.recyclerview;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private LinkedList<String> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get column count
        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

        //populate mWordList
        initializeWordList();

        //instantiate fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wordListSize = mWordList.size();
                //add new word to wordList
                mWordList.addLast(" + Word" + wordListSize);
                //notify adapter that data has been changed
                mRecyclerView.getAdapter().notifyItemInserted(wordListSize);
                //scroll to bottom
                mRecyclerView.smoothScrollToPosition(wordListSize);
            }
        });

        //instantiate RecyclerView
        //get handle of the RecyclerView
        mRecyclerView = findViewById(R.id.recyclerview);
        //create adapter and supply data to be displayed
        mAdapter = new WordListAdapter(this, mWordList);
        //connect adapter with the reqcyclerview
        mRecyclerView.setAdapter(mAdapter);
        //give the RecyclerView a default layout manager
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> listToSave = new ArrayList<>(mWordList);
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putStringArrayList("word_list",listToSave);
        outState.putParcelable("saved_state", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mWordList = new LinkedList<>(savedInstanceState.getStringArrayList("word_list"));
            mListState = savedInstanceState.getParcelable("saved_state");
            mAdapter = new WordListAdapter(this, mWordList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mListState != null){
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            //redefine mWordList
            initializeWordList();
            //notify adapter that mWordList has been changed
            mAdapter.notifyDataSetChanged();
            //scroll to top
            mRecyclerView.scrollToPosition(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeWordList(){
        mWordList.clear();
        for (int i = 0; i < 20; i++){
            mWordList.addLast("Word" + i);
        }
    }
}
