package methods;

// Imports
import java.util.Scanner;
import java.lang.reflect.*;

public class MainLoop{

  public static void main(String [ ] args)
  {
	  // initialize the commandLnInterpreter
	  // will execute the correct command based off the arguments from the command line
    CommandLnInterpreter comLn = new CommandLnInterpreter(args);
    FunctionsFromFile functions = comLn.getFunctionFromFile();



    /*******Testing get Method
    ParseNode.rType[] test = new ParseNode.rType[2];
    test[0] = ParseNode.rType.FLOAT;
    test[1] = ParseNode.rType.FLOAT;

    Method testMethod = functions.getFuncMethod("testsds", test);
    System.out.println(testMethod.getName());
    for (Class<?> para : testMethod.getParameterTypes())
      System.out.println(para);
      */


    String expr;
    Scanner reader = new Scanner(System.in);
    do{
      System.out.print("> ");
      expr = reader.next();
      // parseTree.doEverything(expr);
    } while(!expr.equalsIgnoreCase("q") && !expr.equalsIgnoreCase("quit"));

    System.out.println ( "This is where the main loop will be" );
  }

}
