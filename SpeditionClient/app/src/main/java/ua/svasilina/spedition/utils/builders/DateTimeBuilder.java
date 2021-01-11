package ua.svasilina.spedition.utils.builders;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeBuilder {

    private final SimpleDateFormat format;

    @SuppressLint("SimpleDateFormat")
    public DateTimeBuilder(String pattern){
        format = new SimpleDateFormat(pattern);
    }

    public String build(Date date){
        return format.format(date);
    }

    public String build(Calendar calendar){
        return build(calendar.getTime());
    }
}
