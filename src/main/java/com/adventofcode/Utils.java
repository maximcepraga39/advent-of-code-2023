package com.adventofcode;

import com.google.common.io.Resources;

import java.nio.charset.StandardCharsets;

public class Utils {
    public static String readFileFromResources(String fileName) {
        try {
            var resourceUrl = Resources.getResource(fileName);
            return Resources.toString(resourceUrl, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read the file!");
        }
    }
}
