package com.example.ruokapp.Common;

import android.content.Intent;

import com.example.ruokapp.Model.Preferences;
import com.example.ruokapp.Model.Therapists;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";

    public static final String KEY_PREFERENCES_STORE = "PREFERENCES_SAVE";
    public static final String KEY_THERAPIST_LOAD_DONE = "THERAPIST_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_THERAPIST_SELECTED = "THERAPIST_SELECTED";
    public static final int TIME_SLOT_TOTAL = 8;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static Preferences currentPreferences;
    public static int step = 0;
    public static String preference ="";
    public static Therapists currentTherapist;
    public static int currentTimeSlot = -1;
    public static Calendar currentDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

    public static Object convertTimeSlotToString(int slot) {
        switch (slot) {
            case 0:
                return "04/06/2023 9:00-10:00";
            case 1:
                return "04/06/2023 10:00-11:00";
            case 2:
                return "04/12/2023 11:00-12:00";
            case 3:
                return "04/12/2023 12:00-1:00";
            case 4:
                return "04/17/2023 1:00-2:00";
            case 5:
                return "04/17/2023 2:00-3:00";
            case 6:
                return "04/19/2023 3:00-4:00";
            case 7:
                return "04/19/2023 4:00-5:00";
            default:
                return "Closed";

        }
    }
}
