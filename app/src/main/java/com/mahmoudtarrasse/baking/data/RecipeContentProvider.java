package com.mahmoudtarrasse.baking.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by mahmoud on 03/05/17.
 */

public class RecipeContentProvider extends ContentProvider {

    private DBhelper dBhelper;


    private static final int RECIPES = 100;
    private static final int RECIPE = 101;
    private static final int RECIPE_STEPS = 200;
    private static final int RECIPE_STEP = 201;


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
        dBhelper = new DBhelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor returnCursor ;
        switch (matcher.match(uri)){
            case RECIPES:
                returnCursor = db.query(RecipesContract.RecipeTable.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;
            default:
                returnCursor = db.query(RecipesContract.RecipeTable.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        switch (matcher.match(uri)){
            case RECIPES:
                db.insert(RecipesContract.RecipeTable.TABLE_NAME, null, values);
                break;
            default:
                db.insert(RecipesContract.RecipeTable.TABLE_NAME, null, values);
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
}
