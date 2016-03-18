package methods;

import java.lang.reflect.*;
import java.text.ParseException;

public class ParseNode {

  // Token type
  public enum tType {
    IDENTIFIER, VALUE, WRONGO
  }

  // Return types
  public enum rType {
    INT, FLOAT, STRING, VOID, UNSET, INVALID;

    @Override
    public String toString() {
      switch(this) {
        case INT: return "int";
        case FLOAT: return "float";
        case STRING: return "string";
        default: throw new IllegalArgumentException();
      }
    }
  }

  String token;       // Store token string
  String expr;        // Full expression
  tType tokType;  // Identifier or value?
  int tokenPos;       // Track these for error messages
  int tokenLength;
  int numChildren;    // Number of children
  Method nodeFunction; // Only set for IDENTIFIER (function) nodes

  ParseNode [] children;

  // Contains the resolved (and type-correct) value of the node
  Object value;

// Set this AFTER this initial construction of the tree
  // Used in type checking
  rType retType;

  public ParseNode(String tok, String expr, int pos, int numChildren, tType tokType) {
    token = tok;
    this.expr = expr;
    this.tokType = tokType;
    tokenLength = tok.length();
    tokenPos = pos;
    this.numChildren = numChildren;
    children = new ParseNode [numChildren];

    nodeFunction = null;
  }

  // Assign rTypes to all unset nodes (i.e. all IDENTIFIER nodes with
  // specific return types) based on the children nodes and their types
  public void assignReturnType(FunctionsFromFile funcHelper) throws ParseException {
    if (tokType == tType.IDENTIFIER) {
      rType [] childTypes = new rType [numChildren];
      for (int i = 0; i < numChildren; i++) {
        childTypes[i] = children[i].getRType();
      }
      nodeFunction = funcHelper.getFuncMethod(token, childTypes);
      if (nodeFunction == null) {
        String errMsg = "Matching function for '(" + token;
        for (int i = 0; i < childTypes.length; i++) {
          errMsg += " " + childTypes[i].toString();
        }
        errMsg += ")' not found at offset " + tokenPos + "\n" + showToken();
        throw new ParseException(errMsg, tokenPos);
      } else { // nodeFunction is actually assigned
        retType = funcHelper.getReturnRType(nodeFunction);
      }
    } else {  // tokType == tType.VALUE
      // Assign type to value
      try {
        retType = returnType(token);
        if (retType == rType.INT) {
          value = Integer.parseInt(token);
        } else if (retType == rType.FLOAT) {
          value = Float.parseFloat(token);
        } else if (retType == rType.STRING) {
          value = token;
        } else {
          throw new ParseException("Critical error: ambiguous return type", tokenPos);
        }
      } catch (ParseException e) {
        throw e;
      }
    }
  }


  //Returns an rType of INT, STRING, or FLOAT. based upon the token.
  //if the token is invalid then it will return UNSET
  private rType returnType(String token) throws ParseException {

    rType tokenType = rType.UNSET;
    int dot = 0;

    boolean number = true;
    boolean intnum = true;
    int i = 0;

    // Annoying Case
    if (expr.indexOf('(') == -1 && expr.indexOf(')') == -1 && (token.charAt(0) == '\"' || token.charAt(token.length()-1) == '\"') && !(token.charAt(0) == '\"' && token.charAt(token.length()-1) == '\"')){
      throw new ParseException("Unexpected character encountered at offset " + tokenPos + "\n" + showToken(), tokenPos);
    } else {

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
        tokenType = rType.STRING;
        this.token = token.substring(1, token.length()-1);
      } else {
        tokenType = rType.INVALID;
        String Str = "";
        for (int j = 0; j < expr.length(); j++)
          Str += "-";
        Str += "^";
      throw new ParseException("Encountered end-of-input while reading string beginning at offset " + tokenPos + " at offset " + expr.length() + "\n" + expr + "\n" + Str, tokenPos);
      }
    } else if (number == true && !(token.indexOf('.') == -1) && !(token.indexOf('.') == 0)) {
      tokenType = rType.FLOAT;
    } else if (intnum == true) {
      tokenType = rType.INT;
    } else {
      tokenType = rType.INVALID;
      throw new ParseException("Unexpected character encountered at offset " + tokenPos + "\n" + showToken(), tokenPos);
      }
    }
    return tokenType;
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
    return (expr + "\n" + tStr);
  }


  public ParseNode [] getChildren() { return children; }
  public String getToken() { return token; }
  public int getTokenPos() { return tokenPos; }
  public rType getRType() { return retType; }
  public tType getTType() { return tokType; }
  public void setValue(Object newVal) { value = newVal; }
  public Object getValue() { return value; }
  public Method getMethod() { return nodeFunction; }
}
