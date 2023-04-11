package com.example.ruokapp.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruokapp.Adapter.MyTimeSlotAdapter;
import com.example.ruokapp.Common.Common;
import com.example.ruokapp.Common.SpacesItemDecoration;
import com.example.ruokapp.Interface.ITimeSlotLoadListener;
import com.example.ruokapp.Model.TimeSlot;
import com.example.ruokapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    DocumentReference therapistDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    Calendar selected_date;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendar)
    CalendarView calendar;
    @BindView(R.id.date_view)
    TextView date_view;

    SimpleDateFormat simpleDateFormat;



    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0); //Add current date
            loadAvailableTimeSlotTherapist(Common.currentTherapist.getTherapistId(),simpleDateFormat.format(date.getTime()));
            

        }
    };

    private void loadAvailableTimeSlotTherapist(String therapistId, final String bookDate) {

        //dialog.show();

        //Preferences/In-Person/Branch/FCpHsrqHr2yVmiaOnmBB/Therapists/RXPICqKFcBd6ln2gA09z

        therapistDoc = FirebaseFirestore.getInstance()
                .collection("Preferences")
                .document(Common.preference)
                .collection("Branch")
                .document(Common.currentPreferences.getPreferencesId())
                .collection("Therapists")
                .document(Common.currentTherapist.getTherapistId());

        //Get information of therapist
        therapistDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists())
                    {
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("Preferences")
                                .document(Common.preference)
                                .collection("Branch")
                                .document(Common.currentPreferences.getPreferencesId())
                                .collection("Therapists")
                                .document(Common.currentTherapist.getTherapistId())
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty())
                                        iTimeSlotLoadListener.onTimeSlotEmpty();

                                    else
                                    {
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document: task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }

            }
        });

    }


    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance() {
        if(instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        iTimeSlotLoadListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot,new IntentFilter((Common.KEY_DISPLAY_TIME_SLOT)));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");  //04_04_2023

        //dialog = new SpotsDialog(getContext()); //IF PROBLEM WITH DISPLAYING THIS COULD BE ITTTTTTTTTTTTTTTT

        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE,0); //CURRENT DATE ???


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View v = inflater.inflate(R.layout.fragment_booking_step_three,container,false);

         recycler_time_slot = (RecyclerView) v.findViewById(R.id.recycler_time_slot);

         recycler_time_slot.setHasFixedSize(true);
         recycler_time_slot.setLayoutManager(new GridLayoutManager(getActivity(),2));
         recycler_time_slot.addItemDecoration(new SpacesItemDecoration(4));

         calendar = v.findViewById(R.id.calendar);
         date_view = v.findViewById(R.id.date_view);

         calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
             @Override
             public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                 /** I changed the view to itemView make sure that if that will make anything weird*/

                 String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                 // set this date in TextView for Display
                 date_view.setText(Date);

                 loadAvailableTimeSlotTherapist(Common.currentTherapist.getTherapistId(),simpleDateFormat.format(selected_date.getTime()));
                 

             }

        });


         return v;




    }

    /** THIS SECTION THE INIT WILL MAYBE BE A PROBLEM CHECK IT OUT AGAIN WHEN NOT TIRED*/






    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {

        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(), timeSlotList);
        recycler_time_slot.setAdapter(adapter);

        //dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadFailed(String message) {

        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        //dialog.dismiss();

    }

    @Override
    public void onTimeSlotEmpty() {

        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        //dialog.dismiss();

    }
}
