package com.jezzbox.footyfetcher.models;

import java.util.HashMap;

public record Fixture(int id, String referee,
                      String timezone, String date,
                      int timestamp, HashMap<String, Integer> periods,
                      Venue venue, FixtureStatus status,
                      HashMap<String, Team> teams,
                      HashMap<String, Integer> goals, Score score) {
}
