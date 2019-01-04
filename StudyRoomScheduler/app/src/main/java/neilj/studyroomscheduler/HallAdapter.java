package neilj.studyroomscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class HallAdapter extends RecyclerView.Adapter<HallAdapter.HallViewHolder> {

    private final ArrayList<Hall> mHalls;
    private Context mContext;

    HallAdapter(Context context, ArrayList<Hall> wordList){
        mContext = context;
        mHalls = wordList;
    }

    @NonNull
    @Override
    public HallAdapter.HallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HallViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.hall_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HallAdapter.HallViewHolder viewHolder, int i) {
        Hall mCurrent = mHalls.get(i);
        viewHolder.bindTo(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mHalls.size();
    }


    /**
     * HallViewHolder subclass that represents each row of data
     */
    class HallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mHallImage;
        private TextView mTitle;

        /**
         * Constructor for ViewHolder, used in onCreateViewHolder()
         *
         * @param itemView The rootview of the hall_item.xml layout file
         */
        HallViewHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            mHallImage = itemView.findViewById(R.id.hall_image);
            mTitle = itemView.findViewById(R.id.title);

            //set onclick to entire view
            itemView.setOnClickListener(this);
        }

        void bindTo(Hall h){
            //populate textview with data
            String title = h.getTitle() + " Hall";
            mTitle.setText(title);

            //load image with Glide
            Glide.with(mContext).load(h.getImageResource()).into(mHallImage);
        }


        /**
         * handle onclick to show SingleHallActivity
         *
         * @param v View that is clicked
         */
        @Override
        public void onClick(View v) {
            //make intent
            String text = "You've clicked on " + mTitle.getText().toString() + ".";
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();

            Hall hall = mHalls.get(getAdapterPosition());
            Intent intent = new Intent(mContext, SingleHallActivity.class);
            intent.putExtra(MainActivity.EXTRA_HALL_NAME, hall.getTitle());
            intent.putExtra(MainActivity.EXTRA_IMAGE_RESOURCE, hall.getImageResource());
            mContext.startActivity(intent);
        }
    }
}
