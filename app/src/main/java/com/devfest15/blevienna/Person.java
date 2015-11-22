package com.devfest15.blevienna;


import com.estimote.sdk.eddystone.Eddystone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import static java.lang.System.currentTimeMillis;


/**
 * Class description
 */
public class Person extends RealmObject {
    @Required // macAddress is not nullable
    @PrimaryKey
    private String macAddress;
    private String name;
    private String accountName;
    private int lastSignalStrength;
    private Date lastSeen;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLastSignalStrength() {
        return lastSignalStrength;
    }

    public void setLastSignalStrength(int lastDistance) {
        this.lastSignalStrength = lastDistance;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    static void updatePersons(List<Eddystone> eddystones, Realm realm) {
        ArrayList<Person> update = new ArrayList<>();
        RealmResults<Person> persons = realm.where(Person.class).findAll();
        for (Person person : persons) {
            boolean contained = false;
            for (Eddystone eddystone : eddystones) {
                if (person.getMacAddress().equals(eddystone.macAddress.toString())) {
                    contained = true;
                }
            }
            if (!contained) {
                Person newPerson = new Person();
                newPerson.setMacAddress(person.getMacAddress());
                newPerson.setLastSeen(person.getLastSeen());
                newPerson.setAccountName(person.getAccountName());
                newPerson.setName(person.getName());
                newPerson.setLastSignalStrength(Integer.MIN_VALUE);

                update.add(newPerson);
            }
        }

        for (Person p : update) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(p);
            realm.commitTransaction();
        }

        for (Eddystone eddystone : eddystones) {
            // Update the person in our database


            Person person = realm.where(Person.class).equalTo("macAddress", eddystone.macAddress.toString()).findFirst();
            if (person == null) person = new Person();
            person.setMacAddress(eddystone.macAddress.toString());
            person.setLastSignalStrength(eddystone.rssi);
            person.setLastSeen(new Date(currentTimeMillis()));

            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(person);
            realm.commitTransaction();
        }
    }
}