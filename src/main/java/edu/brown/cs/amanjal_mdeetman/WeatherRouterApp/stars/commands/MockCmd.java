package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.stars.commands;

import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.modelling.MockPerson;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.csv.CSVParser;
import edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input.ICallable;

import java.util.Arrays;
import java.util.List;

/**
 * Class representing the mock command.
 */
public class MockCmd implements ICallable {

  /**
   * Empty constructor for MockCmd.
   */
  public MockCmd() { }

  /**
   * Outputs a list of mock people whose data is present in the provided csv file.
   *
   * @param tokens arguments which should just be the name of the csv
   * @return Either an error message or, if successful, will contain a list of mock people
   */
  @Override
  public String run(List<String> tokens) {
    if (tokens.size() != 1) {
      return "ERROR: mock command should have 1 argument: <csv>";
    }
    CSVParser csvParser = new CSVParser();
    String csv = tokens.get(0);
    try {
      csvParser.parse(csv, true);
    } catch (Exception e) {
      return "";
    }
    /*
    checks the header from the read file to confirm that the csv file contains mock people
    in the correct format
     */
    String[] header = csvParser.getHeader();
    String starsHeader = "[first_name, last_name, datetime, email, gender, street_address]";
    if (!Arrays.toString(header).equals(starsHeader)) {
      return "ERROR: csv should have header \"StarID,ProperName,X,Y,Z\"";
    }
    List<String[]> csvList = csvParser.getList();
    StringBuilder out = new StringBuilder();
    for (String[] strings : csvList) {
      out.append(createMockPerson(strings).toString()).append("\n");
    }
    return out.substring(0, out.length() - 1);
  }

  /**
   * Creates a new instance of MockPerson.
   *
   * @param values an array containing the first name, last name, date, email, gender and
   *               address associated with the mock person
   * @return the newly created MockPerson object
   */
  private MockPerson createMockPerson(String[] values) {
    return new MockPerson(values[0], values[1], values[2], values[3], values[4], values[5]);
  }
}
