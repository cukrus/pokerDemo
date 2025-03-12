package net.cukrus.pokerDemo.sudoku;

import net.cukrus.pokerDemo.sudoku.cloned.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SudokuDablesTest {
    private static final String FOLDER_SUDOKU = "C:\\projects\\flutter\\sudoku\\";
    private static final String SOLVED_FILENAME = "solved_grids.csv";
    private static final int CORRECT_ARR_LENGTH = 9;
    private static final List<Integer> COL_ONE_TO_THREE = List.of(1, 4, 7);
    private static final List<Integer> COL_FOUR_TO_SIX = List.of(2, 5, 8);
    private static final int[] ONE_TO_NINE = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final List<Integer> ONE_TO_NINE_LIST = IntStream.range(1, 10).boxed().collect(Collectors.toList());
    private static final List<Integer> ZERO_TO_80_LIST = IntStream.range(0, 81).boxed().collect(Collectors.toList());
    private static final int[][] BAD_BOARD = new int[][]{ONE_TO_NINE, ONE_TO_NINE, ONE_TO_NINE, ONE_TO_NINE,
            ONE_TO_NINE, ONE_TO_NINE, ONE_TO_NINE, ONE_TO_NINE, ONE_TO_NINE};

    private final SudokuDables underTest = new SudokuDables();

    @Test
    void generateBoard_shouldFail() {
//        int[][] result = underTest.generateBoard();
        int[][] result = BAD_BOARD;

        assertThat(result).isNotEmpty();
        boolean foundDuplicate = false;
        for (int i = 0; i < CORRECT_ARR_LENGTH; i++) {
            int[] row = result[i];
            assertEquals(row.length, CORRECT_ARR_LENGTH);
            List list = Arrays.stream(row).boxed().collect(Collectors.toList());
            Set set = new HashSet<>(list);
            if (row.length != set.size()) {
                foundDuplicate = true;
                break;
            }
        }
        if (!foundDuplicate) {
            int[][] columns = constructColumnArrayFromArray(result);
            assertThat(columns).isNotEmpty();
            for (int i = 0; i < CORRECT_ARR_LENGTH; i++) {
                int[] row = columns[i];
                assertEquals(row.length, CORRECT_ARR_LENGTH);
                List list = Arrays.stream(row).boxed().collect(Collectors.toList());
                Set set = new HashSet<>(list);
                if (row.length != set.size()) {
                    foundDuplicate = true;
                    break;
                }
            }
        }
        assertTrue(foundDuplicate);
    }

//    @Test
    void generateBoard_shouldPass() {
//        int[][] result = underTest.generateBoard();
//        int[][] result = generateBoardLocal();
        int[][] result = generateBoardPython();
//        for(int[] row : result) {
//            System.out.println("generated array: " + row);
//        }
        System.out.println("generated array: " + Arrays.deepToString(result));
        assertThat(isGridFilled(result)).isTrue();

        for (int i = 0; i < result.length; i++) {
            assertThat(result[i]).contains(ONE_TO_NINE);
        }
        int[][] columns = constructColumnArrayFromArray(result);
        for (int i = 0; i < columns.length; i++) {
            assertThat(columns[i]).contains(ONE_TO_NINE);
        }
        String line = gridToNrLineString(result);
        System.out.println("gridToNrLineString: " + line);
    }

//    @Test
    void generateBoard_shouldPass_cloned() {
        byte[] oneToNine = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[][] result = Creator.createFull().getArray();
        System.out.println("generated array: " + Arrays.deepToString(result));
        assertThat(isGridFilled_byte(result)).isTrue();

        for (int i = 0; i < result.length; i++) {
            assertThat(result[i]).contains(oneToNine);
        }
        byte[][] columns = constructColumnArrayFromArray_bytes(result);
        for (int i = 0; i < columns.length; i++) {
            assertThat(columns[i]).contains(oneToNine);
        }
        String line = gridToNrLineString_bytes(result);
        System.out.println("gridToNrLineString: " + line);
    }

//    @Test
    void generateBoard_shouldPass_backtrack() {
        int[][] result = generateBoardBacktrack();
        System.out.println("generated array: " + Arrays.deepToString(result));
        assertThat(isGridFilled(result)).isTrue();

        for (int i = 0; i < result.length; i++) {
            assertThat(result[i]).contains(ONE_TO_NINE);
        }
        int[][] columns = constructColumnArrayFromArray(result);
        for (int i = 0; i < columns.length; i++) {
            assertThat(columns[i]).contains(ONE_TO_NINE);
        }
        String line = gridToNrLineString(result);
        System.out.println("gridToNrLineString: " + line);
    }

    @Test
    void testSolver() {
        int[][] grid = {{8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}};
        int[][] expected = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        int[][] result = duplicate9x9Grid(grid);
        AtomicInteger count = new AtomicInteger();
        solveGrid(result, count);

        System.out.println("grid: " + Arrays.deepToString(grid));
        System.out.println("expected: " + Arrays.deepToString(expected));
        System.out.println("result: " + Arrays.deepToString(result));
        System.out.println("count: " + count.get());

        assertThat(expected).isDeepEqualTo(result);
    }

    @Test
    void testCounter() {
        int[][] grid = {{8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}};
        int[][] solved = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        int[][] result = duplicate9x9Grid(grid);
        AtomicInteger count = new AtomicInteger();
        countSolutions(result, count);

        System.out.println("grid: " + Arrays.deepToString(grid));
        System.out.println("solved: " + Arrays.deepToString(solved));
        System.out.println("result: " + Arrays.deepToString(result));
        System.out.println("count: " + count.get());

        assertThat(solved).isDeepEqualTo(result);
    }

    @Test
    void testMyCounterVsCloned() {
        int[][] puzzle = {{8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}};
        int[][] expectedSolved = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        int[][] result = duplicate9x9Grid(puzzle);
        AtomicInteger count = new AtomicInteger();
        countSolutions(result, count);

        byte[][] byteResult = intToByteGrid(puzzle);
        GameMatrix riddleMatrix = new GameMatrixImpl();
        riddleMatrix.setAll(byteResult);
        Solver s = new Solver(riddleMatrix);
        s.setLimit(Integer.MAX_VALUE);
        List<GameMatrix> results = s.solve();

        System.out.println("puzzle: " + Arrays.deepToString(puzzle));
        System.out.println("expectedSolved: " + Arrays.deepToString(expectedSolved));
        System.out.println("result: " + Arrays.deepToString(result));
        System.out.println("count: " + count.get());
        System.out.println("count of cloned: " + results.size());

        assertThat(result).isDeepEqualTo(expectedSolved);
        assertThat(count.get()).isEqualTo(1).isEqualTo(results.size());
    }

//    @Test
    void testMyCounterVsClonedWithRandomPuzzle() {
        int[][] puzzle = generateRandomPuzzleTillXLeft(35);

        int[][] result = duplicate9x9Grid(puzzle);
        AtomicInteger count = new AtomicInteger();
        countSolutions(result, count);

        byte[][] bytePuzzle = intToByteGrid(puzzle);
        GameMatrix riddleMatrix = new GameMatrixImpl();
        riddleMatrix.setAll(bytePuzzle);
        Solver s = new Solver(riddleMatrix);
        s.setLimit(Integer.MAX_VALUE);
        List<GameMatrix> results = s.solve();

        System.out.println("puzzle: " + Arrays.deepToString(puzzle));
        System.out.println("expectedSolvedCount: " + results.size());
        System.out.println("resultCount: " + count.get());

//        int i = 1;
//        for (GameMatrix res : results) {
//            System.out.println("solution" + i + ": " + Arrays.deepToString(res.getArray()));
//            i++;
//        }

        assertThat(count.get()).isEqualTo(results.size());
    }

    @Test
    void testMyCounterVsClonedWithRandomPuzzle_iterative() {
        IntStream.range(0, 5000).forEach(i -> testMyCounterVsClonedWithRandomPuzzle());
    }

//    @Test
    void testHider_old(int expectedVisibleCount) {
//        int expectedVisibleCount = 25;
        int[][] solved = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        assertGridFilledCorrect(solved);

        int[][] result = duplicate9x9Grid(solved);
//        List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
//        Collections.shuffle(shuffled81);
        //TODO: visible count should be ~29(25 - 33) depending on difficulty
        long hideStart = System.currentTimeMillis();
        hideGridTillXLeft_old(result, expectedVisibleCount);
        long hideEnd = System.currentTimeMillis();
        int visible = countNonZerosInGrid(result);
        int[][] duplicate4Count = duplicate9x9Grid(result);
        AtomicInteger count = new AtomicInteger();
        countSolutions(duplicate4Count, count);

        System.out.println("solved: " + Arrays.deepToString(solved));
        System.out.println("result: " + Arrays.deepToString(result));
        System.out.println("expectedVisibleCount: " + expectedVisibleCount);
        System.out.println("visible: " + visible);
        System.out.println("solution count: " + count.get());
        System.out.println("hiding took: " + ((hideEnd - hideStart) * 0.001) + "s");

        assertThat(visible).isEqualTo(expectedVisibleCount);
        assertThat(count.get()).isEqualTo(1);

        int[][] solution = duplicate9x9Grid(result);
        solveGrid(solution, new AtomicInteger());
        assertGridFilledCorrect(solution);
        assertThat(solved).isDeepEqualTo(solution);
    }

//    @Test
    void testHider_new(int expectedVisibleCount) {
//        int expectedVisibleCount = 25;
        int[][] solved = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        assertGridFilledCorrect(solved);

        int[][] result = duplicate9x9Grid(solved);
        long hideStart = System.currentTimeMillis();
        List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
        Collections.shuffle(shuffled81);
        //TODO: visible count should be ~29(25 - 33) depending on difficulty
        hideGridTillXLeft_new(result, shuffled81, new AtomicInteger(81), expectedVisibleCount, 0);
        long hideEnd = System.currentTimeMillis();
        int visible = countNonZerosInGrid(result);
        int[][] duplicate4Count = duplicate9x9Grid(result);
        AtomicInteger count = new AtomicInteger();
        countSolutions(duplicate4Count, count);

        System.out.println("solved: " + Arrays.deepToString(solved));
        System.out.println("result: " + Arrays.deepToString(result));
        System.out.println("expectedVisibleCount: " + expectedVisibleCount);
        System.out.println("visible: " + visible);
        System.out.println("solution count: " + count.get());
        System.out.println("hiding took: " + ((hideEnd - hideStart) * 0.001) + "s");

        assertThat(visible).isEqualTo(expectedVisibleCount);
        assertThat(count.get()).isEqualTo(1);

        int[][] solution = duplicate9x9Grid(result);
        solveGrid(solution, new AtomicInteger());
        assertGridFilledCorrect(solution);
        assertThat(solved).isDeepEqualTo(solution);
    }

    @Test
    void testHider_newTest() {
        int expectedVisibleCount = 17;
        int[][] solved = {
                {8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        assertGridFilledCorrect(solved);

        int[][] result = duplicate9x9Grid(solved);
        long hideStart = System.currentTimeMillis();
        List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
        Collections.shuffle(shuffled81);
        //TODO: visible count should be ~29(25 - 33) depending on difficulty
        hideGridTillXLeft_new(result, shuffled81, new AtomicInteger(81), expectedVisibleCount, 0);
        long hideEnd = System.currentTimeMillis();
        int visible = countNonZerosInGrid(result);
        int[][] duplicate4Count = duplicate9x9Grid(result);
        AtomicInteger count = new AtomicInteger();
        countSolutions(duplicate4Count, count);

        System.out.println("solved: " + Arrays.deepToString(solved));
        System.out.println("result: " + Arrays.deepToString(result));
        System.out.println("expectedVisibleCount: " + expectedVisibleCount);
        System.out.println("visible: " + visible);
        System.out.println("solution count: " + count.get());
        System.out.println("hiding took: " + ((hideEnd - hideStart) * 0.001) + "s");

        assertThat(visible).isEqualTo(expectedVisibleCount);
        assertThat(count.get()).isEqualTo(1);

        int[][] solution = duplicate9x9Grid(result);
        solveGrid(solution, new AtomicInteger());
        assertGridFilledCorrect(solution);
        assertThat(solved).isDeepEqualTo(solution);
    }

//    @Test
    void testHider_new2(int expectedVisibleCount) {
//        int expectedVisibleCount = 25;
        int[][] solved = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        assertGridFilledCorrect(solved);

        int[][] result = duplicate9x9Grid(solved);
        long hideStart = System.currentTimeMillis();
        List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
        Collections.shuffle(shuffled81);
        //TODO: visible count should be ~29(25 - 33) depending on difficulty
        hideGridTillXLeft_new2(result, shuffled81, new AtomicInteger(81), expectedVisibleCount, 0);
        long hideEnd = System.currentTimeMillis();
        int visible = countNonZerosInGrid(result);
        int[][] duplicate4Count = duplicate9x9Grid(result);
        AtomicInteger count = new AtomicInteger();
        countSolutions(duplicate4Count, count);

        System.out.println("solved: " + Arrays.deepToString(solved));
        System.out.println("result: " + Arrays.deepToString(result));
        System.out.println("expectedVisibleCount: " + expectedVisibleCount);
        System.out.println("visible: " + visible);
        System.out.println("solution count: " + count.get());
        System.out.println("hiding took: " + ((hideEnd - hideStart) * 0.001) + "s");

        assertThat(visible).isEqualTo(expectedVisibleCount);
        assertThat(count.get()).isEqualTo(1);

        int[][] solution = duplicate9x9Grid(result);
        solveGrid(solution, new AtomicInteger());
        assertGridFilledCorrect(solution);
        assertThat(solved).isDeepEqualTo(solution);
    }

    @Test
    void testHider_old_iterative() {
        int expectedVisibleCount = 27;
        IntStream.range(0, 500).forEach(i -> testHider_old(expectedVisibleCount));
    }

    @Test
    void testHider_new_iterative() {//12min
        int expectedVisibleCount = 27;
        IntStream.range(0, 500).forEach(i -> testHider_new(expectedVisibleCount));
    }

    @Test
    void testHider_new2_iterative() {
        int expectedVisibleCount = 27;
        IntStream.range(0, 500).forEach(i -> testHider_new2(expectedVisibleCount));
    }

    @Test
    void compareHiders() {
        //iterations 5k all5 - takes 4h30m
        int iterations = 250;
        int expectedVisibleCount = 27;

        List<int[][]> solved1 = IntStream.range(0, iterations).mapToObj(i -> new int[][]{
                        {8, 1, 2, 7, 5, 3, 6, 4, 9},
                        {9, 4, 3, 6, 8, 2, 1, 7, 5},
                        {6, 7, 5, 4, 9, 1, 2, 8, 3},
                        {1, 5, 4, 2, 3, 7, 8, 9, 6},
                        {3, 6, 9, 8, 4, 5, 7, 2, 1},
                        {2, 8, 7, 1, 6, 9, 5, 3, 4},
                        {5, 2, 1, 9, 7, 4, 3, 6, 8},
                        {4, 3, 8, 5, 2, 6, 9, 1, 7},
                        {7, 9, 6, 3, 1, 8, 4, 5, 2}})
                .collect(Collectors.toList());
        List<int[][]> solved2 = IntStream.range(0, iterations).mapToObj(i -> new int[][]{
                        {8, 1, 2, 7, 5, 3, 6, 4, 9},
                        {9, 4, 3, 6, 8, 2, 1, 7, 5},
                        {6, 7, 5, 4, 9, 1, 2, 8, 3},
                        {1, 5, 4, 2, 3, 7, 8, 9, 6},
                        {3, 6, 9, 8, 4, 5, 7, 2, 1},
                        {2, 8, 7, 1, 6, 9, 5, 3, 4},
                        {5, 2, 1, 9, 7, 4, 3, 6, 8},
                        {4, 3, 8, 5, 2, 6, 9, 1, 7},
                        {7, 9, 6, 3, 1, 8, 4, 5, 2}})
                .collect(Collectors.toList());
        List<int[][]> solved3 = IntStream.range(0, iterations).mapToObj(i -> new int[][]{
                        {8, 1, 2, 7, 5, 3, 6, 4, 9},
                        {9, 4, 3, 6, 8, 2, 1, 7, 5},
                        {6, 7, 5, 4, 9, 1, 2, 8, 3},
                        {1, 5, 4, 2, 3, 7, 8, 9, 6},
                        {3, 6, 9, 8, 4, 5, 7, 2, 1},
                        {2, 8, 7, 1, 6, 9, 5, 3, 4},
                        {5, 2, 1, 9, 7, 4, 3, 6, 8},
                        {4, 3, 8, 5, 2, 6, 9, 1, 7},
                        {7, 9, 6, 3, 1, 8, 4, 5, 2}})
                .collect(Collectors.toList());
        List<int[][]> solved4 = IntStream.range(0, iterations).mapToObj(i -> new int[][]{
                        {8, 1, 2, 7, 5, 3, 6, 4, 9},
                        {9, 4, 3, 6, 8, 2, 1, 7, 5},
                        {6, 7, 5, 4, 9, 1, 2, 8, 3},
                        {1, 5, 4, 2, 3, 7, 8, 9, 6},
                        {3, 6, 9, 8, 4, 5, 7, 2, 1},
                        {2, 8, 7, 1, 6, 9, 5, 3, 4},
                        {5, 2, 1, 9, 7, 4, 3, 6, 8},
                        {4, 3, 8, 5, 2, 6, 9, 1, 7},
                        {7, 9, 6, 3, 1, 8, 4, 5, 2}})
                .collect(Collectors.toList());
        List<int[][]> solved5 = IntStream.range(0, iterations).mapToObj(i -> new int[][]{
                        {8, 1, 2, 7, 5, 3, 6, 4, 9},
                        {9, 4, 3, 6, 8, 2, 1, 7, 5},
                        {6, 7, 5, 4, 9, 1, 2, 8, 3},
                        {1, 5, 4, 2, 3, 7, 8, 9, 6},
                        {3, 6, 9, 8, 4, 5, 7, 2, 1},
                        {2, 8, 7, 1, 6, 9, 5, 3, 4},
                        {5, 2, 1, 9, 7, 4, 3, 6, 8},
                        {4, 3, 8, 5, 2, 6, 9, 1, 7},
                        {7, 9, 6, 3, 1, 8, 4, 5, 2}})
                .collect(Collectors.toList());

        // iter=5k: 326ms; oldAvg: 330.4318; tookMin: Optional[4]; tookMax:Optional[15010]
        List<Long> oldTimes = new ArrayList<>(iterations);
        solved1.forEach(solved -> {
            long start = System.currentTimeMillis();
            hideGridTillXLeft_old(solved, expectedVisibleCount);
            long end = System.currentTimeMillis();
            oldTimes.add(end - start);
        });

        // iter=5k: 328ms; oldAvg_2: 340.7666; tookMin: Optional[3]; tookMax:Optional[24642]
        List<Long> oldTimes_2 = new ArrayList<>(iterations);
        solved4.forEach(solved -> {
            long start = System.currentTimeMillis();
            hideGridTillXLeft_old_2(solved, expectedVisibleCount);
            long end = System.currentTimeMillis();
            oldTimes_2.add(end - start);
        });

        // iter=5k: 1864ms; newAvg: 1420.8904; tookMin: Optional[3]; tookMax:Optional[147373]
        List<Long> newTimes = new ArrayList<>(iterations);
        solved2.forEach(solved -> {
            long start = System.currentTimeMillis();
            List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
            Collections.shuffle(shuffled81);
            hideGridTillXLeft_new(solved, shuffled81, new AtomicInteger(81), expectedVisibleCount, 0);
            long end = System.currentTimeMillis();
            newTimes.add(end - start);
        });

        // iter=5k: 353ms; new2Avg: 352.4778; tookMin: Optional[5]; tookMax:Optional[23506]
        List<Long> new2Times = new ArrayList<>(iterations);
        solved3.forEach(solved -> {
            long start = System.currentTimeMillis();
            List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
            Collections.shuffle(shuffled81);
            hideGridTillXLeft_new2(solved, shuffled81, new AtomicInteger(81), expectedVisibleCount, 0);
            long end = System.currentTimeMillis();
            new2Times.add(end - start);
        });

        // iter=5k: 341ms; new2Avg_2: 336.856; tookMin: Optional[3]; tookMax:Optional[17094]
        List<Long> new2Times_2 = new ArrayList<>(iterations);
        solved5.forEach(solved -> {
            long start = System.currentTimeMillis();
            List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
            Collections.shuffle(shuffled81);
            hideGridTillXLeft_new2(solved, shuffled81, new AtomicInteger(81), expectedVisibleCount, 0);
            long end = System.currentTimeMillis();
            new2Times_2.add(end - start);
        });

        double oldAvg = oldTimes.stream().mapToDouble(a -> a).average().orElse(0);
        double oldAvg_2 = oldTimes_2.stream().mapToDouble(a -> a).average().orElse(0);
        double newAvg = newTimes.stream().mapToDouble(a -> a).average().orElse(0);
        double new2Avg = new2Times.stream().mapToDouble(a -> a).average().orElse(0);
        double new2Avg_2 = new2Times_2.stream().mapToDouble(a -> a).average().orElse(0);

        assertThat(oldAvg).isNotNull().isGreaterThan(0);
        assertThat(oldAvg_2).isNotNull().isGreaterThan(0);
        assertThat(newAvg).isNotNull().isGreaterThan(0);
        assertThat(new2Avg).isNotNull().isGreaterThan(0);
        assertThat(new2Avg_2).isNotNull().isGreaterThan(0);
        System.out.println("oldAvg: " + oldAvg + "; tookMin: " + oldTimes.stream().min(Comparator.naturalOrder()) + "; tookMax:" + oldTimes.stream().max(Comparator.naturalOrder()));
        System.out.println("oldAvg_2: " + oldAvg_2 + "; tookMin: " + oldTimes_2.stream().min(Comparator.naturalOrder()) + "; tookMax:" + oldTimes_2.stream().max(Comparator.naturalOrder()));
        System.out.println("newAvg: " + newAvg + "; tookMin: " + newTimes.stream().min(Comparator.naturalOrder()) + "; tookMax:" + newTimes.stream().max(Comparator.naturalOrder()));
        System.out.println("new2Avg: " + new2Avg + "; tookMin: " + new2Times.stream().min(Comparator.naturalOrder()) + "; tookMax:" + new2Times.stream().max(Comparator.naturalOrder()));
        System.out.println("new2Avg_2: " + new2Avg_2 + "; tookMin: " + new2Times_2.stream().min(Comparator.naturalOrder()) + "; tookMax:" + new2Times_2.stream().max(Comparator.naturalOrder()));
    }

    private void assertGridFilledCorrect(int[][] grid) {
        assertThat(isGridFilled(grid)).isTrue();
        for (int row = 0; row < grid.length; row++) {
            assertThat(grid[row]).contains(ONE_TO_NINE);
        }
        int[][] columns = constructColumnArrayFromArray(grid);
        for (int col = 0; col < columns.length; col++) {
            assertThat(columns[col]).contains(ONE_TO_NINE);
        }
        for (int squareNr = 1; squareNr < 10; squareNr++) {
            List<Integer> square = get3by3SquareAsList(grid, squareNr);
            assertThat(square).containsAll(ONE_TO_NINE_LIST);
        }
    }

    @Test
    void testClonedSolver() {
        int[][] solved = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        assertThat(isGridFilled(solved)).isTrue();
        for (int i = 0; i < solved.length; i++) {
            assertThat(solved[i]).contains(ONE_TO_NINE);
        }
        int[][] cols = constructColumnArrayFromArray(solved);
        for (int i = 0; i < cols.length; i++) {
            assertThat(cols[i]).contains(ONE_TO_NINE);
        }
        for (int squareNr = 1; squareNr < 10; squareNr++) {
//            int[][] square = get3by3Square(solved, squareNr);
            List<Integer> square = get3by3SquareAsList(solved, squareNr);
            assertThat(square).containsAll(ONE_TO_NINE_LIST);
            //if value not already used on this 3x3 square
//            if (!arrayHasVal(square[0], val) &&!arrayHasVal(square[1], val) &&!arrayHasVal(square[2], val)) {
//            }
        }

        int[][] result = duplicate9x9Grid(solved);
        hideGridTillXLeft(result, 17);
        int visible = countNonZerosInGrid(result);
//        int[][] duplicate = duplicate9x9Grid(result);
//        AtomicInteger count = new AtomicInteger();
//        countSolutions(duplicate, count);
        byte[][] byteResult = intToByteGrid(result);
        GameMatrix riddleMatrix = new GameMatrixImpl();
        riddleMatrix.setAll(byteResult);
        Solver s = new Solver(riddleMatrix);
        s.setLimit(Integer.MAX_VALUE);
        List<GameMatrix> results = s.solve();
        int count = results.size();

        System.out.println("solved: " + Arrays.deepToString(solved));
        System.out.println("puzzle: " + Arrays.deepToString(result));
//        System.out.println("duplicate: " + Arrays.deepToString(duplicate));
        System.out.println("visible: " + visible);
        System.out.println("count: " + count);

        assertThat(visible).isEqualTo(17);

        int i = 1;
        for (GameMatrix res : results) {
            System.out.println("solution" + i + ": " + Arrays.deepToString(res.getArray()));
            i++;
        }

//        assertThat(count.get()).isEqualTo(1);

//        assertThat(isGridFilled(duplicate)).isTrue();
//        for (int i = 0; i < duplicate.length; i++) {
//            assertThat(duplicate[i]).contains(ONE_TO_NINE);
//        }
//        int[][] columns = constructColumnArrayFromArray(duplicate);
//        for (int i = 0; i < columns.length; i++) {
//            assertThat(columns[i]).contains(ONE_TO_NINE);
//        }

//        int[][] solution = duplicate9x9Grid(result);
//        solveGrid(solution, new AtomicInteger());
//        System.out.println("solution: " + Arrays.deepToString(solution));
    }

//    @Test
    void testClonedRiddler() {
        byte[][] solved = {{8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}};
        byte[][] riddleArr = {{8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}};

        GameMatrix riddleMatrix = new GameMatrixImpl();
        riddleMatrix.setAll(riddleArr);
        Solver s = new Solver(riddleMatrix);
        s.setLimit(2000);
        List<GameMatrix> results = s.solve();
        int count = results.size();

        System.out.println("solved: " + Arrays.deepToString(solved));
        System.out.println("riddle: " + Arrays.deepToString(riddleArr));
        System.out.println("count: " + count);
        System.out.println("result: " + Arrays.deepToString(results.get(0).getArray()));

        assertThat(count).isEqualTo(1);
        assertThat(Arrays.deepEquals(results.get(0).getArray(), solved)).isTrue();
    }

    @Test
    void testClonedRiddler_iterative() {
        IntStream.range(0, 50000).forEach(i -> testClonedRiddler());
    }

    private byte[][] intToByteGrid(int[][] grid) {
        byte[][] result = new byte[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                result[r][c] = (byte) grid[r][c];
            }
        }
        return result;
    }

    private int[][] byteToIntGrid(byte[][] grid) {
        int[][] result = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                result[r][c] = grid[r][c];
            }
        }
        return result;
    }

    private int[][] duplicate9x9Grid(int[][] grid) {
        int[][] result = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                result[r][c] = grid[r][c];
            }
        }
        return result;
    }

    private void copy9x9Grid(int[][] gridToCopy, int[][] destinationGrid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                destinationGrid[r][c] = gridToCopy[r][c];
            }
        }
    }

    private byte[][] duplicate9x9Grid(byte[][] grid) {
        byte[][] result = new byte[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                result[r][c] = grid[r][c];
            }
        }
        return result;
    }

    private int countNonZerosInGrid(int[][] grid) {
        int result = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] != 0) {
                    result++;
                }
            }
        }
        return result;
    }

    private int countNonZerosInGrid(byte[][] grid) {
        int result = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] != 0) {
                    result++;
                }
            }
        }
        return result;
    }

    // * # #
    // # * #
    // # # *
    private void fillGridDiagonally(int[][] grid) {
        for (int i = 0; i < 3; i++) {
            List<Integer> shuffled = Arrays.stream(ONE_TO_NINE).boxed().collect(Collectors.toList());
            Collections.shuffle(shuffled);
            fillBlock(grid, shuffled, i * 3, i * 3);
        }
    }

    private void fillBlock(int[][] grid, List<Integer> numbers, final int row, final int column) {
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[row + j][column + i] = numbers.get(k++);
            }
        }
    }

    @Test
    void generateBoard_shouldPass_iterative() {
        IntStream.range(0, 5000).forEach(i -> generateBoard_shouldPass());
    }

    @Test
    void generateBoard_shouldPass_iterative_cloned() {
        IntStream.range(0, 5000).forEach(i -> generateBoard_shouldPass_cloned());
    }

    @Test
    void generateBoard_shouldPass_iterative_backtrack() {
        IntStream.range(0, 5000).forEach(i -> generateBoard_shouldPass_backtrack());
    }

    @Test
    void compareAlgorithms() {
        int iterations = 50000;

        //slowest (but all very similar)
        List<Long> pythonTimes = new ArrayList<>(iterations);
        IntStream.range(0, iterations).forEach(i -> {
            long start = System.currentTimeMillis();
            int[][] forcedBoard = generateBoardPython();
            long end = System.currentTimeMillis();
            pythonTimes.add(end - start);
        });

        //2nd place
        List<Long> clonedTimes = new ArrayList<>(iterations);
        IntStream.range(0, iterations).forEach(i -> {
            long start = System.currentTimeMillis();
            GameMatrix matrix = Creator.createFull();
            long end = System.currentTimeMillis();
            clonedTimes.add(end - start);
        });

        //fastest
        List<Long> backtrackTimes = new ArrayList<>(iterations);
        IntStream.range(0, iterations).forEach(i -> {
            long start = System.currentTimeMillis();
            int[][] backtrackBoard = generateBoardBacktrack();
            long end = System.currentTimeMillis();
            backtrackTimes.add(end - start);
        });

        double pythonAvg = pythonTimes.stream().mapToDouble(a -> a).average().orElse(0);
        double clonedAvg = clonedTimes.stream().mapToDouble(a -> a).average().orElse(0);
        double backtrackAvg = backtrackTimes.stream().mapToDouble(a -> a).average().orElse(0);

        assertThat(pythonAvg).isNotNull().isGreaterThan(0);
        assertThat(clonedAvg).isNotNull().isGreaterThan(0);
        assertThat(backtrackAvg).isNotNull().isGreaterThan(0);
        System.out.println("pythonAvg: " + pythonAvg);
        System.out.println("clonedAvg: " + clonedAvg);
        System.out.println("backtrackAvg: " + backtrackAvg);
    }

    @Test
    void generateSolvedBoards_2AndFromFile() {
        String fileName = FOLDER_SUDOKU + SOLVED_FILENAME;
        Set<String> grids;
        if (new File(fileName).exists()) {
            grids = readGridsFromFile(fileName);
        } else {
            grids = new HashSet<>();
        }
        System.out.println("starting with grids size: " + grids.size());

        for (int index = 0; index < 1000000; index++) {
            int[][] gridArray = generateBoardPython();
            String grid = gridToNrLineString(gridArray);
            System.out.println("created grid index: " + index);
            grids.add(grid);
        }

        System.out.println("ending with grids size: " + grids.size());
        writeGridsToFile(fileName, grids);
    }

    @Test
    void multiThread_generateSolvedBoards_2AndFromFile() throws InterruptedException {
        System.out.println(LocalDateTime.now() + " starting parsing grids");
        final String fileName = FOLDER_SUDOKU + SOLVED_FILENAME;
//        final ConcurrentHashMap<String, Boolean> grids = new ConcurrentHashMap<>();
        final ConcurrentSkipListSet<String> grids = new ConcurrentSkipListSet<>();
//        Set<String> grids;
        if (new File(fileName).exists()) {
//            readGridsFromFile(fileName).parallelStream().forEach(grid -> grids.put(grid, true));
            readGridsFromFile(fileName).parallelStream().forEach(grid -> grids.add(grid));
        }
        System.out.println(LocalDateTime.now() + " starting with grids size: " + grids.size());
        //TODO implement
        int threadCount = 10;
        System.out.println(LocalDateTime.now() + " executing using " + threadCount + " threads");
        ExecutorService es = Executors.newFixedThreadPool(threadCount);
        IntStream.range(0, threadCount).forEach(val -> es.submit(() -> {
            System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + ": started execution");
            for (int index = 0; index < 500000; index++) {
                int[][] gridArray = generateBoardPython();
                String grid = gridToNrLineString(gridArray);
//                System.out.println(Thread.currentThread().getName() + ": created grid index " + index);
//                grids.put(grid, true);
                grids.add(grid);
                if (index % 10000 == 0) {
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + ": current index " + index);
                }
            }
            System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + ": completed execution");
        }));
        es.shutdown();
        es.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        System.out.println(LocalDateTime.now() + " ending generation with grids size: " + grids.size());
        System.out.println(LocalDateTime.now() + " starting writing to file");
//        writeGridsToFile(fileName, grids.keys());
        writeGridsToFile(fileName, grids);
        System.out.println(LocalDateTime.now() + " ended writing to file");
    }

    private void writeGridsToFile(String fileName, Enumeration<String> linesToWrite) {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(fileName), CSVFormat.DEFAULT)) {
            while(linesToWrite.hasMoreElements()) {
                printer.printRecord(linesToWrite.nextElement());
            }
        } catch (IOException e) {
            System.out.println("ERROR writing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeGridsToFile(String fileName, Collection<String> linesToWrite) {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(fileName), CSVFormat.DEFAULT)) {
            for (String line : linesToWrite) {
                printer.printRecord(line);
            }
        } catch (IOException e) {
            System.out.println("ERROR writing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Set<String> readGridsFromFile(String fileName) {
        Set<String> result = new HashSet<>();
        try (Reader in = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                result.add(record.get(0));
            }
        } catch (Exception e) {
            System.out.println("ERROR reading: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private String gridToNrLineString(int[][] grid) {
        String result = "";
        for(int[] row : grid) {
            for(int val : row) {
                result += val;
            }
        }
        return result;
    }

    private String gridToNrLineString_bytes(byte[][] grid) {
        String result = "";
        for(byte[] row : grid) {
            for(byte val : row) {
                result += val;
            }
        }
        return result;
    }

    private int[][] constructColumnArrayFromArray(int[][] arr) {
        int[][] result = new int[9][9];
        for (int i = 0; i < CORRECT_ARR_LENGTH; i++) {
            for (int j = 0; j < CORRECT_ARR_LENGTH; j++) {
                result[j][i] = arr[i][j];
            }
        }
        return result;
    }

    private byte[][] constructColumnArrayFromArray_bytes(byte[][] arr) {
        byte[][] result = new byte[9][9];
        for (int i = 0; i < CORRECT_ARR_LENGTH; i++) {
            for (int j = 0; j < CORRECT_ARR_LENGTH; j++) {
                result[j][i] = arr[i][j];
            }
        }
        return result;
    }

    private int[] getColumnOfGrid(int[][] grid, int columnIndex) {
        int[] result = new int[9];
        for (int i = 0; i < CORRECT_ARR_LENGTH; i++) {
            result[i] = grid[i][columnIndex];
        }
        return result;
    }

    private int[][] createGridOfZeros(int[][] grid) {
        int[][] result = grid != null ? grid : new int[9][9];
        IntStream.range(0, 9).forEach(i -> {
            int[] zeroArr = new int[9];
            Arrays.fill(zeroArr, 0);
            result[i] = zeroArr;
        });
        return result;
    }

    private int[][] generateBoardLocal() {
        List<Integer> oneToNine = Arrays.stream(ONE_TO_NINE).boxed().collect(Collectors.toList());
        //TODO implement [Collections.shuffle or some other way like one by one in a loop-in-loop]
        return BAD_BOARD;
    }

    private int[][] generateBoardBacktrack() {
        int[][] result = createGridOfZeros(null);
//        fillGridDiagonally(result);
//        backtrack(result);
        boolean ok = backtrack(result);
//        if (!ok) {
//            throw new RuntimeException("failed to generate board");
//        }
        return result;
    }

    private int[][] generateBoardPython() {
        int[][] result = createGridOfZeros(null);
//        fillGrid(result);
        boolean ok = fillGrid(result);
//        if (!ok) {
//            throw new RuntimeException("failed to generate board");
//        }
        return result;
    }

    private boolean arrayHasVal(int[] array, int val) {
        for (int it : array) {
            if (it == val) return true;
        }
        return false;
    }

    private boolean isGridFilled(int[][] grid) {
        for (int row = 0; row < 9 ; row++) {
            for (int col = 0; col < 9 ; col++) {
                if (grid[row][col] == 0) return false;
            }
        }
        return true;
    }

    private boolean isGridFilled_byte(byte[][] grid) {
        for (int row = 0; row < 9 ; row++) {
            for (int col = 0; col < 9 ; col++) {
                if (grid[row][col] == 0) return false;
            }
        }
        return true;
    }

    private int identify3by3SquareNr(int row, int col) {
        if (row < 3) {
            if (col < 3) {
                return 1;
            } else if (col < 6) {
                return 2;
            } else {
                return 3;
            }
        } else if (row < 6) {
            if (col < 3) {
                return 4;
            } else if (col < 6) {
                return 5;
            } else {
                return 6;
            }
        } else {
            if (col < 3) {
                return 7;
            } else if (col < 6) {
                return 8;
            } else {
                return 9;
            }
        }
    }

    private int[][] get3by3Square(int[][] grid, int squareNr) {
        int[][] square = new int[3][3];
        int rowStart = squareNr < 4 ? 0 : (squareNr < 7 ? 3 : 6);
        int colStart = COL_ONE_TO_THREE.contains(squareNr) ? 0 : (COL_FOUR_TO_SIX.contains(squareNr) ? 3 : 6);
        for (int i = 0; i < 3 ; i++) {
            for (int j = 0; j < 3 ; j++) {
                square[i][j] = grid[rowStart + i][colStart + j];
            }
        }
        return square;
    }

    private List<Integer> get3by3SquareAsList(int[][] grid, int squareNr) {
        List<Integer> result = new ArrayList<>(9);
        int rowStart = squareNr < 4 ? 0 : (squareNr < 7 ? 3 : 6);
        int colStart = COL_ONE_TO_THREE.contains(squareNr) ? 0 : (COL_FOUR_TO_SIX.contains(squareNr) ? 3 : 6);
        for (int i = 0; i < 3 ; i++) {
            for (int j = 0; j < 3 ; j++) {
                result.add(grid[rowStart + i][colStart + j]);
            }
        }
        return result;
    }

    private int[][] generateRandomPuzzleTillXLeft(int xLeft) {
        return generateRandomPuzzleTillXLeft(generateBoardBacktrack(), xLeft);
    }

    private int[][] generateRandomPuzzleTillXLeft(int[][] grid, int xLeft) {
        int[][] result = duplicate9x9Grid(grid);
        int visible = 81;
        List<Integer> shuffled = new ArrayList<>(ONE_TO_NINE_LIST);
        Collections.shuffle(shuffled);
        int row = shuffled.get(0) - 1;
        int col = shuffled.get(8) - 1;
        while (visible > xLeft) {
            while (result[row][col] == 0) {
                Collections.shuffle(shuffled);
                row = shuffled.get(0) - 1;
                col = shuffled.get(8) - 1;
            }
            result[row][col] = 0;
            visible--;
        }
        return result;
    }

    private void hideGridTillXLeft_old(int[][] grid, int xLeft) {
        int visible = 81;
        List<Integer> shuffled = new ArrayList(ZERO_TO_80_LIST);
        Collections.shuffle(shuffled);
        for (int gridNr : shuffled) {
            int row = gridNr / 9;
            int col = gridNr % 9;
            int value = grid[row][col];
            //if value filled in board
            if (value != 0) {
                grid[row][col] = 0;
                int[][] copiedGrid = duplicate9x9Grid(grid);
                AtomicInteger solutionCount = new AtomicInteger();
                countSolutions(copiedGrid, solutionCount);
                if (solutionCount.get() == 1) {
                    if (--visible == xLeft) {
                        return;
                    }
                } else {
//                    System.out.println("solutionCount: " + solutionCount.get());
                    grid[row][col] = value;
                }
            }
        }
//        while (visible > xLeft) {
//            while (grid[row][col] == 0) {
//                Collections.shuffle(shuffled);
//                row = shuffled.get(0) - 1;
//                col = shuffled.get(8) - 1;
//            }
//            int backup = grid[row][col];
//            grid[row][col] = 0;
//            int[][] copiedGrid = duplicate9x9Grid(grid);
//            AtomicInteger solutionCount = new AtomicInteger();
//            countSolutions(copiedGrid, solutionCount);
//            if (solutionCount.get() == 1) {
//                visible--;
//            } else {
//                System.out.println("solutionCount: " + solutionCount.get());
////                System.out.println("copiedGrid: " + Arrays.deepToString(copiedGrid));
////                System.out.println("grid: " + Arrays.deepToString(grid));
//                grid[row][col] = backup;
//            }
//        }
    }

    private void hideGridTillXLeft_old_2(int[][] grid, int xLeft) {
        int visible = 81;
        int[][] originalGrid = duplicate9x9Grid(grid);
        List<Integer> shuffled = new ArrayList<>(ZERO_TO_80_LIST);
        while (visible > xLeft) {
            Collections.shuffle(shuffled);
            for (int gridNr : shuffled) {
                int row = gridNr / 9;
                int col = gridNr % 9;
                int value = grid[row][col];
                //if value filled in board
                if (value != 0) {
                    grid[row][col] = 0;
                    int[][] copiedGrid = duplicate9x9Grid(grid);
                    AtomicInteger solutionCount = new AtomicInteger();
                    countSolutions(copiedGrid, solutionCount);
                    if (solutionCount.get() == 1) {
                        if (--visible == xLeft) {
                            return;
                        }
                    } else {
                        grid[row][col] = value;
                    }
                }
            }
            visible = 81;
            copy9x9Grid(originalGrid, grid);
        }
    }

    //TODO fix this, probably use recursive also
    private boolean hideGridTillXLeft_new(int[][] grid, List<Integer> shuffled81, AtomicInteger visible, int xLeft, int iteration) {
//        System.out.println("iteration: " + iteration);
        for (int gridNr : shuffled81) {
//            System.out.println("shuffled81 index: " + shuffled81.indexOf(gridNr));
            int rowIndex = gridNr / 9;
            int colIndex = gridNr % 9;
            int value = grid[rowIndex][colIndex];
            //if value filled in board
            if (value != 0) {
                grid[rowIndex][colIndex] = 0;
                int[][] copiedGrid = duplicate9x9Grid(grid);
                AtomicInteger solutionCount = new AtomicInteger();
                countSolutions(copiedGrid, solutionCount);
                if (solutionCount.get() == 1) {
                    visible.getAndDecrement();
                    if (visible.get() == xLeft) {
                        return true;
                    }
                    if (hideGridTillXLeft_new(grid, shuffled81, visible, xLeft, ++iteration)) {
                        return true;
                    }
                } else {
//                    System.out.println("solutionCount: " + solutionCount.get());
//                System.out.println("copiedGrid: " + Arrays.deepToString(copiedGrid));
//                System.out.println("grid: " + Arrays.deepToString(grid));
                    grid[rowIndex][colIndex] = value;
//                    return false;
                }
            }
        }
        return false;
    }

    //TODO fix this, probably use recursive also
    private void hideGridTillXLeft_new2(int[][] grid, List<Integer> shuffled81, AtomicInteger visible, int xLeft, int iteration) {
//        System.out.println("iteration: " + iteration);
        for (int gridNr : shuffled81) {
//            System.out.println("shuffled81 index: " + shuffled81.indexOf(gridNr));
            int rowIndex = gridNr / 9;
            int colIndex = gridNr % 9;
            int value = grid[rowIndex][colIndex];
            //if value filled in board
            if (value != 0) {
                grid[rowIndex][colIndex] = 0;
                int[][] copiedGrid = duplicate9x9Grid(grid);
                AtomicInteger solutionCount = new AtomicInteger();
                countSolutions(copiedGrid, solutionCount);
                if (solutionCount.get() == 1) {
                    visible.getAndDecrement();
                    if (visible.get() == xLeft) {
                        return;
                    }
//                    if (hideGridTillXLeft_new2(grid, shuffled81, visible, xLeft, ++iteration)) {
//                        return true;
//                    }
                } else {
//                    System.out.println("solutionCount: " + solutionCount.get());
//                System.out.println("copiedGrid: " + Arrays.deepToString(copiedGrid));
//                System.out.println("grid: " + Arrays.deepToString(grid));
                    grid[rowIndex][colIndex] = value;
//                    return false;
                }
            }
        }
    }

    //TODO fix this, probably use recursive also
    private boolean hideGridTillXLeft_new2_2(int[][] grid, List<Integer> shuffled81, AtomicInteger visible, int xLeft, int iteration) {
        int[][] originalGrid = duplicate9x9Grid(grid);
        while (visible.get() > xLeft) {
            for (int gridNr : shuffled81) {
                int rowIndex = gridNr / 9;
                int colIndex = gridNr % 9;
                int value = grid[rowIndex][colIndex];
                //if value filled in board
                if (value != 0) {
                    grid[rowIndex][colIndex] = 0;
                    int[][] copiedGrid = duplicate9x9Grid(grid);
                    AtomicInteger solutionCount = new AtomicInteger();
                    countSolutions(copiedGrid, solutionCount);
                    if (solutionCount.get() == 1) {
                        visible.getAndDecrement();
                        if (visible.get() == xLeft) {
                            return true;
                        }
                    } else {
                        grid[rowIndex][colIndex] = value;
                    }
                }
            }
            visible.set(81);
            copy9x9Grid(originalGrid, grid);
            Collections.shuffle(shuffled81);
        }
        return false;
    }

    //TODO fix this, probably use recursive also
    private void hideGridTillXLeft(int[][] grid, int xLeft) {
        int visible = 81;
        List<Integer> shuffled81 = new ArrayList<>(ZERO_TO_80_LIST);
        Collections.shuffle(shuffled81);
        while (visible > xLeft) {
            for (int gridNr : shuffled81) {
                int rowIndex = gridNr / 9;
                int colIndex = gridNr % 9;
//            int[] row = grid[rowIndex];
//            int[] col = getColumnOfGrid(grid, colIndex);
                //if value not filled in board
                int value = grid[rowIndex][colIndex];
                if (value != 0) {
                    grid[rowIndex][colIndex] = 0;
                    int[][] copiedGrid = duplicate9x9Grid(grid);
                    AtomicInteger solutionCount = new AtomicInteger();
                    countSolutions(copiedGrid, solutionCount);
                    if (solutionCount.get() == 1) {
                        visible--;
                        if (visible == xLeft) {
                            return;
                        }
                    } else {
                        System.out.println("solutionCount: " + solutionCount.get());
//                System.out.println("copiedGrid: " + Arrays.deepToString(copiedGrid));
//                System.out.println("grid: " + Arrays.deepToString(grid));
                        grid[rowIndex][colIndex] = value;
                    }
                }
            }
        }
        throw new RuntimeException("failed to hide till " + xLeft + " values left in grid. Got till " + visible + " values left");
    }

    private boolean solveGrid(int[][] grid, AtomicInteger count) {
        for (int i = 0; i < 81; i++) {
            int rowIndex = i / 9;
            int colIndex = i % 9;
            int[] row = grid[rowIndex];
            int[] col = getColumnOfGrid(grid, colIndex);
            //if value not filled in board
            if(grid[rowIndex][colIndex] == 0) {
                for (int val : ONE_TO_NINE) {
                    //if value not already used on this row and column
                    if (!arrayHasVal(row, val) && !arrayHasVal(col, val)){
                        int squareNr = identify3by3SquareNr(rowIndex, colIndex);
//                        int[][] square = get3by3Square(grid, squareNr);
                        List<Integer> square = get3by3SquareAsList(grid, squareNr);
                        //if value not already used on this 3x3 square
//                        if (!arrayHasVal(square[0], val) &&!arrayHasVal(square[1], val) &&!arrayHasVal(square[2], val)) {
                        if (!square.contains(val)) {
                            grid[rowIndex][colIndex] = val;
                            if (isGridFilled(grid)) {
                                count.incrementAndGet();
                                return true;
//                                break;
                            }
                            if (solveGrid(grid, count)) {
                                return true;
                            }
                        }
                    }
                }
                grid[rowIndex][colIndex] = 0;
                return false;
            }
        }
        return false;
    }

    //TODO maybe improve this by going for in for with rows and cols, that would reduce nr of times row and col and 3x3 is resolved
    private boolean countSolutions(int[][] grid, AtomicInteger count) {
        for (int i = 0; i < 81; i++) {
            int rowIndex = i / 9;
            int colIndex = i % 9;
            int[] row = grid[rowIndex];
            int[] col = getColumnOfGrid(grid, colIndex);
            //if value not filled in board
            if(grid[rowIndex][colIndex] == 0) {
                for (int val : ONE_TO_NINE) {
                    //if value not already used on this row and column
                    if (!arrayHasVal(row, val) && !arrayHasVal(col, val)) {
                        int squareNr = identify3by3SquareNr(rowIndex, colIndex);
//                        int[][] square = get3by3Square(grid, squareNr);
                        List<Integer> square = get3by3SquareAsList(grid, squareNr);
                        //if value not already used on this 3x3 square
//                        if (!arrayHasVal(square[0], val) &&!arrayHasVal(square[1], val) &&!arrayHasVal(square[2], val)) {
                        if (!square.contains(val)) {
                            grid[rowIndex][colIndex] = val;
                            if (isGridFilled(grid)) {
                                count.incrementAndGet();
//                                return true;
                                continue;
//                                break;
                            }
                            if (countSolutions(grid, count)) {
                                return true;
                            }
                        }
                    }
                }
                grid[rowIndex][colIndex] = 0;
                return false;
//                break;
//                continue;
            }
//            grid[rowIndex][colIndex] = 0;
//            return false;
        }
        return false;
    }

    /**
     * Do the backtracking job.
     *
     * @return {@code true} if the search shall be aborted by the
     * call hierarchy or {@code false} if search shall continue.
     */
    private boolean backtrack(int[][] grid) {
        for (int i = 0; i < 81; i++) {
            int rowIndex = i / 9;
            int colIndex = i % 9;
            int[] row = grid[rowIndex];
            int[] col = getColumnOfGrid(grid, colIndex);
            //if value not filled in board
            if(grid[rowIndex][colIndex] == 0) {
                List<Integer> shuffled = Arrays.stream(ONE_TO_NINE).boxed().collect(Collectors.toList());
                Collections.shuffle(shuffled);
                for (int val : shuffled) {
                    //if value not already used on this row and column
                    if (!arrayHasVal(row, val) && !arrayHasVal(col, val)){
                        int squareNr = identify3by3SquareNr(rowIndex, colIndex);
                        int[][] square = get3by3Square(grid, squareNr);
                        //if value not already used on this 3x3 square
                        if (!arrayHasVal(square[0], val) &&!arrayHasVal(square[1], val) &&!arrayHasVal(square[2], val)) {
                            grid[rowIndex][colIndex] = val;
                            if (isGridFilled(grid)) {
                                return true;
                            }
                            if (backtrack(grid)) {
                                return true;
                            }
                        }
                    }
                }
                grid[rowIndex][colIndex] = 0;
                return false;
            }
        }
        return false;
    }

    private boolean fillGrid(int[][] grid) {
        for (int i = 0; i < 81; i++) {
            int rowIndex = i / 9;
            int colIndex = i % 9;
            int[] row = grid[rowIndex];
            int[] col = getColumnOfGrid(grid, colIndex);
            //if value not filled in board
            if(grid[rowIndex][colIndex] == 0) {
                List<Integer> shuffled = Arrays.stream(ONE_TO_NINE).boxed().collect(Collectors.toList());
                Collections.shuffle(shuffled);
                for (int val : shuffled) {
                    //if value not already used on this row and column
                    if (!arrayHasVal(row, val) && !arrayHasVal(col, val)){
                        int squareNr = identify3by3SquareNr(rowIndex, colIndex);
//                        int[][] square = get3by3Square(grid, squareNr);
                        List<Integer> square = get3by3SquareAsList(grid, squareNr);
                        //if value not already used on this 3x3 square
//                        if (!arrayHasVal(square[0], val) &&!arrayHasVal(square[1], val) &&!arrayHasVal(square[2], val)) {
                        if (!square.contains(val)) {
                            grid[rowIndex][colIndex] = val;
                            if (isGridFilled(grid)) {
                                return true;
                            }
                            if (fillGrid(grid)) {
                                return true;
                            }
//                            break;
                        }
                    }
                }
                grid[rowIndex][colIndex] = 0;
                return false;
//                if(grid[rowIndex][colIndex] == 0) {
////                    System.out.println("COULDN'T FIND A GOOD VALUE, REITERATING");
//                    fillGrid(createGridOfZeros(grid));
//                    return;
//                }
            }
        }
        return false;
//        if (isGridFilled(grid)) {
//            return;
//        } else {
//            fillGrid(grid);
//        }
    }
}

// https://www.101computing.net/sudoku-generator-algorithm/
//        You can find an example of such an algorithm by investigating the code provided in this Python Challenge:
//        https://www.101computing.net/backtracking-algorithm-sudoku-solver/
//
//        Solution
//
//        Our solution is based on 5 steps:
//
//        Generate a full grid of numbers (fully filled in). This step is more complex as it seems as we cannot just randomly generate numbers to fill in the grid. We have to make sure that these numbers are positioned on the grid following the Sudoku rules. To do so will use a sudoku solver algorithm (backtracking algorithm) that we will apply to an empty grid. We will add a random element to this solver algorithm to make sure that a new grid is generated every time we run it.
//        From our full grid, we will then remove 1 value at a time.
//        Each time a value is removed we will apply a sudoku solver algorithm to see if the grid can still be solved and to count the number of solutions it leads to.
//        If the resulting grid only has one solution we can carry on the process from step 2. If not we will have to put the value we took away back in the grid.
//        We can repeat the same process (from step 2) several times using a different value each time to try to remove additional numbers, resulting in a more difficult grid to solve. The number of attempts we will use to go through this process will have an impact on the difficulty level of the resulting grid.
//
//
//        #Sudoku Generator Algorithm - www.101computing.net/sudoku-generator-algorithm/
//        import turtle
//        from random import randint, shuffle
//        from time import sleep
//
//        #initialise empty 9 by 9 grid
//        grid = []
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//        grid.append([0, 0, 0, 0, 0, 0, 0, 0, 0])
//
//        myPen = turtle.Turtle()
//        myPen.tracer(0)
//        myPen.speed(0)
//        myPen.color("#000000")
//        myPen.hideturtle()
//        topLeft_x=-150
//        topLeft_y=150
//
//        def text(message,x,y,size):
//        FONT = ('Arial', size, 'normal')
//        myPen.penup()
//        myPen.goto(x,y)
//        myPen.write(message,align="left",font=FONT)
//
//        #A procedure to draw the grid on screen using Python Turtle
//        def drawGrid(grid):
//        intDim=35
//        for row in range(0,10):
//        if (row%3)==0:
//        myPen.pensize(3)
//        else:
//        myPen.pensize(1)
//        myPen.penup()
//        myPen.goto(topLeft_x,topLeft_y-row*intDim)
//        myPen.pendown()
//        myPen.goto(topLeft_x+9*intDim,topLeft_y-row*intDim)
//        for col in range(0,10):
//        if (col%3)==0:
//        myPen.pensize(3)
//        else:
//        myPen.pensize(1)
//        myPen.penup()
//        myPen.goto(topLeft_x+col*intDim,topLeft_y)
//        myPen.pendown()
//        myPen.goto(topLeft_x+col*intDim,topLeft_y-9*intDim)
//
//        for row in range (0,9):
//        for col in range (0,9):
//        if grid[row][col]!=0:
//        text(grid[row][col],topLeft_x+col*intDim+9,topLeft_y-row*intDim-intDim+8,18)
//
//
//        #A function to check if the grid is full
//        def checkGrid(grid):
//        for row in range(0,9):
//        for col in range(0,9):
//        if grid[row][col]==0:
//        return False
//
//        #We have a complete grid!
//        return True
//
//        #A backtracking/recursive function to check all possible combinations of numbers until a solution is found
//        def solveGrid(grid):
//        global counter
//        #Find next empty cell
//        for i in range(0,81):
//        row=i//9
//        col=i%9
//        if grid[row][col]==0:
//        for value in range (1,10):
//        #Check that this value has not already be used on this row
//        if not(value in grid[row]):
//        #Check that this value has not already be used on this column
//        if not value in (grid[0][col],grid[1][col],grid[2][col],grid[3][col],grid[4][col],grid[5][col],grid[6][col],grid[7][col],grid[8][col]):
//        #Identify which of the 9 squares we are working on
//        square=[]
//        if row<3:
//        if col<3:
//        square=[grid[i][0:3] for i in range(0,3)]
//        elif col<6:
//        square=[grid[i][3:6] for i in range(0,3)]
//        else:
//        square=[grid[i][6:9] for i in range(0,3)]
//        elif row<6:
//        if col<3:
//        square=[grid[i][0:3] for i in range(3,6)]
//        elif col<6:
//        square=[grid[i][3:6] for i in range(3,6)]
//        else:
//        square=[grid[i][6:9] for i in range(3,6)]
//        else:
//        if col<3:
//        square=[grid[i][0:3] for i in range(6,9)]
//        elif col<6:
//        square=[grid[i][3:6] for i in range(6,9)]
//        else:
//        square=[grid[i][6:9] for i in range(6,9)]
//        #Check that this value has not already be used on this 3x3 square
//        if not value in (square[0] + square[1] + square[2]):
//        grid[row][col]=value
//        if checkGrid(grid):
//        counter+=1
//        break
//        else:
//        if solveGrid(grid):
//        return True
//        break
//        grid[row][col]=0
//
//        numberList=[1,2,3,4,5,6,7,8,9]
//        #shuffle(numberList)
//
//        #A backtracking/recursive function to check all possible combinations of numbers until a solution is found
//        def fillGrid(grid):
//        global counter
//        #Find next empty cell
//        for i in range(0,81):
//        row=i//9
//        col=i%9
//        if grid[row][col]==0:
//        shuffle(numberList)
//        for value in numberList:
//        #Check that this value has not already be used on this row
//        if not(value in grid[row]):
//        #Check that this value has not already be used on this column
//        if not value in (grid[0][col],grid[1][col],grid[2][col],grid[3][col],grid[4][col],grid[5][col],grid[6][col],grid[7][col],grid[8][col]):
//        #Identify which of the 9 squares we are working on
//        square=[]
//        if row<3:
//        if col<3:
//        square=[grid[i][0:3] for i in range(0,3)]
//        elif col<6:
//        square=[grid[i][3:6] for i in range(0,3)]
//        else:
//        square=[grid[i][6:9] for i in range(0,3)]
//        elif row<6:
//        if col<3:
//        square=[grid[i][0:3] for i in range(3,6)]
//        elif col<6:
//        square=[grid[i][3:6] for i in range(3,6)]
//        else:
//        square=[grid[i][6:9] for i in range(3,6)]
//        else:
//        if col<3:
//        square=[grid[i][0:3] for i in range(6,9)]
//        elif col<6:
//        square=[grid[i][3:6] for i in range(6,9)]
//        else:
//        square=[grid[i][6:9] for i in range(6,9)]
//        #Check that this value has not already be used on this 3x3 square
//        if not value in (square[0] + square[1] + square[2]):
//        grid[row][col]=value
//        if checkGrid(grid):
//        return True
//        else:
//        if fillGrid(grid):
//        return True
//        break
//        grid[row][col]=0
//
//        #Generate a Fully Solved Grid
//        fillGrid(grid)
//        drawGrid(grid)
//        myPen.getscreen().update()
//        sleep(1)
//
//
//        #Start Removing Numbers one by one
//
//        #A higher number of attempts will end up removing more numbers from the grid
//        #Potentially resulting in more difficiult grids to solve!
//        attempts = 5
//        counter=1
//        while attempts>0:
//        #Select a random cell that is not already empty
//        row = randint(0,8)
//        col = randint(0,8)
//        while grid[row][col]==0:
//        row = randint(0,8)
//        col = randint(0,8)
//        #Remember its cell value in case we need to put it back
//        backup = grid[row][col]
//        grid[row][col]=0
//
//        #Take a full copy of the grid
//        copyGrid = []
//        for r in range(0,9):
//        copyGrid.append([])
//        for c in range(0,9):
//        copyGrid[r].append(grid[r][c])
//
//        #Count the number of solutions that this grid has (using a backtracking approach implemented in the solveGrid() function)
//        counter=0
//        solveGrid(copyGrid)
//        #If the number of solution is different from 1 then we need to cancel the change by putting the value we took away back in the grid
//        if counter!=1:
//        grid[row][col]=backup
//        #We could stop here, but we can also have another attempt with a different cell just to try to remove more numbers
//        attempts -= 1
//
//        myPen.clear()
//        drawGrid(grid)
//        myPen.getscreen().update()
//
//        print("Sudoku Grid Ready")