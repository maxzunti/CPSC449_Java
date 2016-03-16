
public class ParseNode {

  // Token type
  public enum tType {
    IDENTIFIER, VALUE
  }
  
  // Return types
  public enum rType {
    INT, FLOAT, STRING, VOID, UNSET
  }

  String token;       // Store token string
  tType valType;  // Identifier or value?
  int tokenPos;       // Track these for error messages
  int tokenLength;
  int numChildren;    // Number of children

  ParseNode [] children;
  
  //ArrayList<ParseNode> children = new ArrayList();

  // Set this AFTER this initial construction of the tree
  // Used in type checking
  rType retType;

  public ParseNode(int numChildren) {
    retType = rType.UNSET;
    children = new ParseNode[numChildren];
  }

//  ArrayList<ParseNode> children;

}
