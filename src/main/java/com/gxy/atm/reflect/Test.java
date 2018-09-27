package com.gxy.atm.reflect;

import com.gxy.atm.entity.Movie;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws Exception {
        //反射重要资源就是  字节码
        //获得到字节码以后可以通过反射机制获取目标字节码属性、行为
        //还可以执行目标方法

        //获取字节码的三种方式
        Class clazz1 = Movie.class;
        Class clazz2 = Class.forName("com.gxy.atm.entity.Movie");//常用语spring中

        Movie movie = new Movie();
        Class clazz3 = movie.getClass();

//        System.out.println(clazz2.getName());
//        System.out.println(clazz2.getSimpleName());

        /**
         * getFields()只能获取public的字段，包括父类的。
         * 而getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
         */

//        Field[] fields = clazz1.getDeclaredFields();
//        for (Field f :
//             fields) {
//            System.out.println(String.format("%s,%s,%s", Modifier.toString(f.getModifiers()),f.getType().getSimpleName(),f.getName()));
//        }

//        Method[] methods = clazz1.getDeclaredMethods();
//        for (Method m:
//             methods) {
//            System.out.println("................"+m.getName());
//            System.out.println(",,,,,,,"+Modifier.toString(m.getModifiers()));
//            System.out.println("111"+m.getParameterTypes());
//
//        }
        Object obj = clazz1.newInstance();

        Field moviename = clazz1.getDeclaredField("moviename");
        moviename.setAccessible(true);
        moviename.set(obj,"sccc");


//        Method setMoviename = clazz1.getDeclaredMethod("setMoviename", String.class);

//        setMoviename.invoke(obj,"卡璐璐");

        Movie movie1 = (Movie) obj;
        System.out.println(movie1.getMoviename());


    }
}
