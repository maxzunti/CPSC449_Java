package methods;

// Imports
import java.util.Scanner;
//import methods.ParseNode.rType;
//import methods.ParseNode.tType;

public class MainLoop{

  public static void main(String [ ] args)
  {
	  // initialize the commandLnInterpreter
	  // will execute the correct command based off the arguments from the command line
    CommandLnInterpreter comLn = new CommandLnInterpreter(args);
    FunctionsFromFile functions = comLn.getFunctionFromFile();


    String expr = "(add 1 (add 3 4) )))";
    ParseTree tree = new ParseTree(new ParseNode("", "", 0, 0, ParseNode.tType.WRONGO));
    int test = tree.checkBrackets(expr);
    //ParseNode node = new ParseNode("hi",0,0,tType.VALUE);
    //int test2 = node.returnType(expr);
    System.out.println("Begin loop");

    comLn.printHelpText();
    Scanner reader = new Scanner(System.in);
    do{
      System.out.print("> ");
      expr = reader.nextLine();
      tree = new ParseTree(tree.genTree(expr, expr, 0));
      ParseNode head = tree.getHead();
      // head.finishTree();
      //head.assignMethod(functions);
      //rType test2 = node.returnType(expr);
      // parseTree.doEverything(expr);
    } while(!expr.equals("q") && !expr.equals("quit"));

    System.out.println ( "bye" );
    System.exit(0);
  }

}
