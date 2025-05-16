package org.example.cook1;

import java.util.ArrayList;
import java.util.List;

public class customer {
    private String name;
    private String preferences;
    private String allergies;

    public customer(String name) {
        this.name = name;
    }

    public void setPreference(String pref) {
        if (pref == null || pref.isEmpty()) {
            throw new IllegalArgumentException("Preference cannot be empty");
        }
        preferences = pref;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }


    public String getAllergies() {
        return allergies;
    }

    public String getPreference() {
        return preferences;
    }



}
