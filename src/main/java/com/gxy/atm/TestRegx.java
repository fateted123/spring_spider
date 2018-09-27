package com.gxy.atm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegx {
    public static void main(String[] args) {

        String value = "tttt123abcbbbbb";
        String regx = "([0-9]+)abc";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(value);

        if(matcher.find()){//判断是否能匹配到，一般用于表单验证

            System.out.println("匹配内容："+matcher.group());//匹配整个正则
            System.out.println("匹配内容："+matcher.group(1));//匹配正则中的第一组
            //获取abc前面的直接数字


            String s = matcher.replaceAll("---");//正则替换
            System.out.println(s);//新字符串
            System.out.println(value);//原始字符串
        }

    }
}
