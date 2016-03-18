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

    //Testing the getReturnRType method --------------
    /*Method testMethod = functions.getFuncMethod("add", new ParseNode.rType[] {ParseNode.rType.INT, ParseNode.rType.INT});
    ParseNode.rType rt = functions.getReturnRType(testMethod);
    if (rt == ParseNode.rType.INT)
      System.out.println("GOT INT");
    else
      System.out.println("didnt get INT");*//*
    ParseNode.rType[] testrType = new ParseNode.rType[] {ParseNode.rType.INT};
    Method testMethod = functions.getFuncMethod("add", testrType);
    System.out.println(testMethod.getName());
    for (Class<?> para : testMethod.getParameterTypes())
      System.out.println(para);*/

  //  ParseTree tree;
  //  String expr = "(add 1 (add 3 4) )))";
  /*  try {
    } catch (ParseException e) {
      System.out.println("Critical initialization error");
    }*/
    //int test = tree.checkBrackets(expr);
    //ParseNode node = new ParseNode("hi",0,0,tType.VALUE);
    //int test2 = node.returnType(expr);
    String expr;
    System.out.println("Begin loop");

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
        if (tree.checkBrackets(expr) == -1){
        try {
          // Generate a new parse tree from an expression
          tree = new ParseTree(tree.genTree(expr, expr, 0));
          ParseNode head = tree.getHead();

          // Traverse the tree, assigning types to values and functions
          tree.resolveTypes(head, functions);
        } catch (ParseException e) {
          System.out.println(e.getMessage());
          if (verbose)
            e.printStackTrace();
          }
        }
      }
      // head.finishTree();
      //head.assignMethod(functions);
      //rType test2 = node.returnType(expr);
      // parseTree.doEverything(expr);
    } while(!expr.equals("q"));

    System.out.println ( "bye" );
    System.exit(0);
  }

}
