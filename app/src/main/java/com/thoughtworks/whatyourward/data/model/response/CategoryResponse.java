package com.thoughtworks.whatyourward.data.model.response;

import java.util.List;

/**
 * Created by Chandru on 09/11/17.
 */

public class CategoryResponse {


    /**
     * response : {"categories":[{"category":"Electronics","products":[{"product_details":{"image_url":"","price":"4500","name":"LG Microwave Oven"},"name":"Microwave oven"},{"product_details":{"image_url":"","price":"45000","name":"Sony LED"},"name":"Television"},{"product_details":{"image_url":"","price":"9000","name":"Bajaj Vacuum Cleaner"},"name":"Vacuum Cleaner"}]},{"category":"Furniture","products":[{"product_details":{"image_url":"","price":"25000","name":"Rubber wood Table"},"name":"Table"},{"product_details":{"image_url":"","price":"1200","name":"Nilkamal Chair"},"name":"Chair"},{"product_details":{"image_url":"","price":"16000","name":"Bestwave Almirah"},"name":"Almirah"}]}]}
     */

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {
        private List<Categories> categories;

        public List<Categories> getCategories() {
            return categories;
        }

        public void setCategories(List<Categories> categories) {
            this.categories = categories;
        }


    }
}
