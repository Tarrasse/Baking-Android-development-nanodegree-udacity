package com.mahmoudtarrasse.baking.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utilty;
import com.mahmoudtarrasse.baking.data.RecipeContentProvider;
import com.mahmoudtarrasse.baking.data.RecipesContract;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import timber.log.Timber;

/**
 * Created by mahmoud on 3/25/2017.
 */
public class RecipeSyncAdapter extends AbstractThreadedSyncAdapter {

    private Context mContext;
    private ContentResolver mContentResolver;

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public RecipeSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public RecipeSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle bundle,
                              String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        Timber.plant(new Timber.DebugTree());

        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/5907926b_baking/baking.json";
        OkHttpClient client = new OkHttpClient();
        ;

        Request request = new Request.Builder()
                .url(url)
                .build();

        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
            String body = response.body().string();
            response.close();

            ContentValues values = new ContentValues();
            values.put(RecipeContentProvider.JSON_DATA, body);
            getContext().getContentResolver().bulkInsert(RecipesContract.RecipeTable.CONTENT_URI, new ContentValues[]{values});
            Timber.d(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Timber.d("configurePeriodicSync");
        Account account = getSyncAccount(context);
        String authority = Utilty.CONTENT_AUTHORITY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
        Timber.d("configurePeriodicSync");
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                Utilty.CONTENT_AUTHORITY, bundle);
        Timber.d("syncImmediately");

    }

    //tested
    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), Utilty.CONTENT_AUTHORITY);

        Timber.d(newAccount.toString());
        if ( null == accountManager.getPassword(newAccount) ) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        Timber.d("account created");
        RecipeSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, Utilty.CONTENT_AUTHORITY, true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        Timber.d("in init");
        getSyncAccount(context);
    }

}
















