package edu.brown.cs.amanjal_mdeetman.WeatherRouterApp.routing.parsing.input;

import java.util.List;

/**
 * Interface for running each of the supported repl commands.
 */
public interface ICallable {
  /**
   * A run method that every iCallable class has to obtain the results string.
   * @param tokens the repl input
   * @return a String result
   */
  String run(List<String> tokens);
}
