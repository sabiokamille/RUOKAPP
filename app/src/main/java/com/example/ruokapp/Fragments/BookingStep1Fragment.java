package com.example.ruokapp.Fragments;

import android.app.AlertDialog;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruokapp.Adapter.MyPreferencesAdapter;
import com.example.ruokapp.Common.Common;
import com.example.ruokapp.Common.SpacesItemDecoration;
import com.example.ruokapp.Interface.IBranchLoadListener;
import com.example.ruokapp.Interface.IPreferencesLoadListener;
import com.example.ruokapp.Model.Preferences;
import com.example.ruokapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;


import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLProtocolException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;


public class BookingStep1Fragment extends Fragment implements IPreferencesLoadListener, IBranchLoadListener {

    CollectionReference preferencesRef;
    CollectionReference branchRef;

    IPreferencesLoadListener iPreferencesLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_preference)
    RecyclerView recycler_preference;

    Unbinder unbinder;

    AlertDialog dialog;




    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance() {
        if(instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesRef = FirebaseFirestore.getInstance().collection("Preferences");
        iPreferencesLoadListener = this;
        iBranchLoadListener = this;

        dialog = new SpotsDialog(getContext()); //make sure to check on this, can be a problem we will see



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container,false);
         unbinder = ButterKnife.bind(this,itemView);

         initView();

         loadPreferences();

         return itemView;

    }

    private void initView() {
        recycler_preference.setHasFixedSize(true);
        recycler_preference.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_preference.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadPreferences() {
        preferencesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please choose your preference");
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                                list.add(documentSnapshot.getId());
                            iPreferencesLoadListener.onPreferencesLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iPreferencesLoadListener.onPreferencesLoadFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void onPreferencesLoadSuccess(List<String> areaNameList) {

        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0)
                {
                    loadBranchofPreferences(item.toString());
                }
                else
                    recycler_preference.setVisibility(View.GONE);
            }
        });

    }

    private void loadBranchofPreferences(String preferencesName) {

        dialog.show();

        Common.preference = preferencesName;

        branchRef = FirebaseFirestore.getInstance()
                .collection("Preferences")
                .document(preferencesName)
                .collection("Branch");

        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Preferences> list = new ArrayList<>();
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                    {
                        Preferences preferences = documentSnapshot.toObject(Preferences.class);
                        preferences.setPreferencesId(documentSnapshot.getId());
                        list.add(preferences);
                    }
                    iBranchLoadListener.onBranchLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onPreferencesLoadFailed(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBranchLoadSuccess(List<Preferences> preferencesList) {

        MyPreferencesAdapter adapter = new MyPreferencesAdapter(getActivity(),preferencesList);
        recycler_preference.setAdapter(adapter);
        recycler_preference.setVisibility(View.VISIBLE);

        dialog.dismiss();

    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }


}
