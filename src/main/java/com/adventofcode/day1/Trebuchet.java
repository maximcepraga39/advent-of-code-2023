package com.adventofcode.day1;

import com.adventofcode.Utils;

import java.util.regex.Pattern;

public class Trebuchet {

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
        StringBuilder digits = new StringBuilder();
        var pattern = Pattern.compile("\\d");
        var matcher = pattern.matcher(s);
        while (matcher.find()) {
            digits.append(matcher.group());
        }
        return digits.toString();
    }
}