package methods;

import java.lang.Integer;
import java.lang.reflect.*;
import java.util.*;

public class FunctionsFromFile{
  private Class<?> funClass;
  private Object funClassObj;

  public FunctionsFromFile(Class cl){
    try{
      funClass = cl;
      Constructor<?> classConstructor = funClass.getConstructor(new Class<?>[0]);
      funClassObj = classConstructor.newInstance();
      funClass.cast(funClassObj);
    }catch (NoSuchMethodException e) {
            e.printStackTrace();
    }catch (InstantiationException e) {
        e.printStackTrace();
    }catch (IllegalAccessException e) {
        e.printStackTrace();
    }catch (InvocationTargetException e) {
        e.printStackTrace();
    }
  }

  public Method getFuncMethod(String methodName, ParseNode.rType[] paraRTypes)
  {
    if ((paraRTypes.length == 1) && (paraRTypes[0] == ParseNode.rType.VOID))
      return null;
    Method[] methodWithName = getMethodFromName(methodName);
    for (Method eachMethod : methodWithName) {
        Class<?>[] paraOfMethod = eachMethod.getParameterTypes();
        if (checkIfSignatureMatch(paraOfMethod, paraRTypes))
          return eachMethod;
    }
      return null;
  }

  public Boolean checkIfSignatureMatch(Class<?>[] methodSig, ParseNode.rType[] paraRTypes){
    Integer anInteger = 1;
    Float aFlaot = new Float(2.0);
    String aString = "test";
    for (int i = 0; i < paraRTypes.length; i++ ){
      if (paraRTypes[i] == ParseNode.rType.INT){
        if (!(methodSig[i] == Integer.TYPE) && !(methodSig[i] == anInteger.getClass()) ){
          return false;
        }
      }
      else if (paraRTypes[i] == ParseNode.rType.FLOAT){
        if (!(methodSig[i] == Float.TYPE) && !(methodSig[i] == aFlaot.getClass()) ){
          return false;
        }
      }
      else if (paraRTypes[i] == ParseNode.rType.STRING){
        if (!(methodSig[i] == aString.getClass()) ){
          return false;
        }
      }
    }
    return true;
  }

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

}
