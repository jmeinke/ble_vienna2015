package com.devfest15.blevienna;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.eddystone.Eddystone;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static java.lang.System.currentTimeMillis;


public class RangingActivity extends AppCompatActivity implements BeaconManager.EddystoneListener {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private PersonAdapter personAdapter;
    private String scanId;
    private Realm realm;
    private Person mSelectedPerson;

    static final int REQUEST_ACCOUNT_PICKER = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        // Get a Realm instance for this thread
        realm = Realm.getInstance(this);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        personAdapter = new PersonAdapter(new ArrayList<Person>());
        rv.setAdapter(personAdapter);
        queryPersonsAndUpdateList();

        beaconManager = new BeaconManager(this);
        // Should be invoked in #onCreate.
        beaconManager.setEddystoneListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Should be invoked in #onStart.
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                scanId = beaconManager.startEddystoneScanning();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Should be invoked in #onStop.
        beaconManager.stopEddystoneScanning(scanId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Should be invoked in #onStop.
        beaconManager.stopEddystoneScanning(scanId);
    }

    @Override
    public void onEddystonesFound(List<Eddystone> eddystones) {
        if (eddystones.isEmpty())
            return;

        Person.updatePersons(eddystones, realm);

        queryPersonsAndUpdateList();
    }

    private void queryPersonsAndUpdateList() {
        // update our adapter with the new data
        // Build the query looking at all users:
        RealmQuery<Person> query = realm.where(Person.class);

        // Execute the query:
        RealmResults<Person> persons = query.findAll();
        persons.sort("lastSignalStrength", false);

        List<Person> pList = new ArrayList<>(persons);
        personAdapter.setPersons(pList);
    }

    private void onBeaconSelect(Person person) {
        mSelectedPerson = person;
        startActivityForResult(
                Calendar.getCredential(getApplicationContext()).newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null && mSelectedPerson != null) {
                        mSelectedPerson.setAccountName(accountName);
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(mSelectedPerson);
                        realm.commitTransaction();

                    }
                }
                mSelectedPerson = null;

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
