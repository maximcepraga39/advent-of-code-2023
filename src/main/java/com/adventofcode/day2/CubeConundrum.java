package com.adventofcode.day2;

import com.adventofcode.Utils;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubeConundrum {
    @SuppressWarnings("unused")
    private static final String TEST_INPUT = """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """;

    private static final String RED_CUBE_NAME = "red";
    private static final String GREEN_CUBE_NAME = "green";
    private static final String BLUE_CUBE_NAME = "blue";
    private static final Integer RED_CUBE_COUNT = 12;
    private static final Integer GREEN_CUBE_COUNT = 13;
    private static final Integer BLUE_CUBE_COUNT = 14;

    public static void main(String[] args) {
        var input = Utils.readFileFromResources("day2_cube_conundrum_input.txt");
        var games = parseInput(input);

        var gameResult = checkPossibleGames(games);
        System.out.println(gameResult);
    }

    private static int checkPossibleGames(Map<String, List<Map<String, Integer>>> games) {
        return games.entrySet().stream()
                .filter(game -> {
                    var gameSubsets = game.getValue();
                    return checkGameForConditionMatching(gameSubsets);
                })
                .map(possibleGame -> {
                    var key = possibleGame.getKey();
                    return Integer.valueOf(key.substring("Game ".length()));
                })
                .reduce(0, Integer::sum);
    }

    private static boolean checkGameForConditionMatching(List<Map<String, Integer>> gameSubsets) {
        return gameSubsets.stream()
                .map(gameSubset -> {
                    var redCubeConditionMet = gameSubset.getOrDefault(RED_CUBE_NAME, 0).compareTo(RED_CUBE_COUNT) <= 0;
                    var greenCubeConditionMet = gameSubset.getOrDefault(GREEN_CUBE_NAME, 0).compareTo(GREEN_CUBE_COUNT) <= 0;
                    var blueCubeConditionMet = gameSubset.getOrDefault(BLUE_CUBE_NAME, 0).compareTo(BLUE_CUBE_COUNT) <= 0;

                    return redCubeConditionMet && greenCubeConditionMet && blueCubeConditionMet;
                })
                .reduce(true, (a, b) -> a && b);
    }

    private static Map<String, List<Map<String, Integer>>> parseInput(String input) {
        return input.lines()
                .map(line -> {
                    var colonIndex = line.indexOf(':');
                    checkInput(colonIndex);
                    var gameId = line.substring(0, colonIndex);
                    var listOfRevealedCubes = Arrays.asList(line.substring(colonIndex + 1).split(";"));
                    var subsetOfCubesList = listOfRevealedCubes.stream()
                            .map(CubeConundrum::parseSubsetOfCubes)
                            .toList();

                    return Map.of(gameId, subsetOfCubesList);
                })
                .reduce(new HashMap<>(), (map, pair) -> {
                    map.putAll(pair);
                    return map;
                });
    }

    private static Map<String, Integer> parseSubsetOfCubes(String subset) {
        return Arrays.stream(subset.split(","))
                .map(pair -> {
                    pair = pair.trim();
                    var spaceIndex = pair.indexOf(' ');
                    var cubeCount = pair.substring(0, spaceIndex);
                    var cubeColor = pair.substring(spaceIndex + 1);

                    return Map.of(cubeColor, Integer.valueOf(cubeCount));
                })
                .reduce(new HashMap<>(), (map, pair) -> {
                    map.putAll(pair);
                    return map;
                });
    }

    private static void checkInput(Integer colonIndex) {
        if (colonIndex < 0) {
            throw new InvalidParameterException("Input is not valid, `:` not found!");
        }
    }
}
