package youtubeapidemo.examples.com.bakingapp.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * class used for Setting up the FireBase Job Dispatcher.
 */

public class JobDispatcher {

    private static final String REMINDER_JOB_TAG = "recipe_update_reminder_tag";

    private static boolean sInitialized;

    private static final int REFRESH_INTERVAL_SECONDS = 0;
    private static final int SYNC_FLEXTIME_MINUTES =1;
    private static final int SYNC_FLEXTIME_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(SYNC_FLEXTIME_MINUTES));

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTrigger(Trigger.executionWindow(REFRESH_INTERVAL_SECONDS,SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .build();
        dispatcher.schedule(constraintReminderJob);
        sInitialized = true;
    }

}
