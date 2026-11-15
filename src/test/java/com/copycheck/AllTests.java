package com.copycheck;

// Runs every test class. Each issue un-comments its own line below.
public class AllTests {

    public static void main(String[] args) {
        System.out.println("Running CopyCheck tests...");

        DocumentTest.run();

        FileLoaderTest.run();             // CC-03

        TextCleanerTest.run();            // CC-04

        SentenceSplitterTest.run();       // CC-04

        // WordFrequencyTest.run();       // CC-05

        CommonSentencesTest.run();        // CC-06

        // ComparerTest.run();            // CC-07

        // ReportPrinterTest.run();       // CC-07

        // RankerTest.run();              // CC-09

        // ThreeWordSequenceTest.run();   // CC-10

        // ReportSaverTest.run();         // CC-11

        Check.printSummary();
        if (Check.getFailed() > 0) {
            System.exit(1);
        }
    }
}
