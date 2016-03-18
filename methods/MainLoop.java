package methods;

// Imports
import java.util.Scanner;
import java.text.ParseException;
//import methods.ParseNode.rType;
//import methods.ParseNode.tType;
import java.lang.reflect.*;

public class MainLoop{

  public static boolean verbose = false;

  public static void main(String [ ] args)
  {
    // initialize the commandLnInterpreter
    // will execute the correct command based off the arguments from the command line
    CommandLnInterpreter comLn = new CommandLnInterpreter(args);
    FunctionsFromFile functions = comLn.getFunctionFromFile();
    ParseTree tree = new ParseTree(new ParseNode("", "", 0, 0, ParseNode.tType.WRONGO));

    String expr;

    comLn.printHelpText();
    Scanner reader = new Scanner(System.in);
    do{
      System.out.print("> ");
      expr = reader.nextLine();
      if (expr.equals("")) {
        continue;
      } else if (expr.equals("v")){
        if (verbose == true){
          System.out.println("Verbose off.");
          verbose = false;
        }
        else {
          System.out.println("Verbose on.");
          verbose = true;
        }
      }
      else if (expr.equals("f")){
        functions.printFunctionsFromFile();
      }
      else if (expr.equals("?")){
        comLn.printHelpText();
      }
      else if (expr.equals("q"))
        continue;

      else{
        try{
          if (tree.checkBrackets(expr) == -1){

          // Generate a new parse tree from an expression
          tree = new ParseTree(tree.genTree(expr, expr, 0));
          ParseNode head = tree.getHead();

          // Traverse the tree, assigning types to values and functions
          tree.resolveTypes(head, functions);
          // Compute the final result of the (now verified) tree
          System.out.println(tree.computeTree(head, functions).toString());
        }
        } catch (ParseException e) {
          System.out.println(e.getMessage());
          if (verbose)
            e.printStackTrace();
        }
      }
    } while(!expr.equals("q"));

    System.out.println ( "bye" );
    System.exit(0);
  }

}
