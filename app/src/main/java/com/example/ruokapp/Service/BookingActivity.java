package com.example.ruokapp.Service;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.ruokapp.Adapter.MyViewPagerAdapter;
import com.example.ruokapp.Common.Common;
import com.example.ruokapp.Common.NonSwipeViewPager;
import com.example.ruokapp.Model.Therapists;
import com.example.ruokapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends FragmentActivity {

    LocalBroadcastManager localBroadcastManager;

    AlertDialog dialog; //DIALOG CREATED A PROBLEM WE CAN TRY TO FIX IT LATER NOT IMPORTANT RIGHT NOW
    CollectionReference therapistsRef;



    private FragmentStateAdapter pagerAdapter;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)   //The NONSWIPEVIEWPAGER IS NOT WORKING
    ViewPager2 viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;


    @OnClick(R.id.btn_previous_step)
    void previousStep() {
        if(Common.step == 3 || Common.step > 0)
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
        }
    }

    @OnClick(R.id.btn_next_step)
    void nextClick() {
        if((Common.step < 3) || Common.step == 0)
        {
            Common.step++;
            if(Common.step == 1)
            {
                if(Common.currentPreferences != null)
                    loadTherapistByPreferences(Common.currentPreferences.getPreferencesId());
            }
            else if(Common.step == 2)  //PICK TIME SLOT
            {
                if(Common.currentTherapist != null)
                    loadTimeSlotofTherapist(Common.currentTherapist.getTherapistId());
            }
            else if(Common.step == 3)  //Confirm
            {
                if(Common.currentTimeSlot != -1)
                    confirmBooking();
            }

            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmBooking() {

        //SEND BROADCAST
        Intent intent  = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotofTherapist(String therapistId) {
        //Send Local Broadcast to Fragment step 3
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTherapistByPreferences(String preferencesId) {

        /**dialog.show();*/

        //Now we get all therapists
        ///Preferences/In-Person/Branch/FCpHsrqHr2yVmiaOnmBB/Therapists

        if(!TextUtils.isEmpty(Common.preference))
        {
            therapistsRef = FirebaseFirestore.getInstance()
                    .collection("Preferences")
                    .document(Common.preference)
                    .collection("Branch")
                    .document(preferencesId)
                    .collection("Therapists");

            therapistsRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            ArrayList<Therapists> therapists = new ArrayList<>();
                            for(QueryDocumentSnapshot therapistsSnapShot:task.getResult())
                            {
                                Therapists therapists1 = therapistsSnapShot.toObject(Therapists.class);
                                therapists1.setPassword("");
                                therapists1.setTherapistId(therapistsSnapShot.getId());

                                therapists.add(therapists1);

                            }

                            //Send Broadcast to BookingStep2Fragment
                            Intent intent = new Intent(Common.KEY_THERAPIST_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_THERAPIST_LOAD_DONE, therapists);

                            localBroadcastManager.sendBroadcast(intent);

                            /**dialog.dismiss();*/

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            /**dialog.dismiss();*/
                        }
                    });

        }


    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if(step == 1)
                Common.currentPreferences = intent.getParcelableExtra(Common.KEY_PREFERENCES_STORE);
            else if (step == 2)
                Common.currentTherapist = intent.getParcelableExtra(Common.KEY_THERAPIST_SELECTED);
            else if (step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);



            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        //THIS IS GOING TO ASK FOR PERMISSION TO ACCESS YOUR CALENDAR


        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);


        ButterKnife.bind(BookingActivity.this);

        /**dialog = new SpotsDialog(getApplicationContext()); //IF YOU HAVE PROBLEMS IT COULD BE THIS ONE*/


        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();
        
        //VIEW

        viewPager.setOffscreenPageLimit(4); //WE HAVE FOUR FRAGMENTS SO THIS LIMITS IT



        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_DRAGGING && viewPager.getCurrentItem() == 0) {
                    viewPager.setUserInputEnabled(false);
                } else {
                    viewPager.setUserInputEnabled(true);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                //Show step
                stepView.go(position,true);

                if(position == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);


                //SET DISABLE BUTTON NEXT HERE
                btn_next_step.setEnabled(false);

                setColorButton();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private void setColorButton() {

        if(btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.color.pink);
        }
        else
        {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }

        if(btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.color.pink);
        }
        else
        {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }

    }

    private void setupStepView() {

        List<String> stepList = new ArrayList<>();
        stepList.add("Location");
        stepList.add("Therapist");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);

    }
}