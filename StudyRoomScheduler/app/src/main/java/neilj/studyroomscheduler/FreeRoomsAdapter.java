package neilj.studyroomscheduler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

class FreeRoomsAdapter extends RecyclerView.Adapter<FreeRoomsAdapter.RoomViewHolder> {

    private Context mContext;
    private ArrayList<Room> mFreeRoomList;

    FreeRoomsAdapter(Context context, ArrayList<Room> rooms){
        mContext = context;
        mFreeRoomList = rooms;
    }

    @NonNull
    @Override
    public FreeRoomsAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = LayoutInflater.from(mContext).inflate(
                R.layout.room_item, viewGroup, false);
        return new RoomViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeRoomsAdapter.RoomViewHolder viewHolder, int i) {
        Room mCurrent = mFreeRoomList.get(i);
        viewHolder.bindTo(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mFreeRoomList.size();
    }

    public void setData(ArrayList<Room> rs){
        mFreeRoomList = rs;
    }

    class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mRoomTextView;

        /**
         * Constructor of RoomViewHolder, used in onCreateViewHolder()
         *
         * @param itemView The root view of room_item.xml
         */
        RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            //init Views
            mRoomTextView = itemView.findViewById(R.id.room_textview);

            //set onclick to whole view
            itemView.setOnClickListener(this);
        }

        /**
         * Update UI of room_item with values from Room r.
         *
         * @param r Room object passed in by the Adapter
         */
        void bindTo(Room r){
            //populate Views
            String roomTitle = StringUtils.capitalize(r.getHall() + " " + r.getRoomNum());
            mRoomTextView.setText(roomTitle);
        }

        /**
         *
         * @param v The single room View that is being clicked
         */
        @Override
        public void onClick(View v) {
            //Toast!
            String text = "You've clicked on " + mRoomTextView.getText().toString() + ".";
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();

            //get position of item clicked
            int pos = getLayoutPosition();
            //use that to access affected item in mFreeRoomList
            Room r = mFreeRoomList.get(pos);
            int roomNum = r.getRoomNum();
            int roomId = r.getRoomId();
            //create intent to OrderActivity
        }
    }
}
