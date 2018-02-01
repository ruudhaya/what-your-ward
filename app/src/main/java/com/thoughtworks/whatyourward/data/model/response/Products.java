package com.thoughtworks.whatyourward.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    /**
     * product_details : {"image_url":"","price":"4500","name":"LG Microwave Oven"}
     * name : Microwave oven
     */

    private ProductDetails product_details;
    private String name;
    private String image_url;
    private String product_id;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public ProductDetails getProduct_details() {
        return product_details;
    }

    public void setProduct_details(ProductDetails product_details) {
        this.product_details = product_details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public Products() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.product_details, flags);
        dest.writeString(this.name);
        dest.writeString(this.image_url);
        dest.writeString(this.product_id);
    }

    protected Products(Parcel in) {
        this.product_details = in.readParcelable(ProductDetails.class.getClassLoader());
        this.name = in.readString();
        this.image_url = in.readString();
        this.product_id = in.readString();
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel source) {
            return new Products(source);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };
}