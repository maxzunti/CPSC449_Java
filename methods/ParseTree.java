package methods;

import java.text.ParseException;

public class ParseTree {
  //ParseNode tNode;
  //ParseNode.tType tType;
  ParseNode treeHead;

  public ParseTree(ParseNode newHead) {
    treeHead = newHead;
  }

  // Generate a new ParseTree from an expression and return its head
  public ParseNode genTree(String expr, String fullExpr, int offset) {
    ParseNode head;
      head = new ParseNode("garbage", "garbage", 0, 0, ParseNode.tType.WRONGO);
    if (expr.charAt(0) == '(') {   // Expression
      if (expr.charAt(expr.length()-1) == ')') { // Brackets match
        String newExpr = expr.substring(1,expr.length()-1);
        String [] elements = subTokenize(newExpr);
        head = new ParseNode(elements[0], fullExpr, offset+1, elements.length - 1, ParseNode.tType.IDENTIFIER);
        for (int i = 1; i < elements.length; i++) {
          int tempOffset = expr.indexOf(elements[i]);
          head.addChild(genTree(elements[i], fullExpr, offset + tempOffset));
        }
      } else {
        // We should have done this already
        System.out.println("Error: mismatched brackets");
      }
    
    } else { // Value
      head = new ParseNode(expr, fullExpr, offset, 0, ParseNode.tType.VALUE);
    }

    return head;
  }

  // Once we've tokenized and assigned values, attempt to recursively
  // resolve identifiers (and child types) to functions
  public void resolveTypes(ParseNode head, FunctionsFromFile fHelper) throws ParseException {
    ParseNode [] children = head.getChildren();
    for (int i = 0; i < children.length; i++) {
      try {
        resolveTypes(children[i], fHelper);
      } catch (ParseException e) {
        throw e;
      }
    }
    try {
      head.assignReturnType(fHelper);
    } catch (ParseException e) {
      throw e;
    }
  }
  
  // used for debugging - recursively highlights token locations
  void printTokens(String expr, ParseNode head) {
    ParseNode currNode = head; 
    String test = head.getToken();
    System.out.println("***************");
    System.out.println("For token " + test + ":");
    System.out.println(expr);
    System.out.println(head.showToken());
    
    ParseNode [] children = currNode.getChildren();
    for (int i = 0; i < children.length; i++) {
      printTokens(expr, children[i]);
    }
    
  }

  // For a string expr, return an array of tokens and (bracketed expressions)
  // e.g. subTokenize("hey (you (over)) there)") returns
  // tokens[0] = hey, tokens[1] = (you over), tokens[2] = there
  String [] subTokenize(String expr) {
    int tokenCount = countTokens(expr);
    int bDepth = 0;
    int j = 0;

    String [] tokens = new String[tokenCount];
    int tindex = 0;
    boolean inToken = false;
    boolean inBracket = false;
    for (int i = 0; i < expr.length(); i++) {
     if (inToken == false && bDepth == 0) {
        if (expr.charAt(i) == '(') {
          bDepth++;
          tindex = i;
        } else if (expr.charAt(i) != ' ') {
            if (i == expr.length()-1) {
              tokens[j] = expr.substring(i, i+1).trim();
              j++;
            }
          inToken = true;
          tindex = i;
        }
      } else if (inToken) {
        if ((expr.charAt(i) == ' ') || (i == expr.length()-1)) {
          tokens[j] = expr.substring(tindex, i+1).trim();
          inToken = false;
          j++;
        }
      } else if (bDepth > 0) {
        if (expr.charAt(i) == '(')
          bDepth++;
        if (expr.charAt(i) == ')') {
          bDepth--;
          if (bDepth == 0) {
            tokens[j] = expr.substring(tindex, i+1).trim(); 
            j++;
          }
        }
      }
    }
    return tokens;
  }

  // Count of # of tokens we'll want to return from a single expression iteration
  int countTokens(String expr) {
    int bDepth = 0;
    int j = 0;

    int tindex = 0;
    boolean inToken = false;
    boolean inBracket = false;
    for (int i = 0; i < expr.length(); i++) {
     if (inToken == false && bDepth == 0) {
        if (expr.charAt(i) == '(') {
          bDepth++;
          tindex = i;
        } else if (expr.charAt(i) != ' ') {
            if (i == expr.length()-1) // Single-character end token
              j++;
          inToken = true;
          tindex = i;
        }
      } else if (inToken) {
        if ((expr.charAt(i) == ' ') || (i == expr.length()-1)) {
          inToken = false;
          j++;
        }
      } else if (bDepth > 0) {
        if (expr.charAt(i) == '(')
          bDepth++;
        if (expr.charAt(i) == ')') {
          bDepth--;
          if (bDepth == 0) {
            j++;
          }
        }
      }
    }
    return j;
  }
 

  int checkBrackets(String expr) {
  // if good return -1 (or something), if bad, return token number of mismatch
  String expr2 = expr;
  String expr3 = expr;
  int left = 0;
  int right = 0;
  int leftNum = 0;
  int rightNum = 0;
  int index = 0;
  int num = 0;
  int i;

  //find number of left brackets
  while (!(left == -1)){
    left = expr.indexOf('(');
    if (!(left == -1)) {
      expr = expr.substring(left+1);
      leftNum++;
    }
  }

  //find number of right brackets
  while (!(right == -1)){
    right = expr.indexOf(')');
    if (!(right == -1)) {
      expr = expr.substring(right+1);
      rightNum++;
    }
    }

  //Determine index of error
  if (leftNum == rightNum){
    index = -1;
  } else {
    if (leftNum > rightNum){
        num = leftNum - rightNum;
        while (num > 0){
          left = expr2.indexOf('(');
          expr2 = expr2.substring(left+1,expr2.length());
          num--;
        }
        index = expr3.length() - expr2.length() - 1;
        System.out.println("no matching right bracket at index: " + index + " in expression: " + expr3);
    } else {
      num = rightNum - leftNum;
      while (num > 0){
        right = expr2.lastIndexOf(')');
        expr2 = expr2.substring(0,right);
        num--;
      }
      index = right;
      System.out.println("no matching left bracket at index: " + index + " in expression: " + expr3);
    }
  }

    return index;
  }

  public ParseNode getHead() { return treeHead; }
}
