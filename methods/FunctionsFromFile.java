package methods;

import java.lang.Integer;
import java.lang.reflect.*;

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
  /********Does not work************/
  //Problem: cant get correct class types for paraType
  public void executeMethod(String methodName, Object[] parameters){
    try{
        Class<?>[] paraType = new Class<?>[parameters.length];
        for (int i = 0;i < parameters.length; i++ ) {
          System.out.println(parameters[i]);
          System.out.println(parameters[i].getClass());
          System.out.println(parameters[i].getClass().getName());
          paraType[i] = parameters[i].getClass();
        }
        Method oneMethod = funClass.getMethod(methodName, paraType);

        System.out.println("Method is: " + oneMethod);
        System.out.println("DOES METHOD WORK " + oneMethod.invoke(funClassObj, parameters));
    }catch (IllegalAccessException e) {
          e.printStackTrace();
    }catch (NoSuchMethodException e) {
        e.printStackTrace();
    }catch (InvocationTargetException e) {
        e.printStackTrace();
    }/*catch (ClassNotFoundException e) {
        e.printStackTrace();
    }*//*catch (NoSuchFieldException e) {
        e.printStackTrace();
    }*/
  }

}
