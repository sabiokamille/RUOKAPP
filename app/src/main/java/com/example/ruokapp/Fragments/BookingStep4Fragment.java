package com.example.ruokapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.ruokapp.Common.Common;
import com.example.ruokapp.Model.BookingInformation;
import com.example.ruokapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    @BindView(R.id.txt_booking_therapist_text)
    TextView txt_booking_therapist_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_therapy_address)
    TextView txt_therapy_address;
    @BindView(R.id.txt_therapyplace_name)
    TextView txt_therapyplace_name;
    @BindView(R.id.txt_therapy_open_hours)
    TextView txt_therapy_open_hours;
    @BindView(R.id.txt_therapy_number)
    TextView txt_therapy_number;
    @BindView(R.id.txt_therapy_website)
    TextView txt_therapy_website;

    @OnClick(R.id.btn_confirm)
    void confirmBooking() {

        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setTherapistId(Common.currentTherapist.getTherapistId());
        bookingInformation.setTherapistName(Common.currentTherapist.getName());
        bookingInformation.setPreferencesId(Common.currentPreferences.getPreferencesId());
        bookingInformation.setPreferencesName(Common.currentPreferences.getPreferencesId());
        bookingInformation.setPreferencesAddress(Common.currentPreferences.getAddress());
        bookingInformation.setTime(new StringBuilder(String.valueOf(Common.convertTimeSlotToString(Common.currentTimeSlot)))
                .append("at")
                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //SUBMIT TO THERAPIST DOCUMENT
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("Preferences")
                .document(Common.preference)
                .collection("Branch")
                .document(Common.currentPreferences.getPreferencesId())
                .collection("Therapists")
                .document(Common.currentTherapist.getTherapistId())
                .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        //Write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getActivity().finish(); //Closes activity
                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            setData();

        }
    };

    private void setData() {

        txt_booking_therapist_text.setText(Common.currentTherapist.getName());
        //IF WE HAVE A PROBLEM GO BACK TO VIDEO 23:00 FOR STRING BUILDER CHECK IT OUT
        txt_booking_time_text.setText(new StringBuilder(String.valueOf(Common.convertTimeSlotToString(Common.currentTimeSlot)))
                .append("at")
                .append(simpleDateFormat.format(Common.currentDate.getTime())));

        txt_therapy_number.setText(Common.currentPreferences.getAddress());
        txt_therapy_website.setText(Common.currentPreferences.getWebsite());
        txt_therapyplace_name.setText(Common.currentPreferences.getName());
        txt_therapy_open_hours.setText(Common.currentPreferences.getOpenHours());

    }


    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {
        if(instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));
    }


    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container,false);

        unbinder = ButterKnife.bind(this,itemView);

        return itemView;
    }
}
