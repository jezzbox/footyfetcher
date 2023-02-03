package com.jezzbox.footyfetcher.models;

import java.util.HashMap;


public record Score(HashMap<String, Integer> fulltime,
                    HashMap<String, Integer> extratime,
                    HashMap<String, Integer> penalty,
                    HashMap<String, Integer> halftime) {
}
