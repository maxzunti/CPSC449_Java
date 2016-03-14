package methods;

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

    System.out.println ( "This is where the main loop will be" );
  }

}
