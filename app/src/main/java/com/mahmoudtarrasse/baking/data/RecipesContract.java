package com.mahmoudtarrasse.baking.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mahmoud on 03/05/17.
 */

public class RecipesContract {

    public static String CONTENT_AUTHORTY = "com.mahmoudtarrasse.baking";

    public static String PATH_RECIPE = "recipe";
    public static String PATH_STEP = "step";

    private static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORTY);

    public static class RecipeTable implements BaseColumns{

        public static String TABLE_NAME = "recipes";

        public static String ID_COLUMN = "id";
        public static String NAME_COLUMN = "name";
        public static String SERVINGS_COLUMN = "servings";
        public static String IMAGE_COLUMN = "image";

    }


    public static class IngredientTable implements BaseColumns{

        public static String TABLE_NAME = "ingredient";

        public static String NAME_COLUMN = "name";
        public static String MEASURE_COLUMN = "measure";
        public static String RECIPE_COLUMN = "recipe";

    }

    public static class StepTable implements BaseColumns{

        public static String TABLE_NAME = "step";


        public static String LOCAL_ID_COLUMN = "id";
        public static String VIDEO_URL_COLUMN = "measure";
        public static String SHORT_DECRYPTION_COLUMN = "short_description";
        public static String THUMBNAIL_URL_COLUMN = "thumbnailURL";
        public static String RECIPE_COLUMN = "recipe";

    }






}
