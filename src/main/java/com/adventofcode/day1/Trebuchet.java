package com.adventofcode.day1;

import com.adventofcode.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Trebuchet {

    private static final Map<String, String> spelledDigitsToDigits = new HashMap<>();

    static {
        spelledDigitsToDigits.put("one", "1");
        spelledDigitsToDigits.put("two", "2");
        spelledDigitsToDigits.put("three", "3");
        spelledDigitsToDigits.put("four", "4");
        spelledDigitsToDigits.put("five", "5");
        spelledDigitsToDigits.put("six", "6");
        spelledDigitsToDigits.put("seven", "7");
        spelledDigitsToDigits.put("eight", "8");
        spelledDigitsToDigits.put("nine", "9");
    }

    public static void main(String[] args) {
        String fileName = "day1_trebuchet_input.txt";
        var inputText = Utils.readFileFromResources(fileName);

        var result = calibrateDocument(inputText);
        System.out.println(result);
    }

    private static int calibrateDocument(String document) {
        return document.lines()
                .map(line -> {
                    var digits = parseDigits(line);
                    digits = digits.isBlank() ? "0" : digits;
                    var firstDigitChar = digits.substring(0, 1);
                    var lastDigitChar = digits.substring(digits.length() - 1);

                    return firstDigitChar + lastDigitChar;
                })
                .map(Integer::valueOf)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static String parseDigits(String s) {
        s = convertSpelledDigitsToDigits(s);
        StringBuilder digits = new StringBuilder();
        var pattern = Pattern.compile("\\d");
        var matcher = pattern.matcher(s);
        while (matcher.find()) {
            digits.append(matcher.group());
        }
        return digits.toString();
    }

    private static String convertSpelledDigitsToDigits(String s) {
        for (String key : spelledDigitsToDigits.keySet()) {
            var pattern = Pattern.compile(key);
            var matcher = pattern.matcher(s);
            while (matcher.find()) {
                var group = matcher.group();
                var i = s.indexOf(group);
                s = s.substring(0, ++i) + spelledDigitsToDigits.get(key) + s.substring(i);
            }
        }
        return s;
    }
}