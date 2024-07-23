package com.anranruozhu.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author anranruozhu
 * @className MockServiceProxy
 * @description Mock服务代理（JDK动态代理）
 * @create 2024/7/23 下午3:06
 **/
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    /**
     *调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //根据方法的返回值类型，生成特定的默认值对象
        Class<?> methodReturnType=method.getReturnType();
        log.info("mock invoke {}",method.getName());
        return getDefaultValue(methodReturnType);
    }

    /**
     *
     * 生成指定类型的默认值对象
     *
     * @param type
     * @return
     */
    private Object getDefaultValue(Class<?> type) {
        //基本类型
        //boolean，short，int，long，float，double，byte，char
        if(type.isPrimitive()){
            if (type == boolean.class) {
                return false;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            }else if (type == float.class) {
                return 0.0f;
            }else if (type == double.class) {
                return 0.0;
            }else if (type == char.class) {
                return '\0';
            }else if (type == byte.class) {
                return (byte) 0;
            }
        }
        //对象类型
        return null;
    }
}
