package com.example.ruokapp.Interface;

import com.example.ruokapp.Model.Preferences;

import java.util.List;

public interface IBranchLoadListener {

    void onBranchLoadSuccess(List<Preferences> preferencesList);
    void onBranchLoadFailed(String message);
}
