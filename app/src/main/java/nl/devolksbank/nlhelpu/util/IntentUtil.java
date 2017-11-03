package nl.devolksbank.nlhelpu.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

/**
 * Created by Evelien Surstedt on 3-11-2017.
 */

public class IntentUtil {
    public static boolean isIntentHandlerPresent(final Intent intent, Context context) {
        Log.d("DocCollectionActivity", "Checking whether an intent handler is present");
        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> resolveInfoItems = manager.queryIntentActivities(intent, 0);
        if (resolveInfoItems.size() > 0) {
            Log.d("DocCollectionActivity", "Intent handler is present");
            return true;
        } else {
            Log.d("DocCollectionActivity", "Intent handler is not present");
            return false;
        }
    }
}
