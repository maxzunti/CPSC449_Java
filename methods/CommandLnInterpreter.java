package methods;

import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.*;

public class CommandLnInterpreter{
  private FunctionsFromFile filesFunctions;

  // takes the command line args and execute the correct functions
  public CommandLnInterpreter (String [] commandLnArgs) {
    interpretArgs(commandLnArgs);
  }

  //get the files that contains the functions
  public FunctionsFromFile getFunctionFromFile(){
    return filesFunctions;
  }

  //Given a list of all the command args, execute the apporpriate action
  private void interpretArgs (String [] commandLnArgs){
    //If there is no command line arguments, then print Synopsis
    if (commandLnArgs.length == 0){
      printSynopsis();
      System.exit(0);
    }
    //checks the number of commandLnArg is within range with qualifiers
    else if(commandLnArgs.length > 3){
      System.err.println("This program takes at most two command line arguments.");
      System.err.println(getErrSynopsis());
      System.exit(-2);
    }
    //If there is only one argument and it is a helper argument, then print message
    else if (validHelpArg(commandLnArgs[0]) && commandLnArgs.length == 1){
      printSynopsis();
      System.out.println ("\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding");
      System.out.println ("methods in <class-name>, and executes them, printing the result to sysout.");
      System.exit(0);
    }
    //when there is help and more args, then error message
    else if(validHelpArg(commandLnArgs[0]) && commandLnArgs.length > 1){
      System.err.println("Qualifier --help (-h, -?) should not appear with any comand-line arguments.");
      System.err.println(getErrSynopsis());
      System.exit(-4);
    }
    else if (validVerboseArg(commandLnArgs[0]) && commandLnArgs.length == 2 ){
      if (commandLnArgs[1].length() <= 4){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      if (!commandLnArgs[1].substring(commandLnArgs[1].length() - 4).equals(".jar")){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }

      filesFunctions = getClassFromJar(commandLnArgs[1], "Commands");
      MainLoop.verbose = true;
    }
    else if (validVerbHelpArg(commandLnArgs[0]) && commandLnArgs.length == 2 ){
      if (commandLnArgs[1].length() <= 4){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      if (!commandLnArgs[1].substring(commandLnArgs[1].length() - 4).equals(".jar")){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      printSynopsis();
      System.out.println ("\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding");
      System.out.println ("methods in <class-name>, and executes them, printing the result to sysout.");
      filesFunctions = getClassFromJar(commandLnArgs[1], "Commands");
      MainLoop.verbose = true;
    }
    //If there is a verbrose argument first followed by a valid jar and class name, then continue
    else if (validVerboseArg(commandLnArgs[0]) && commandLnArgs.length == 3){
      if (commandLnArgs[1].length() <= 4){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      if (!commandLnArgs[1].substring(commandLnArgs[1].length() - 4).equals(".jar")){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      filesFunctions = getClassFromJar(commandLnArgs[1], commandLnArgs[2]);
      MainLoop.verbose = true;
    }
    //If there is a verbroseHelper argument first followed by a valid jar and class name, then continue
    else if (validVerbHelpArg(commandLnArgs[0]) && commandLnArgs.length == 3){
      if (commandLnArgs[1].length() <= 4){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      if (!commandLnArgs[1].substring(commandLnArgs[1].length() - 4).equals(".jar")){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      printSynopsis();
      System.out.println ("\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding");
      System.out.println ("methods in <class-name>, and executes them, printing the result to sysout.");
      filesFunctions = getClassFromJar(commandLnArgs[1], commandLnArgs[2]);
      MainLoop.verbose = true;
    }
    //If there is a valid jar and class name, then continue
    else if (commandLnArgs.length == 2){
      if (commandLnArgs[0].length() <= 4){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      if (!commandLnArgs[0].substring(commandLnArgs[0].length() - 4).equals(".jar")){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      filesFunctions = getClassFromJar(commandLnArgs[0], commandLnArgs[1]);
    }
    //Give errors to unrecognized qualifiers that start with '--'
    else if (commandLnArgs[0].substring(0, 2).equals("--")){
      System.err.println("Unrecognized qualifier: --" + commandLnArgs[0].substring(2, commandLnArgs[0].length()) + ".");
      System.err.println(getErrSynopsis());
      System.exit(-1);
    }
    //Give errors to unrecognized qualifiers that start with '-'
    else if (commandLnArgs[0].substring(0, 1).equals("-")){
      System.err.println("Unrecognized qualifier '" + commandLnArgs[0].charAt(1) + "' in '" + commandLnArgs[0] + "'.");
      System.err.println(getErrSynopsis());
      System.exit(-1);
    }
    //checks the number of commandLnArg is within range with no qualifiers
    else if(commandLnArgs.length > 2){
      System.err.println("This program takes at most two command line arguments.");
      System.err.println(getErrSynopsis());
      System.exit(-2);
    }
    else if (commandLnArgs.length == 1){
      if (commandLnArgs[0].length() <= 4){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      if (!commandLnArgs[0].substring(commandLnArgs[0].length() - 4).equals(".jar")){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        System.err.println(getErrSynopsis());
        System.exit(-3);
      }
      filesFunctions = getClassFromJar(commandLnArgs[0], "Commands");
    }
    //if none of the above the print error message and quit
    else{
        System.out.println("Bad Args / Not capturing a fatal errors");

      }
  }

  //Given a jarName and class, return a FunctionsFromFile object
  public FunctionsFromFile getClassFromJar(String jarName, String className){
    try{
      //prep for extracting class
      File f = new File(jarName);
      //Checks if the jar file exists
      if(!(f.exists() && !f.isDirectory())){
        System.err.println("Could not load jar file: <" + jarName + ">");
        System.exit(-5);
      }
      URL[] urls = new URL[1];
      urls[0] = f.toURI().toURL();
      URLClassLoader cl = URLClassLoader.newInstance(urls);
      //get class from jar
      Class<?> c = Class.forName(className, true, cl);
      //instaniate functions from file
      FunctionsFromFile functionClass = new FunctionsFromFile(c);
      return functionClass;
      }catch(MalformedURLException e){
        System.out.println("MalformedURLException");
        System.exit(0);
      }catch(ClassNotFoundException e){
        System.err.println("Could not find class: <" + className + ">");
        System.exit(-6);
      }
      System.out.println("This should never happen");
      return null;
    }

/****************CHECK valid args****************************/
//check if string is verboseHelper arg
  private Boolean validVerbHelpArg(String helpStr){
    if (helpStr.equals("-hv")){
      return true;
    }else if (helpStr.equals("-?v")){
      return true;
    }else if (helpStr.equals("-vh")){
      return true;
    }else if (helpStr.equals("-v?")){
      return true;
    }else{
      return false;
    }
  }
  // check if a string is a verbose arg
  private Boolean validVerboseArg(String helpStr){
    if (helpStr.equals("-v")){
      return true;
    }else if (helpStr.toLowerCase().startsWith("--v")){
      return true;
    }else{
      return false;
    }
  }

  // check if a string is a help arg
  private Boolean validHelpArg (String helpStr){
    if (helpStr.equals("-h")){
      return true;
    }else if (helpStr.equals("-?")){
      return true;
    }else if (helpStr.toLowerCase().startsWith("--h")){
      return true;
    }else{
      return false;
    }
  }

/********Print Methods****************************/
  //print Synopsis
  public void printSynopsis (){
    System.out.println ("Synopsis:");
    System.out.println ("  methods");
    System.out.println ("  methods { -h | -? | --help }+");
    System.out.println ("  methods {-v --verbose}* <jar-file> [<class-name>]");
    System.out.println ("Arguments:");
    System.out.println ("  <jar-file>:   The .jar file that contains the class to load (see next line).");
    System.out.println ("  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"] ");
    System.out.println ("Qualifiers:");
    System.out.println ("  -v --verbose: Print out detailed errors, warning, and tracking.");
    System.out.println ("  -h -? --help: Print out a detailed help message.");
    System.out.println ("Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.");
  }
  public String getErrSynopsis (){
    return "Synopsis:\n  methods\n  methods { -h | -? | --help }+\n methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"] \nQualifiers:\n -v --verbose: Print out detailed errors, warning, and tracking.\n -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.";
  }

  public void printHelpText(){
    System.out.println("q           : Quit the program.");
    System.out.println("v           : Toggle verbose mode (stack traces).");
    System.out.println("f           : List all known functions.");
    System.out.println("?           : Print this helpful text.");
    System.out.println("<expression>: Evaluate the expression.");
    System.out.println("Expressions can be integers, floats, strings (surrounded in double quotes) or function");
    System.out.println("calls of the form '(identifier {expression}*)'.");
  }

}
