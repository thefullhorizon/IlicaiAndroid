package com.huoqiu.framework.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if
     * cannot be determined
     */
    public static Type getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Type getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return params[index];
    }

    public static <T> T getFieldValue(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        T value = (T) object.getClass().getField(fieldName).get(object);
        return value;
    }

    public static <T> T getStaticFieldValue(Class clazz, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        T value = (T) clazz.getField(fieldName).get(null);
        return value;
    }
}
