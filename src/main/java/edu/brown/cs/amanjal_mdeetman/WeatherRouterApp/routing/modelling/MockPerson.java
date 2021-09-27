package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.modelling;

import java.util.regex.Pattern;

/**
 * MockPerson class which stores information about a mock person.
 */
public class MockPerson {
  private String firstName;
  private String lastName;
  private String datetime;
  private String email;
  private String gender;
  private String address;

  /**
   * Constructor for MockPerson which instantiates local variables by calling mutator methods.
   *
   * @param mockFirstName first name of person
   * @param mockLastName last name of person
   * @param mockDate date
   * @param mockEmail email of person
   * @param mockGender gender of person
   * @param mockAddress street address of person
   */
  public MockPerson(String mockFirstName, String mockLastName, String mockDate, String mockEmail,
                    String mockGender, String mockAddress) {
    //calling mutator methods allow fields to be checked for validity
    setFirstName(mockFirstName);
    setLastName(mockLastName);
    setDatetime(mockDate);
    setEmail(mockEmail);
    setGender(mockGender);
    setAddress(mockAddress);
  }

  /**
   * Mutator method for the first name of the mock person.
   *
   * @param mockFirstName first name of mock person
   */
  public void setFirstName(String mockFirstName) {
    this.firstName = mockFirstName;
  }

  /**
   * Mutator method for the last name of the mock person.
   *
   * @param mockLastName last name of mock person
   */
  public void setLastName(String mockLastName) {
    this.lastName = mockLastName;
  }

  /**
   * Mutator method for the date.
   *
   * @param mockDate date in the form MM/DD/YYYY
   */
  public void setDatetime(String mockDate) {
    //checks that the date is in the form MM/DD/YYYY
    String regex = "^([1-9]|1[0-2])(/)([1-9]|1[0-9]|2[0-9]|3[0-1])(/)\\d{4}$";
    Pattern datePattern = Pattern.compile(regex);
    if (datePattern.matcher(mockDate).matches()) {
      this.datetime = mockDate;
    } else {
      this.datetime = "";
    }
  }

  /**
   * Mutator method for the email of the mock person.
   *
   * @param mockEmail email of mock person
   */
  public void setEmail(String mockEmail) {
    //checks that the email contains text, followed by an @, followed by more text
    Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
    if (emailPattern.matcher(mockEmail).matches()) {
      this.email = mockEmail;
    } else {
      this.email = "";
    }
  }

  /**
   * Mutator method for the gender of the mock person.
   *
   * @param mockGender gender of the mock person
   */
  public void setGender(String mockGender) {
    this.gender = mockGender;
  }

  /**
   * Mutator method for the street address of the mock person.
   *
   * @param mockAddress street address where mock person lives
   */
  public void setAddress(String mockAddress) {
    this.address = mockAddress;
  }

  /**
   * Gives the String representation of the mock person.
   *
   * @return information about the mock person in the form "Name: a,Date: b,
   * Email: c,Gender: d,Address e".
   * All empty fields will be omitted.
   */
  @Override
  public String toString() {
    String out = "";
    if (firstName.length() != 1) {
      out += "Name: " + firstName;
      if (lastName.length() != 0) {
        out += " " + lastName;
      }
      out += ",";
    } else if (lastName.length() != 0) {
      out += "Name: " + lastName + ",";
    }
    if (datetime.length() != 0) {
      out += "Date: " + datetime + ",";
    }
    if (email.length() != 0) {
      out += "Email: " + email + ",";
    }
    if (gender.length() != 0) {
      out += "Gender: " + gender + ",";
    }
    if (address.length() != 0) {
      out += "Address: " + address;
    } else {
      out = out.substring(0, out.length() - 1);
    }
    return out;
  }
}
