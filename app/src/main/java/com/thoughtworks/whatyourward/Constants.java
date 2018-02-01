package com.thoughtworks.whatyourward;

/**
 * Created by Chandru on 09/11/17.
 */
public class Constants {

    public static final String PREF_FILE_NAME = "thoughtworks_pref_file";


    public static final String CATEGORY_OFFLINE_FILE = "productcategory.txt";
    public static final String DATABASE_NAME =  "thoughtworks_db";


    public static final int SPLASH_TIME_OUT = 2000;


    public interface INTENT_KEY {
        String PRODUCT_DETAILS = "Constants.INTENT_KEY.PRODUCT_DETAILS";

        String PRODUCT = "Constants.INTENT_KEY.PRODUCT";

        String IS_SPLASH_LAUNCH = "Constants.INTENT_KEY.IS_SPLASH_LAUNCH";

    }

    public interface SHARED_PREF {
        String SCREEN_ID = "Constants.SHARED_PREF.SCREEN_ID";

    }


    public interface TOOLBAR {
        String TITLE_PRODUCT_LIST = "Product List";
        String TITLE_PRODUCT_DETAILS = "Product Details";
        String TITLE_CART_LIST = "My Carts";

    }


    public interface SCREENS {
        int CART_LIST = 3;
        int PRODUCT_LIST = 1;
        int PRODUCT_DETAILS = 2;

    }

}
