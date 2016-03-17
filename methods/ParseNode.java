package methods;

public class ParseNode {

  // Token type
  public enum tType {
    IDENTIFIER, VALUE, WRONGO
  }

  // Return types
  public enum rType {
    INT, FLOAT, STRING, VOID, UNSET
  }

  String token;       // Store token string
  tType tokType;  // Identifier or value?
  int tokenPos;       // Track these for error messages
  int tokenLength;
  int numChildren;    // Number of children

  ParseNode [] children;

  //ArrayList<ParseNode> children = new ArrayList();

  // Set this AFTER this initial construction of the tree
  // Used in type checking
  rType retType;

  //Returns an rType of INT, STRING, or FLOAT. based upon the token.
  //if the token is invalid then it will return UNSET
  private rType returnType(String token){

    rType tokenType = rType.UNSET;
    int dot = 0;

    boolean number = true;
    boolean intnum = true;
    int i = 0;
    while(i < token.length()) {
      if ((token.charAt(i) >= '0' && token.charAt(i) <='9') || token.charAt(i) == '.'){
      if (token.charAt(i) == '.') {dot++;}
        if (dot > 1) {number = false;}
        if (dot > 0) {intnum = false;}
      } else {
        number = false;
        intnum = false;
      }
      i++;
    }

    if(token.charAt(0) == '\"' || token.charAt(token.length()-1) == '\"'){
      if(token.charAt(0) == '\"' && token.charAt(token.length()-1) == '\"'){
        System.out.println("String");
        tokenType = rType.STRING;
      } else {
        System.out.println("Invalid String");
        tokenType = rType.UNSET;
      }
    } else if (number == true && !(token.indexOf('.') == -1) && (!(token.indexOf('.') == 0) && !(token.indexOf('.') == token.length()-1))) {
      System.out.println("Float");
      tokenType = rType.FLOAT;
    } else if (intnum == true) {
      System.out.println("Integer");
      tokenType = rType.INT;
    } else {
      System.out.println("Invalid Token");
      tokenType = rType.UNSET;
    }

    return tokenType;

  }

  public ParseNode(String tok, int pos, int numChildren, tType tokType) {
    token = tok;
    this.tokType = tokType;
    tokenLength = tok.length();
    tokenPos = pos;
    this.numChildren = numChildren;
    children = new ParseNode [numChildren];
    // if tokType == VALUE
    //    determine rType
    // else
    //    retType = UNSET
  }

  // Adds a child node to the next free entry in the "children" array
  // children should have the correct size, and the order of its children is irrelevant
  public void addChild(ParseNode cNode) {
    for (int i = 0; i < children.length; i++) {
      if (children[i] == null) {
        children[i] = cNode;
        break;
      }
    }
  }

  // Return a string highlighting a problematic token's position
  public String showToken() {
    String tStr = "";
    for (int i = 0; i < tokenPos; i++)
      tStr += "-";
    tStr += "^";
    return tStr;
  }


  public ParseNode [] getChildren() { return children; }
  public String getToken() { return token; }
  public int getTokenPos() { return tokenPos; }

}
