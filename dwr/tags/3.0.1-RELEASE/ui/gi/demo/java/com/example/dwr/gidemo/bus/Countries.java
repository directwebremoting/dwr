package com.example.dwr.gidemo.bus;

import java.util.ArrayList;
import java.util.List;

/**
 * A demo business service
 */
public class Countries
{
    /**
     * Accessor for the current list of countries
     */
    public List<Country> getCountries()
    {
        return countries;
    }

    /**
     * Internal store for the current list of countries with defaults.
     */
    private List<Country> countries = new ArrayList<Country>();
    {
        countries.add(new Country("US", "United States"));
        countries.add(new Country("UK", "United Kingdom"));
        countries.add(new Country("SE", "Sweden"));
        countries.add(new Country("DE", "Germany"));
        countries.add(new Country("FR", "France"));
        countries.add(new Country("ES", "Spain"));
    }
}
