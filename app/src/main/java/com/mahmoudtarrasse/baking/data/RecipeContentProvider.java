package com.mahmoudtarrasse.baking.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by mahmoud on 03/05/17.
 */

public class RecipeContentProvider extends ContentProvider {

    private DBhelper helper;

    private static final int RECIPES = 100;
    private static final int RECIPE = 101;
    private static final int RECIPE_STEPS = 200;
    private static final int RECIPE_STEP = 201;

    private static UriMatcher matcher = createUriMatcher();

    private static UriMatcher createUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORITY, RecipesContract.PATH_RECIPE, RECIPES);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORITY, RecipesContract.PATH_RECIPE + "/#", RECIPE);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORITY, RecipesContract.PATH_RECIPE + "/#/" + RecipesContract.PATH_STEP, RECIPE_STEPS);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORITY, RecipesContract.PATH_RECIPE + "/#/" + RecipesContract.PATH_STEP + "/#", RECIPE_STEP);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        helper = new DBhelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor ;
        switch (matcher.match(uri)){
            case RECIPES:
                cursor = db.query(RecipesContract.RecipeTable.TABLE_NAME,
                        null, null,
                        null, null,
                        null, null);
                break;
            case RECIPE:
                long id = ContentUris.parseId(uri);
                cursor = db.query(RecipesContract.RecipeTable.TABLE_NAME,
                        null, RecipesContract.RecipeTable.ID_COLUMN + "=" + id,
                        null, null,
                        null, null);
                break;

            default:
                throw new UnsupportedOperationException(uri + " is not supported");
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

        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(RecipesContract.RecipeTable.TABLE_NAME,
                null,
                null);
        for (ContentValues value : values){
            db.insert(RecipesContract.RecipeTable.TABLE_NAME, null, value);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return super.bulkInsert(uri, values);
    }
}
