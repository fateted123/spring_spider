package com.gxy.atm.reflect;

import com.gxy.atm.entity.Movie;
import com.gxy.atm.service.BussService;
import com.gxy.atm.service.HttpClientService;
import com.gxy.atm.util.JDBC;

import java.util.*;

public class Tset2 {
    public static void main(String[] args) {
//
//        List<Integer> list2 = new ArrayList<>();
//
//        List<Integer> list = new ArrayList<Integer>() {{在初始化的时候直接赋值
//            add(100);
//            add(200);
//        }};
//        System.out.println(Arrays.toString(list.toArray()));//遍历数组的另一种方法

//        MyList<Integer> myList = new MyList<>();
//        myList.add(1);
//        myList.add(1);
//        myList.add(12);
//        myList.add(13);
//        myList.add(14);
//        myList.add(12);
//
//        Integer s = myList.get(3);
//        System.out.println(s);

//        MyBeanFactory myBeanFactory = new MyBeanFactory();
//        BussService userService = myBeanFactory.getBean("userService", BussService.class);
        JDBC jdbc = new JDBC();
        try {
            jdbc.select("select * from movie", Movie.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

class MyList<T> {
    private int index;

    private Object[] objects;

    public MyList(int size) {

        objects = new Object[size];
    }

    public MyList() {
        objects = new Object[7];//数组初始化长度
    }

    public void add (T value){
        double g = (index + 1) / Double.valueOf(objects.length);
        if (g >= 0.75){
            objects = Arrays.copyOf(objects,objects.length*2);
        }
        objects[index++] = value;
    }

    public T get(int index){
        if (index > objects.length){
            return null;
        }
        return (T)objects[index];
    }
}

/**
 * 尝试泛型方法
 * 手写spring getBean
 */
class MyBeanFactory {

    Map<String,Object> beans = new HashMap<String,Object>(){
        {
            put("userService", new BussService());
            put("httpClientService", new HttpClientService());
        }
    };

    /**
     * 定义泛型方法
     *
     * @param beanName
     * @param clazz
     * @param <K>
     * @return
     */

    public <K> K getBean(String beanName, Class<K> clazz){

        return (K)beans.get(beanName);
    }

}
