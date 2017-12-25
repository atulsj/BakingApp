package youtubeapidemo.examples.com.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 1515012 on 11-07-2017.
 */

public class Description implements Parcelable {
    private int mId;
    private String mShortDescription, mDescription, mVideoURL;

    public Description(int id, String shortDescription, String description, String videoURL) {
        mId = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
    }

    public int getId() {
        return mId;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getVideoURL() {
        return mVideoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDescription);
        parcel.writeString(mShortDescription);
        parcel.writeString(mVideoURL);
        parcel.writeInt(mId);
    }

    protected Description(Parcel in) {
       mDescription=in.readString();
        mShortDescription=in.readString();
        mVideoURL=in.readString();
        mId=in.readInt();
    }

    public static final Creator<Description> CREATOR = new Creator<Description>() {
        @Override
        public Description createFromParcel(Parcel in) {
            return new Description(in);
        }

        @Override
        public Description[] newArray(int size) {
            return new Description[size];
        }
    };
}
