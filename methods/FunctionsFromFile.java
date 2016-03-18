package methods;

import java.lang.Integer;
import java.lang.reflect.*;
import java.util.*;

public class FunctionsFromFile{
  private Class<?> funClass;
  private Object funClassObj;

  //Contructor for class
  //creates an Object of the class given and stores it as a field also stores the class as a field
  public FunctionsFromFile(Class cl){
    try{
      funClass = cl;
      Constructor<?> classConstructor = funClass.getConstructor(new Class<?>[0]);
      funClassObj = classConstructor.newInstance();
      funClass.cast(funClassObj);
    }catch (NoSuchMethodException e) {
        e.printStackTrace();
        System.exit(0);
    }catch (InstantiationException e) {
        e.printStackTrace();
        System.exit(0);
    }catch (IllegalAccessException e) {
        e.printStackTrace();
        System.exit(0);
    }catch (InvocationTargetException e) {
        e.printStackTrace();
        System.exit(0);
    }
  }

  public ParseNode.rType getReturnRType(Method method){
    Class<?> returnType = method.getReturnType();
    if (returnType == Integer.class || returnType == Integer.TYPE)
      return ParseNode.rType.INT;
    else if (returnType == Float.class || returnType == Float.TYPE)
      return ParseNode.rType.FLOAT;
    else if (returnType == String.class)
      return ParseNode.rType.STRING;
    else
      return ParseNode.rType.VOID;
  }

  //Given a method name and a list of rtypes, return the defined method
  //if there are no method that meet the specification, the return null
  public Method getFuncMethod(String methodName, ParseNode.rType[] paraRTypes)
  {
    //if ((paraRTypes.length == 1) && (paraRTypes[0] == ParseNode.rType.VOID))
      //return null;
    Method[] methodWithName = getMethodFromName(methodName);
    for (Method eachMethod : methodWithName) {
        Class<?>[] paraOfMethod = eachMethod.getParameterTypes();
        if (checkIfSignatureMatch(paraOfMethod, paraRTypes))
          return eachMethod;
    }
      return null;
  }

  //check that the rtype signature matches the real sigature
  //checks both primitve and class type
  public Boolean checkIfSignatureMatch(Class<?>[] methodSig, ParseNode.rType[] paraRTypes){
    for (int i = 0; i < paraRTypes.length; i++ ){
      if (paraRTypes[i] == ParseNode.rType.INT){
        if (!(methodSig[i] == Integer.TYPE) && !(methodSig[i] == Integer.class) ){
          return false;
        }
      }
      else if (paraRTypes[i] == ParseNode.rType.FLOAT){
        if (!(methodSig[i] == Float.TYPE) && !(methodSig[i] == Float.class )){
          return false;
        }
      }
      else if (paraRTypes[i] == ParseNode.rType.STRING){
        if (!(methodSig[i] == String.class) ){
          return false;
        }
      }
    }
    return true;
  }

  //return all the methods that shares the given name
  public Method[] getMethodFromName(String name)
  {
    Method[] methods = funClass.getDeclaredMethods();
    List<Method> methodsToReturn = new ArrayList<Method>();
    for (Method method : methods)
    {
      if (method.getName() == name)
        methodsToReturn.add(method);
    }
    Method[] a = methodsToReturn.toArray(new Method[methodsToReturn.size()]);
    return a;
  }

  //print the functions from file that has a parameters and return type of Float, Int, or String
  public void printFunctionsFromFile(){
    //get all methods
    Method[] allmethods = funClass.getMethods();
    //for each method
      for (Method method : allmethods) {
        if (!validPrintSignature(method.getParameterTypes(), method.getReturnType()))
          continue;
        String printString = "(";
        printString = printString + method.getName();
        for (Class<?> para : method.getParameterTypes()){
          printString += " " + (para.toString());
        }
          printString += ") : " + method.getReturnType().toString();
          System.out.println(printString.replaceAll("class java.lang.", "").toLowerCase());

      }

  }

  //checks that the signature contains Float, String, or Int
  public boolean validPrintSignature(Class<?>[] parameters, Class<?> returnType)
  {
    if (parameters.length == 0)
        return false;
    for (Class<?> para : parameters){
      if (para != Integer.class &&
          para != Float.class   &&
          para != Integer.TYPE  &&
          para != Float.TYPE    &&
          para != String.class  )
          return false;
    }
    if (returnType != Integer.class &&
        returnType != Float.class   &&
        returnType != Integer.TYPE  &&
        returnType != Float.TYPE    &&
        returnType != String.class  )
        return false;
    return true;
  }


}
