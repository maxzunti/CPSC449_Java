package methods;

// Imports
import java.util.Scanner;

public class MainLoop{

  public static void main(String [ ] args)
  {
	  // initialize the commandLnInterpreter
	  // will execute the correct command based off the arguments from the command line
    CommandLnInterpreter comLn = new CommandLnInterpreter(args);
    FunctionsFromFile functions = comLn.getFunctionFromFile();

    ////*******Testing executeMethod DOES NOT WORK
    //Integer test1 = new Integer(1);
    //int test1 = 30;
    //Integer test2 = new Integer(2);
    //Object[] array = {test1};
    //System.out.println(test1.getClass());
    //functions.executeMethod("inc", array);
    String expr;
    Scanner reader = new Scanner(System.in);
    ParseTree newTree = new ParseTree("");
    do{
      System.out.print("> ");
      expr = reader.nextLine();
      expr = expr.replaceAll(" +", " ");
      // parseTree.doEverything(expr);
      ParseNode newNode = newTree.genTree(expr, 0);
    } while(!expr.equals("q") && !expr.equals("quit"));

    System.out.println ( "This is where the main loop will be" );
  }

}
