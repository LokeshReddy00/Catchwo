package com.buddies.catchwo.Support;

import com.buddies.catchwo.Model.ProfModel;
import com.buddies.catchwo.Model.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public interface ITimeSlotLoadListener {

    void ITimeSlotLoadListenerSuccess(ArrayList<TimeSlot> areaNameList);
    void ITimeSlotLoadListenerFailed(String message);
    void TimeslotEmpty();
}
