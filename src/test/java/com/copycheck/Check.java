package com.copycheck;

// A very small test helper, so we don't need JUnit.
public class Check {

    private static int passed = 0;
    private static int failed = 0;

    // Passes if the condition is true.
    public static void isTrue(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  PASS  " + testName);
        } else {
            failed++;
            System.out.println("  FAIL  " + testName);
        }
    }

    // Passes if expected and actual are equal.
    public static void equal(String testName, Object expected, Object actual) {
        boolean same = (expected == null) ? actual == null : expected.equals(actual);
        if (!same) {
            testName = testName + "  (expected: " + expected + ", got: " + actual + ")";
        }
        isTrue(testName, same);
    }

    // Passes if two decimal numbers are almost equal (floating point is never exact).
    public static void close(String testName, double expected, double actual) {
        boolean near = Math.abs(expected - actual) < 0.0001;
        if (!near) {
            testName = testName + "  (expected: " + expected + ", got: " + actual + ")";
        }
        isTrue(testName, near);
    }

    public static void printSummary() {
        System.out.println();
        System.out.println("Tests passed: " + passed + ", failed: " + failed);
        if (failed == 0) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println("SOME TESTS FAILED");
        }
    }

    public static int getFailed() {
        return failed;
    }
}
