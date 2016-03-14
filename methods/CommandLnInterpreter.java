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

/*****
TODO: print correct error message
TODO: find out about Truncations
*****/
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
    //If there is only one argument and it is a helper argument, then print message
    else if (validHelpArg(commandLnArgs[0]) && commandLnArgs.length == 1){
      printSynopsis();
      System.out.println ("\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding");
      System.out.println ("methods in <class-name>, and executes them, printing the result to sysout.");
      System.exit(0);
    }
    //If there is a verbrose argument first followed by a valid jar and class name, then continue
    // TODO: check Valid jar and class
    else if (validVerboseArg(commandLnArgs[0]) && commandLnArgs.length == 3){
      filesFunctions = getClassFromJar(commandLnArgs[1], commandLnArgs[2]);

    }
    //If there is a verbroseHelper argument first followed by a valid jar and class name, then continue
    else if (validVerbHelpArg(commandLnArgs[0]) && commandLnArgs.length == 3){
      printSynopsis();
      System.out.println ("\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding");
      System.out.println ("methods in <class-name>, and executes them, printing the result to sysout.");
      filesFunctions = getClassFromJar(commandLnArgs[1], commandLnArgs[2]);

    }
    //If there is a valid jar and class name, then continue
    else if (commandLnArgs.length == 2){
      filesFunctions = getClassFromJar(commandLnArgs[0], commandLnArgs[1]);
    }
    //if none of the above the print error message and quit
    //TODO: print correct error message
    else{
      System.out.println ("Bad args");
      System.exit(0);
    }
  }

  public FunctionsFromFile getClassFromJar(String jarName, String className){
    try{
      File f = new File(jarName);
      URL[] urls = new URL[1];
      urls[0] = f.toURI().toURL();
      URLClassLoader cl = URLClassLoader.newInstance(urls);
      Class<?> c = Class.forName(className, true, cl);
      FunctionsFromFile functionClass = new FunctionsFromFile(c);
      return functionClass;
      }catch(MalformedURLException e){
        e.printStackTrace();
      }catch(ClassNotFoundException e){
        e.printStackTrace();
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
    }else if (helpStr.equals("--verbose")){
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
    }else if (helpStr.equals("--help")){
      return true;
    }else{
      return false;
    }
  }

/*
  public String doubleMinusTrunc(String orignalStr){
    if (orignalStr.substring(0,2) == "--" ){
      orignalStr.toLowerCase();
      return
    }

  }*/
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

}
