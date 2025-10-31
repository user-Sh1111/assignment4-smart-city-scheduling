package main;

import utils.GraphGenerator;

/**
 * Main class for generating test datasets.
 */
public class GenerateDatasets {
    public static void main(String[] args) {
        System.out.println("Generating test datasets...");

        try {
            GraphGenerator.generateDatasets();
            System.out.println("All datasets generated successfully!");
            System.out.println("Check the /data/ folder for generated JSON files");
        } catch (Exception e) {
            System.err.println("Error generating datasets: " + e.getMessage());
            e.printStackTrace();
        }
    }
}