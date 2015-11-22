package com.devfest15.blevienna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.sdk.cloud.internal.User;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;


public class MonitoringActivity extends AppCompatActivity implements CalendarEventDelegate {
    protected static final String TAG = "MonitoringActivity";

    private Realm realm;
    private List<Calendar> calendarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(this);
    }

    @Override
    public void eventsLoaded(List<String> events) {
            //TODO: show UI
    }

    private void updateCalendar() {
        RealmQuery<Person> query = realm.where(Person.class);

        query.isNotNull("accountName");


        RealmResults<Person> persons = query.findAllSorted("lastSignalStrength", false);
        calendarList = new ArrayList<>();
        for (Person p : persons) {
            calendarList.add(new Calendar(p, getApplicationContext()));
        }
    }

}