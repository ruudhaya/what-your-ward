package com.thoughtworks.whatyourward.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetails implements Parcelable {
    /**
     * image_url :
     * price : 4500
     * name : LG Microwave Oven
     */

    private String image_url;
    private int price;
    private String name;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image_url);
        dest.writeInt(this.price);
        dest.writeString(this.name);
    }

    public ProductDetails() {
    }

    protected ProductDetails(Parcel in) {
        this.image_url = in.readString();
        this.price = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel source) {
            return new ProductDetails(source);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };
}