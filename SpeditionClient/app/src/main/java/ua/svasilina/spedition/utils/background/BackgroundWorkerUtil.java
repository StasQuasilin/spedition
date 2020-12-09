package ua.svasilina.spedition.utils.background;

import android.content.Context;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BackgroundWorkerUtil {
    private static final long DURATION = 10;

    private static BackgroundWorkerUtil instance;
    public static BackgroundWorkerUtil getInstance(){
        if (instance == null){
            instance = new BackgroundWorkerUtil();
        }
        return instance;
    }

    public void runWorker(Context context){
        stopWorker(context);

        OneTimeWorkRequest one = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                .addTag(BackgroundWorker.TAG)
                .setInitialDelay(DURATION, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(one);
    }

    public void stopWorker(Context context){
        WorkManager.getInstance(context).cancelAllWorkByTag(BackgroundWorker.TAG);
    }
}
