package youtubeapidemo.examples.com.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;



class Recipes implements Parcelable {
    private int id, servings;
    private String dish_Name;
    private String imageUrl;
    private ArrayList<String> mIngredients;

    Recipes(int id, String dish_name, int servings, String image_url, ArrayList<String> ingredients) {
        this.id = id;
        this.dish_Name = dish_name;
        this.servings = servings;
        this.imageUrl = image_url;
        mIngredients=ingredients;
    }

    public int getId() {
        return id;
    }

    int getServings() {
        return servings;
    }

    String getDish_Name() {
        return dish_Name;
    }

    String getImageUrl() {
        return imageUrl;
    }

    ArrayList<String> getIngredients(){
        return mIngredients;
    }

    public String getRecipeIngredients(int pos){
        return mIngredients.get(pos);
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
        parcel.writeStringList(mIngredients);
    }

    private Recipes(Parcel parcel) {
        id = parcel.readInt();
        dish_Name = parcel.readString();
        servings = parcel.readInt();
        imageUrl = parcel.readString();
        parcel.readStringList(mIngredients);
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
