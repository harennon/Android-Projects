package neilj.studyroomscheduler;

import android.os.Parcel;
import android.os.Parcelable;

class Hall implements Parcelable {

    private String title;
    private final int imageResource;

    Hall(String title, int imageResource){
        this.title = title;
        this.imageResource = imageResource;
    }

    private Hall(Parcel in) {
        title = in.readString();
        imageResource = in.readInt();
    }

    public static final Creator<Hall> CREATOR = new Creator<Hall>() {
        @Override
        public Hall createFromParcel(Parcel in) {
            return new Hall(in);
        }

        @Override
        public Hall[] newArray(int size) {
            return new Hall[size];
        }
    };

    String getTitle(){ return title; }

    int getImageResource(){ return imageResource; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(imageResource);
    }
}
