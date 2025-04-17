package com.eduplatform.util;

import java.util.Date;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static long daysBetween(Date start, Date end) {
        long diffInMillies = end.getTime() - start.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static boolean isOverdue(Date deadline) {
        return new Date().after(deadline);
    }

    public static long minutesRemaining(Date expiryTime) {
        long diffInMillies = expiryTime.getTime() - new Date().getTime();
        return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}