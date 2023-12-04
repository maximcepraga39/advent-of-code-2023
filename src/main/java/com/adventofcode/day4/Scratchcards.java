package com.adventofcode.day4;

import com.adventofcode.Utils;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Day 3. Started at 11:00 GMT+2 - 04/12/2023
 * */
public class Scratchcards {
    private static final String TEST_INPUT = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11""";

    public static void main(String[] args) {
        var input = Utils.readFileFromResources("day4.txt");
//        var input = TEST_INPUT;

        var cards = parseInput(input);

        var pointsTotal = cards.stream()
                .map(Card::calculatePoints)
                .reduce(0L, Long::sum);

        System.out.println(pointsTotal);
    }

    private static Set<Card> parseInput(String s) {
        return s.lines()
                .map(line -> {
                    var colonIndex = line.indexOf(':');
                    var pipeIndex = line.indexOf('|');

                    var cardId = Integer.parseInt(line.substring("Card ".length(), colonIndex).trim());
                    var winningNumbers = parseNumbers(line, colonIndex, pipeIndex, true);
                    var actualNumbers = parseNumbers(line, colonIndex, pipeIndex, false);

                    return new Card(cardId, winningNumbers, actualNumbers);
                })
                .collect(Collectors.toSet());
    }

    private static Set<Integer> parseNumbers(String line, Integer colonIndex, Integer pipeIndex, Boolean parseWinning) {
        var winningNumbersString = line.substring(colonIndex + 1, pipeIndex - 1);
        var actualNumbersString = line.substring(pipeIndex + 2);
        if (parseWinning) {
            var winningNumbersArr = winningNumbersString.split(" ");
            return Arrays.stream(winningNumbersArr)
                    .map(Ints::tryParse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } else {
            var actualNumbersArr = actualNumbersString.split(" ");
            return Arrays.stream(actualNumbersArr)
                    .map(Ints::tryParse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
    }
}

class Card {
    private final Integer cardId;
    private final Set<Integer> winningNumbers;
    private final Set<Integer> actualNumbers;


    public Card(Integer cardId, Set<Integer> winningNumbers, Set<Integer> actualNumbers) {
        this.cardId = cardId;
        this.winningNumbers = winningNumbers;
        this.actualNumbers = actualNumbers;
    }

    public Long calculatePoints() {
        var points = 0L;

        for (Integer winningNumber : winningNumbers) {
            if (actualNumbers.contains(winningNumber)) {
                if (points == 0) {
                    points++;
                } else {
                    points *= 2;
                }
            }
        }

        return points;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", winningNumbers=" + winningNumbers +
                ", actualNumbers=" + actualNumbers +
                '}';
    }
}