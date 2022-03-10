package com.buddies.catchwo.Support;

import com.buddies.catchwo.Model.ProfModel;
import com.buddies.catchwo.Model.Professional;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_PROF_PERSON = "PROF_SAVE";
    public static final String KEY_PROF_LOAD_DONE = "PROF_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_PROF_PERSON_SELECTED = "PROF_PERSON_SELECTED";
    public static final int TIME_SLOT_TOTAL = 20;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String KEY_COLLECTION_TOKEN = "fdmToken";
    public static ProfModel currentProf;
    public static int step = 0;
    public static String city = "";
    public static Professional CurrentPer;
    public static int CurrentTimeSlot = -1;
    public static Calendar BookingTime = Calendar.getInstance();
    public static SimpleDateFormat sampleDataFormat = new SimpleDateFormat("dd_MM_yyyy");

    public static String convertTimeSlotToString(int slot) {

        switch (slot){

            case 0:
                return "9:00 - 9:30";
            case 1:
                return "9:30 - 10:00";
            case 2:
                return "10:00 - 10:30";
            case 3:
                return "10:30 - 11:00";
            default:
                return "Closed";
        }

    }
}
