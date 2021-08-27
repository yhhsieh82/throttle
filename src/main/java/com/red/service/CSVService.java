package com.red.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVService {
    private List<Integer[]> experimentData = new ArrayList<>();

    public void addOneRecord(Integer[] recordValues) {
        experimentData.add(recordValues);
    }

    public void printRecords() throws IOException {
        FileWriter out = new FileWriter("throttle-experiment.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("Success", "Fail", "Total"))) {
            printer.printRecords(experimentData);
        }
    }
}
