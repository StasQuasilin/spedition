package ua.svasilina.spedition.utils;

import android.content.Context;
import android.content.Intent;

import ua.svasilina.spedition.activity.ReportEdit;
import ua.svasilina.spedition.activity.ReportShow;

import static ua.svasilina.spedition.constants.Keys.ID;

public class Opener {
    public static void openReport(Context context, boolean done, String uuid){
        Intent intent = new Intent();
        if (done){
            intent.setClass(context, ReportShow.class);
        } else {
            intent.setClass(context, ReportEdit.class);
        }

        intent.putExtra(ID, uuid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
