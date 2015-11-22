package com.devfest15.blevienna;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements BeaconManager.EddystoneListener, CalendarEventDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        realm = Realm.getInstance(this);
        beaconManager = new BeaconManager(this);
        // Should be invoked in #onCreate.
        beaconManager.setEddystoneListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, RangingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Realm realm;
    private List<Calendar> calendarList;
    private BeaconManager beaconManager;
    private String scanId;

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
            if (cal.index == 0 && !events.isEmpty()) {
                ((TextView) findViewById(R.id.name_text)).setText(cal.person.getAccountName());
                ((TextView) findViewById(R.id.event_text)).setText(events.get(0));

            }

        if (cal.index == 1 && !events.isEmpty()) {
            ((TextView) findViewById(R.id.name_text1)).setText(cal.person.getAccountName());
            ((TextView) findViewById(R.id.event_text1)).setText(events.get(1));

        }
    }

    private void updateCalendar() {
        RealmQuery<Person> query = realm.where(Person.class);

        query.isNotNull("accountName");


        RealmResults<Person> persons = query.findAllSorted("lastSignalStrength", false);
        calendarList = new ArrayList<>();

        if (persons.size() > 0) {
            Calendar cal1 = new Calendar(persons.get(0), 0, getApplicationContext());
            cal1.delegate = this;
            calendarList.add(cal1);
            cal1.loadEvents();

        }
        if (persons.size() > 1) {
            Calendar cal1 = new Calendar(persons.get(1), 1, getApplicationContext());
            cal1.delegate = this;
            calendarList.add(cal1);
            cal1.loadEvents();

        }
    }
}
