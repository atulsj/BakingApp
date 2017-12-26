package youtubeapidemo.examples.com.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;



class Description implements Parcelable {
    private int mId;
    private String mShortDescription, mDescription, mVideoURL,mThumbnailURL;

    Description(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        mId = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL=thumbnailURL;
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

    String getVideoURL() {
        return mVideoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDescription);
        parcel.writeString(mShortDescription);
        parcel.writeString(mVideoURL);
        parcel.writeInt(mId);
        parcel.writeString(mThumbnailURL);
    }

    protected Description(Parcel in) {
       mDescription=in.readString();
        mShortDescription=in.readString();
        mVideoURL=in.readString();
        mId=in.readInt();
        mThumbnailURL=in.readString();
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
