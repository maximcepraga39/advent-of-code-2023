package com.adventofcode.day3;

import com.adventofcode.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GearRatios {
    private static final String TEST_INPUT = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..""";

    private static final Map<Coordinates, List<Integer>> gearRatio = new HashMap<>();


    public static void main(String[] args) {
        var inputText = Utils.readFileFromResources("day3_gear_ratios_input.txt");
//        var inputText = TEST_INPUT;

        Map<Coordinates, String> numberCoords = parseInput(inputText, true);
        Map<Coordinates, String> symCoords = parseInput(inputText, false);

        System.out.println(checkGearRatios(numberCoords, symCoords));

        System.out.println(part2(numberCoords, symCoords));
    }

    private static Integer part2(Map<Coordinates, String> numberCoords, Map<Coordinates, String> symCoords) {
        var gearMap = symCoords.entrySet().stream()
                .filter(entry -> entry.getValue().equals("*"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        numberCoords.forEach((key, number) -> {
            var numberCoord = (NumberCoordinates) key;
            isAdjacentToSymbol1(numberCoord, number, gearMap);
        });

        return gearRatio.values().stream()
                .filter(numbers -> numbers.size() > 1)
                .map(gears -> gears.stream().reduce(1, (a, b) -> a * b))
                .reduce(0, Integer::sum);
    }

    private static void isAdjacentToSymbol1(NumberCoordinates numberCoordinates, String number, Map<Coordinates, String> symCoords) {
        AtomicBoolean isAdjacentToSymbol = new AtomicBoolean(false);
        var numberLineIndex = numberCoordinates.getLineIndex();
        var numberCharIndex = numberCoordinates.getCharIndex();
        var numberLength = numberCoordinates.getLength();

        var previousCharSymCoords = new Coordinates(numberLineIndex, numberCharIndex - 1);
        var nextCharSymCoords = new Coordinates(numberLineIndex, numberCharIndex + numberLength);
        var previousLineSymCoords = generateSymCoordsForNextLine(numberCoordinates, -1);
        var nextLineSymCoords = generateSymCoordsForNextLine(numberCoordinates, 1);

        var symCoordsToCheck = new ArrayList<Coordinates>();
        symCoordsToCheck.add(previousCharSymCoords);
        symCoordsToCheck.add(nextCharSymCoords);
        symCoordsToCheck.addAll(previousLineSymCoords);
        symCoordsToCheck.addAll(nextLineSymCoords);
        symCoords.forEach((key, val) -> {
            symCoordsToCheck
                    .forEach(e -> {
                        if (!isAdjacentToSymbol.get()) {
                            isAdjacentToSymbol.set(e.getLineIndex() == key.getLineIndex() &&
                                    e.getCharIndex() == key.getCharIndex());
                            var isAdjacent = isAdjacentToSymbol.get();

                            if (isAdjacent) {
                                var numbers = gearRatio.getOrDefault(key, new ArrayList<>());
                                numbers.add(Integer.parseInt(number));
                                gearRatio.put(key, numbers);
                            }
                        }
                    });
        });
    }

    private static Integer checkGearRatios(Map<Coordinates, String> numberCoords, Map<Coordinates, String> symCoords) {
        return numberCoords.entrySet().stream()
                .map(numberCoordMap -> {
                    var numberCoord = (NumberCoordinates) numberCoordMap.getKey();
                    if (isAdjacentToSymbol(numberCoord, symCoords)) {
                        return Integer.parseInt(numberCoordMap.getValue());
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);
    }

    private static boolean isAdjacentToSymbol(NumberCoordinates numberCoordinates, Map<Coordinates, String> symCoords) {
        AtomicBoolean isAdjacentToSymbol = new AtomicBoolean(false);
        var numberLineIndex = numberCoordinates.getLineIndex();
        var numberCharIndex = numberCoordinates.getCharIndex();
        var numberLength = numberCoordinates.getLength();

        var previousCharSymCoords = new Coordinates(numberLineIndex, numberCharIndex - 1);
        var nextCharSymCoords = new Coordinates(numberLineIndex, numberCharIndex + numberLength);
        var previousLineSymCoords = generateSymCoordsForNextLine(numberCoordinates, -1);
        var nextLineSymCoords = generateSymCoordsForNextLine(numberCoordinates, 1);

        var symCoordsToCheck = new ArrayList<Coordinates>();
        symCoordsToCheck.add(previousCharSymCoords);
        symCoordsToCheck.add(nextCharSymCoords);
        symCoordsToCheck.addAll(previousLineSymCoords);
        symCoordsToCheck.addAll(nextLineSymCoords);
        symCoords.forEach((key, val) -> {
            symCoordsToCheck
                    .forEach(e -> {
                        if (!isAdjacentToSymbol.get()) {
                            isAdjacentToSymbol.set(e.getLineIndex() == key.getLineIndex() &&
                                    e.getCharIndex() == key.getCharIndex());
                        }
                    });
        });
        return isAdjacentToSymbol.get();
    }

    private static List<Coordinates> generateSymCoordsForNextLine(NumberCoordinates numberCoordinates, int linesToAdd) {
        var symCoords = new ArrayList<Coordinates>();
        var lineIndex = numberCoordinates.getLineIndex() + linesToAdd;
        var charIndex = numberCoordinates.getCharIndex();
        for (int i = charIndex - 1; i < charIndex + numberCoordinates.getLength() + 1; i++) {
            symCoords.add(new Coordinates(lineIndex, i));
        }
        return symCoords;
    }


    private static Map<Coordinates, String> parseInput(String s, boolean parseDigits) {
        AtomicInteger lineIndex = new AtomicInteger(0);
        AtomicInteger charIndex = new AtomicInteger(0);
        return s.lines()
                .map(line -> {
                    Map<Coordinates, String> resultingMap = new HashMap<>();
                    var chars = line.toCharArray();
                    while (charIndex.get() < chars.length) {
                        var c = chars[charIndex.get()];
                        if (c == '.') {
                            charIndex.addAndGet(1);
                            continue;
                        }

                        if (parseDigits) {
                            if (Character.isDigit(c)) {
                                var coordinates = new NumberCoordinates(lineIndex.get(), charIndex.get());
                                var numLength = 1;
                                var digits = new StringBuilder();
                                digits.append(c);
                                var i = charIndex.addAndGet(1);
                                while (i < chars.length) {
                                    if (Character.isDigit(chars[i])) {
                                        numLength++;
                                        digits.append(chars[i]);
                                        i = charIndex.addAndGet(1);
                                    } else {
                                        break;
                                    }
                                }
                                charIndex.addAndGet(1);
                                coordinates.setLength(numLength);
                                resultingMap.put(coordinates, digits.toString());
                            } else {
                                charIndex.addAndGet(1);
                            }
                        } else {
                            if (!Character.isDigit(c)) {
                                var coordinates = new Coordinates(lineIndex.get(), charIndex.getAndAdd(1));
                                resultingMap.put(coordinates, String.valueOf(c));
                            } else {
                                charIndex.addAndGet(1);
                            }
                        }
                    }

                    lineIndex.addAndGet(1);
                    charIndex.set(0);
                    return resultingMap;
                })
                .reduce(new HashMap<>(), (map, pair) -> {
                    map.putAll(pair);
                    return map;
                });
    }
}

class Coordinates {
    protected int lineIndex = 0;
    protected int charIndex = 0;

    public Coordinates(int lineIndex, int charIndex) {
        this.lineIndex = lineIndex;
        this.charIndex = charIndex;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public int getCharIndex() {
        return charIndex;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "lineIndex=" + lineIndex +
                ",\tcharIndex=" + charIndex +
                '}';
    }
}

class NumberCoordinates extends Coordinates {
    private int length = 0;

    public NumberCoordinates(int lineIndex, int charIndex) {
        super(lineIndex, charIndex);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "NumberCoordinates{" +
                "lineIndex=" + lineIndex +
                ",\tcharIndex=" + charIndex +
                ",\tlength=" + length +
                '}';
    }
}