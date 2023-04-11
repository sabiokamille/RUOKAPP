package com.example.ruokapp.Interface;
import java.util.List;


public interface IPreferencesLoadListener {
    void onPreferencesLoadSuccess(List<String> areaNameList);
    void onPreferencesLoadFailed(String message);

}
