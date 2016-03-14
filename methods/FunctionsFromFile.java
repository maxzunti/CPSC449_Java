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
  /********Does not work yet************/
  //Problem: cant get correct class types for paraType
  public void executeMethod(String methodName, Object[] parameters){
    try{
/*
      Method[] declaredMethods = funClass.getDeclaredMethods();
          for (Method dmethod : declaredMethods) {
              Class<?>[] test = dmethod.getParameterTypes();
              for (Class t : test)
                System.out.print(t + "\n");

          }
*/
        Class<?>[] paraType = new Class<?>[parameters.length];
        for (int i = 0;i < parameters.length; i++ ) {
          //paraType[i] = parameters[i].getClass();
          //System.out.println("abc" + paraType[i]);
          /*if (parameters[i] instanceof int)
              paraType[i] = Integer.TYPE;
          else if (parameters[i] instanceof float)
              paraType[i] = Float.TYPE;
          else if (parameters[i] instanceof boolean)
              paraType[i] = Boolean.TYPE;
          else if (parameters[i] instanceof byte)
              paraType[i] = Byte.TYPE;
          else if (parameters[i] instanceof char)
              paraType[i] = Char.TYPE;
          else if (parameters[i] instanceof short)
              paraType[i] = Short.TYPE;
          else if (parameters[i] instanceof long)
              paraType[i] = Long.TYPE;
          else if (parameters[i] instanceof double)
              paraType[i] = Double.TYPE;*/
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
