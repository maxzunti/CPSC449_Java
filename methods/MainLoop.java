package methods;

public class MainLoop{

  public static void main(String [ ] args)
  {
	// initialize the commandLnInterpreter
	// will execute the correct command based off the arguments from the command line
    CommandLnInterpreter comLn = new CommandLnInterpreter(args);
    System.out.println ( "This is where the main loop will be" );
  }

}
