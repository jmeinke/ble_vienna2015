package com.devfest15.blevienna;

import android.animation.IntArrayEvaluator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.cloud.internal.User;
import com.estimote.sdk.eddystone.Eddystone;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.currentTimeMillis;


public class MonitoringActivity extends AppCompatActivity implements CalendarEventDelegate, BeaconManager.EddystoneListener {
    protected static final String TAG = "MonitoringActivity";

    private Realm realm;
    private List<Calendar> calendarList;
    private BeaconManager beaconManager;
    private String scanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(this);
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
        updateCalendar();
    }

    @Override
    public void calendarDidLoadEvents(Calendar cal, List<String> events) {

    }

    private void updateCalendar() {
        RealmQuery<Person> query = realm.where(Person.class);

        query.isNotNull("accountName");


        RealmResults<Person> persons = query.findAllSorted("lastSignalStrength", false);
        calendarList = new ArrayList<>();
        for (Person p : persons) {
            Calendar cal = new Calendar(p, getApplicationContext());
            cal.delegate = this;
            calendarList.add(cal);

        }
        if (!calendarList.isEmpty()) {
            calendarList.get(0).loadEvents();
        }
    }

}