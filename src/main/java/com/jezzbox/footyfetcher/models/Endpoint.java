package com.jezzbox.footyfetcher.models;

import java.util.HashMap;

public record Endpoint(String get, HashMap<String, String> parameters, String[] errors, int results, Paging paging, Fixture[] response) {
}
