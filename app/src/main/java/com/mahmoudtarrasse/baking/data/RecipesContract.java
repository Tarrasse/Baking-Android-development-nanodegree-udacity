package com.mahmoudtarrasse.baking.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import timber.log.Timber;

/**
 * Created by mahmoud on 03/05/17.
 */

public class RecipesContract {

    public static String CONTENT_AUTHORITY = "com.mahmoudtarrasse.baking";

    public static String PATH_RECIPE = "recipe";
    public static String PATH_STEP = "step";

    private static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static class RecipeTable implements BaseColumns{
        public static Uri CONTENT_URI = BASE_URI.buildUpon().appendEncodedPath(PATH_RECIPE).build();


        public static String TABLE_NAME = "recipes";

        public static String ID_COLUMN = "id";
        public static String NAME_COLUMN = "name";
        public static String SERVINGS_COLUMN = "servings";
        public static String IMAGE_COLUMN = "image";

        public static String INGREDIENTS_JSON = "ing_json";
        public static String STEPS_JSON = "steps_json";


        public static ContentValues createContentValues(int id, String name, int servings, String image, String ingredients, String steps){
            ContentValues values = new ContentValues();
            values.put(ID_COLUMN, id);
            values.put(NAME_COLUMN, name);
            values.put(SERVINGS_COLUMN, servings);
            values.put(IMAGE_COLUMN, image);
            values.put(INGREDIENTS_JSON, ingredients);
            values.put(STEPS_JSON, steps);

            return values;
        }


        public static Uri buildRecipeId(long id){
            Timber.d(ContentUris.withAppendedId(CONTENT_URI, id).toString());
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

//
//    public static class IngredientTable implements BaseColumns{
//        public static String TABLE_NAME = "ingredient";
//
//        public static String NAME_COLUMN = "name";
//        public static String MEASURE_COLUMN = "measure";
//        public static String RECIPE_COLUMN = "recipe";
//
//    }
//
//    public static class StepTable implements BaseColumns{
//
//        public static String TABLE_NAME = "step";
//
//
//        public static String LOCAL_ID_COLUMN = "id";
//        public static String VIDEO_URL_COLUMN = "measure";
//        public static String SHORT_DECRYPTION_COLUMN = "short_description";
//        public static String THUMBNAIL_URL_COLUMN = "thumbnailURL";
//        public static String RECIPE_COLUMN = "recipe";
//
//    }






}
