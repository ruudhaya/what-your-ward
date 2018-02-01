package com.thoughtworks.whatyourward.data.model.response;

import java.util.List;

public class Categories {
            /**
             * category : Electronics
             * products : [{"product_details":{"image_url":"","price":"4500","name":"LG Microwave Oven"},"name":"Microwave oven"},{"product_details":{"image_url":"","price":"45000","name":"Sony LED"},"name":"Television"},{"product_details":{"image_url":"","price":"9000","name":"Bajaj Vacuum Cleaner"},"name":"Vacuum Cleaner"}]
             */

            private String category;
            private List<Products> products;

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public List<Products> getProducts() {
                return products;
            }

            public void setProducts(List<Products> products) {
                this.products = products;
            }


        }