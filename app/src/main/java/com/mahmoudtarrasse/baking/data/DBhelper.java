package com.mahmoudtarrasse.baking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vansuita.sqliteparser.SqlParser;

import com.mahmoudtarrasse.baking.data.RecipesContract.*;

/**
 * Created by mahmoud on 03/05/17.
 */

public class DBhelper extends SQLiteOpenHelper {
    public static String DB_NAME = "baking.app";
    public static int VERSION = 1;


    public DBhelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTAbleRecipe = SqlParser.create(RecipeTable.TABLE_NAME)
                .pk(RecipeTable.ID_COLUMN)
                .str(RecipeTable.NAME_COLUMN)
                .num(RecipeTable.SERVINGS_COLUMN)
                .str(RecipeTable.IMAGE_COLUMN)
                .build();


        String createTableIngredient = SqlParser.create(IngredientTable.TABLE_NAME)
                .pk(IngredientTable._ID)
                .str(IngredientTable.NAME_COLUMN)
                .str(IngredientTable.MEASURE_COLUMN)
                .num(IngredientTable.RECIPE_COLUMN)
                .build();

        String createTableSteps = SqlParser.create(StepTable.TABLE_NAME)
                .pk(StepTable._ID)
                .num(StepTable.LOCAL_ID_COLUMN)
                .str(StepTable.SHORT_DECRYPTION_COLUMN)
                .str(StepTable.VIDEO_URL_COLUMN)
                .str(StepTable.THUMBNAIL_URL_COLUMN)
                .num(StepTable.RECIPE_COLUMN)
                .build();

        db.execSQL(createTAbleRecipe);
        db.execSQL(createTableIngredient);
        db.execSQL(createTableSteps);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(String.format("DROP TABLE IF EXISTS %s ;", RecipeTable.TABLE_NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s ;", IngredientTable.TABLE_NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s ;", StepTable.TABLE_NAME));

        onCreate(db);

    }
}
