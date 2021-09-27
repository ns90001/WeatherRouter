//package edu.brown.cs.mdeetman.parsing.input;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Repl class which is primarily in charge of reading user input, printing, and looping.
// */
//public class Repl {
//
//  /** Constructor for the repl which stars looping and reading inputs.
//   *
//   * @param eval an evaluator objects which will evaluate the parsed user inputs
//   */
//  public Repl(Evaluator eval) {
//    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//    String line;
//    while (true) {
//      try {
//        line = readLine(input, eval);
//        if (line != null) {
//          if (line.length() > 0) {
//            System.out.println(line);
//          }
//        }
//      } catch (IOException e) {
//        System.out.println("ERROR: could not read user input");
//        break;
//      }
//    }
//  }
//
//  /**
//   * Reads and parses a line from the user and then evaluates based on the input.
//   *
//   * @param input the BufferedReader which will read user input
//   * @param eval an evaluator object which will perform the evaluating step of the REPL
//   * @return the output from the evaluator which will be printed
//   * @throws IOException exception when a line cannot be read by the BufferedReader
//   */
//  private String readLine(BufferedReader input, Evaluator eval) throws IOException {
//    String line = input.readLine();
//    if (line == null) {
//      System.exit(0);
//    }
//    List<String> tokens = new ArrayList<>();
//    /*
//    regex below found on stack overflow:
//    https://stackoverflow.com/questions/7804335/
//    by user: aioobe
//     */
//    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
//    while (m.find()) {
//      tokens.add(m.group(1));
//    }
//    return eval.evaluate(tokens);
//  }
//}

package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that supports the user-facing REPL and its usage.
 */
public class REPL {
  private final HashMap<String, ICallable> commandToActions;
  /**
   * REPL constructor.
   *
   * @param commandToActions the list of usable commands
   */
  public REPL(HashMap<String, ICallable> commandToActions) {
    this.commandToActions = commandToActions;
  }

  /**
   * Method to start the loop of the repl and take in input.
   */
  public void run() {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in,
        StandardCharsets.UTF_8));
    while (true) {
      try {
        String line = in.readLine();
        if (line == null) {
          in.close();
          System.exit(0);
        } else {
          List<String> tokens = new ArrayList<>();
          /*
          regex below found on stack overflow:
          https://stackoverflow.com/questions/7804335/
          by user: aioobe
           */
          Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
          while (m.find()) {
            tokens.add(m.group(1));
          }
          String out = stringRun(tokens);
          if (out != null) {
            if (out.length() > 0) {
              System.out.println(out);
            }
          }
        }
      } catch (IOException e) {
        System.out.println("ERROR: could not read file");
        break;
      }
    }
  }

  /**
   * Method to evaluate the first token and then call the proper command.
   * @param tokens - a string list.
   * @return - string from the executed command.
   */
  public String stringRun(List<String> tokens) {
    if (tokens.size() == 0) { // if statement to return error if user just clicks enter
      return "ERROR: Invalid Command";
    }
    String cmd = tokens.get(0).toLowerCase();
    tokens.remove(0);
    ICallable command = commandToActions.get(cmd);
    if (command == null) {
      return String.format("ERROR: %s is not a recognized command", cmd);
    } else {
      //calls execute with the arguments for the command, not the command name itself
      return (command.run(tokens));
    }
  }
}
