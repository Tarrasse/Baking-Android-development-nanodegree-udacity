package com.mahmoudtarrasse.baking.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mahmoud on 3/27/2017.
 */
public class RecipeAuthenticatorService extends Service {

    private RecipeAuthenticator mAuthenticator;


    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new RecipeAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mAuthenticator.getIBinder();
    }
}
