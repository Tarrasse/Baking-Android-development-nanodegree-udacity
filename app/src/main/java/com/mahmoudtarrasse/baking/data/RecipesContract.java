package com.mahmoudtarrasse.baking.data;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mahmoud on 03/05/17.
 */

public class RecipesContract {

    public static String FILE_NAME = "data.json";
    public static String CONTENT_AUTHORTY = "com.mahmoudtarrasse.baking";

    public static String PATH_RECIPE = "recipe";
    public static String PATH_STEP = "step";

    private static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORTY);




    public static class RecipeFile{
        public static final String MATIRX_CURSOR_JSON_COLUMNS  = "json";
        public static final int MATRIX_CURSOR_JSON_COLUMNS_INDEX = 0;
        public static final String[] MATRIX_CURSOR_COLUMNS = new String[] {MATIRX_CURSOR_JSON_COLUMNS};
    }

    public static class RecipeTable implements BaseColumns{
        public static Uri CONTENT_URI = BASE_URI.buildUpon().appendEncodedPath(PATH_RECIPE).build();


        public static String TABLE_NAME = "recipes";

        public static String ID_COLUMN = "id";
        public static String NAME_COLUMN = "name";
        public static String SERVINGS_COLUMN = "servings";
        public static String IMAGE_COLUMN = "image";


        public static ContentValues createContentValue(int id, String name, String servings, String image) {
            ContentValues values = new ContentValues();
            values.put(ID_COLUMN, id);
            values.put(NAME_COLUMN, name);
            values.put(SERVINGS_COLUMN, servings);
            values.put(IMAGE_COLUMN, image);
            return values;
        }
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
