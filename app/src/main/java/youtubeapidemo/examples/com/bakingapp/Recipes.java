package youtubeapidemo.examples.com.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 1515012 on 09-07-2017.
 */

public class Recipes implements Parcelable {
    private int id, servings;
    private String dish_Name;
    private String imageUrl;

    public Recipes(int id, String dish_name, int servings, String image_url) {
        this.id = id;
        this.dish_Name = dish_name;
        this.servings = servings;
        this.imageUrl = image_url;
    }

    public int getId() {
        return id;
    }

    public int getServings() {
        return servings;
    }

    public String getDish_Name() {
        return dish_Name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(dish_Name);
        parcel.writeInt(servings);
        parcel.writeString(imageUrl);
    }

    private Recipes(Parcel parcel) {
        id = parcel.readInt();
        dish_Name = parcel.readString();
        servings = parcel.readInt();
        imageUrl = parcel.readString();
    }

    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel parcel) {
            return new Recipes(parcel);
        }

        @Override
        public Recipes[] newArray(int i) {
            return new Recipes[i];
        }
    };
}
