package methods;

public class ParseTree {
  //ParseNode tNode;
  //ParseNode.tType tType;

  public ParseTree(String expr) {

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
}
