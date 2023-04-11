package com.example.ruokapp.Interface;

import com.example.ruokapp.Model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {

    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotEmpty();
}
