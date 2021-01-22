package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column).toLowerCase(); //added .toLowerCase() for case insensitivity requirement.
            if (aValue.contains(value)) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /** findByValue task
     * want it to iterate through the ArrayList, then through the HashMap objects and check each key/value pair (objects) for the value we pass through (the search term). If an object contains that term, we want to add that job object to an ArrayList of HashMap objects which will make up the results for the user.**/
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        value = value.toLowerCase();

        ArrayList<HashMap<String, String>> jobRecords = new ArrayList<>();
        // line 95 creates the ArrayList of HashMaps that will act as results the users sees. This is where the code pushes the matching job records based on the search term.
        for (int i = 0; i < allJobs.size(); i++) {
            String aJob = "";
            for (Map.Entry<String, String> jobListing : allJobs.get(i).entrySet()) {
                //line 99 states jobListing is of type Map.Entry<String, String>, which "represents the key/value pairs within HashMaps". So jobListing is a HashMap object, and in this case it's one of many objects stored within the allJobs ArrayList. The .get() method gets us the value of one of those objects (the index we're on in our loop at that time) and entrySet() groups the keys and values together from that index point.
                aJob = aJob + jobListing.getValue();
                //line 101 now adds to the empty string for aJob with the value side of the key/value pairs (ex: employer(key): Splitwise(value)) from the jobListing HashMap for each iteration (via concatenation). These are the values that are checked against the value argument we pass through (searchTerm).
            }
            aJob = aJob.toLowerCase();
            //moved .toLowerCase() outside of loop

            if (aJob.contains(value)) {
                jobRecords.add(allJobs.get(i));
            } //had to move the conditional statement in line 107 out of the HashMap loop to prevent duplicates. Initialized and declared aJob outside of the HashMap loop (as opposed to inside) as a result.

        }
        return jobRecords;
    };

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
