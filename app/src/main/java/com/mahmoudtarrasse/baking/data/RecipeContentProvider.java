package com.mahmoudtarrasse.baking.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.FeatureInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import timber.log.Timber;


/**
 * Created by mahmoud on 03/05/17.
 */

public class RecipeContentProvider extends ContentProvider {
    public static final String JSON_DATA = "json_data";

    private static final int RECIPES = 100;
    private static final int RECIPE = 101;
    private static final int RECIPE_STEPS = 200;
    private static final int RECIPE_STEP = 201;

    private File dataFile ;

    private static UriMatcher matcher = createUriMatcher();

    private static UriMatcher createUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORTY, RecipesContract.PATH_RECIPE, RECIPES);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORTY, RecipesContract.PATH_RECIPE + "/#", RECIPE);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORTY, RecipesContract.PATH_RECIPE + "/#/" + RecipesContract.PATH_STEP, RECIPE_STEPS);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORTY, RecipesContract.PATH_RECIPE + "/#/" + RecipesContract.PATH_STEP + "/#", RECIPE_STEP);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        dataFile = new File(getContext().getFilesDir(), RecipesContract.FILE_NAME);
        if (! dataFile.exists()){
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(RecipesContract.RecipeFile.MATRIX_CURSOR_COLUMNS);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        String json = "";
        JSONArray dataArray = null;
        FileReader reader = null;
        try {
            if (! dataFile.exists()){
                dataFile.createNewFile();
            }
            reader = new FileReader(dataFile);
            int a;
            StringBuilder dataBuilder = new StringBuilder();
            while ((a = reader.read()) != -1){
                dataBuilder.append((char) a);
            }
            dataArray = new JSONArray(dataBuilder.toString());
            switch (matcher.match(uri)){
                case RECIPES:
                    for (int i = 0; i<dataArray.length();i++){
                        cursor.addRow(new String[] {dataArray.get(i).toString()});

                    }
                    break;
                default:
                    for (int i = 0; i<dataArray.length();i++){
                        cursor.addRow(new String[] {dataArray.get(i).toString()});

                    }
            }
            return cursor;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)){
            case RECIPES:
                break;
            default:

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        String json = values[0].getAsString(JSON_DATA);
        try {
            if (! dataFile.exists()){
                dataFile.createNewFile();
            }
            FileWriter writer = new FileWriter(dataFile, false);
            writer.write(json.toCharArray());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return super.bulkInsert(uri, values);
    }
}
