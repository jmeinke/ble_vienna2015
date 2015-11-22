package com.devfest15.blevienna;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


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
}