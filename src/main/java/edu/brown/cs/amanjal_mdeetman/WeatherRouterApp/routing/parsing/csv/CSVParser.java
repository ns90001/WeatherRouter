package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVParser class which will parse a generic csv file.
 */
public class CSVParser {
  private String[] header;
  private final List<String[]> list;
  private boolean hasHeader;

  /**
   * Constructor for CSVParser which instantiates instance variables.
   */
  public CSVParser() {
    list = new ArrayList<>();
    header = null;
    hasHeader = true;
  }

  /**
   * Will parse a csv file, separate the header and store the remainder of the data in a
   * list of arrays of Strings.
   *
   * @param filename file path of the csv file to be parsed
   * @param containsHeader true if file contains a header, false otherwise
   * @return Error message if one occurred. If not, will return an empty String
   */
  public String parse(String filename, boolean containsHeader) {
    hasHeader = containsHeader;
    File csv = new File(filename);
    String ext;
    int extIndex = filename.lastIndexOf('.');
    if (extIndex <= 0) {
      return "ERROR: can not read extension of " + filename;
    } else {
      ext = filename.substring(extIndex + 1);
    }
    BufferedReader fileReader;
    String firstLine;
    if (!ext.equals("csv")) {
      return "ERROR: " + filename + " is not a csv file";
    }
    try {
      fileReader = new BufferedReader(new FileReader(csv));
    } catch (FileNotFoundException e) {
      return "ERROR: " + filename + " not found";
    }
    try {
      firstLine = fileReader.readLine();
    } catch (IOException e) {
      return "ERROR: can not read " + filename;
    }
    if (firstLine == null) {
      return "ERROR: " + filename + " is empty";
    }
    String[] firstLineValues = lineToArr(firstLine);
    if (hasHeader) {
      header = firstLineValues;
    } else {
      list.add(firstLineValues);
    }
    try {
      return readFile(fileReader);
    } catch (IOException e) {
      return "ERROR: could not read " + filename;
    }
  }

  /**
   * Will split a line into an array of fields.
   *
   * @param line line of the csv
   * @return Array of strings representing each of the fields in the csv
   */
  private String[] lineToArr(String line) {
    return line.split(",", -1);
  }

  /**
   * Will read a csv file and transform it into a list of arrays of values.
   *
   * @param fileReader the BufferedReader which will read the csv file
   * @return String containing an error message or the empty string on success
   * @throws IOException when line cannot be read by the file reader
   */
  private String readFile(BufferedReader fileReader) throws IOException {
    String line;
    String[] values;
    while ((line = fileReader.readLine()) != null) {
      values = lineToArr(line);
      int targetLength;
      if (hasHeader) {
        targetLength = header.length;
      } else {
        targetLength = list.get(0).length;
      }
      if (values.length == targetLength) {
        list.add(values);
      } else {
        return "ERROR: csv has a row with the wrong number of values";
      }
    }
    return "";
  }

  /**
   * Getter method for the header of a csv file.
   *
   * @return array of fields in the header of the csv
   */
  public String[] getHeader() {
    return header;
  }

  /**
   * Getter method for the content of the csv.
   *
   * @return List of arrays containing the data in the csv
   */
  public List<String[]> getList() {
    return list;
  }
}
