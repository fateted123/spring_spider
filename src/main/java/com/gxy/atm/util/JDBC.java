package com.gxy.atm.util;

import java.lang.reflect.Field;
import java.sql.*;

public class JDBC {
    /**
     * 利用反射、泛型设计通用的查询方法
     * 模仿mybatis的数据映射功能
     */
    public <T> T select(String sql, Class<T> clazz) throws IllegalAccessException, InstantiationException {

        T t = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();

        Connection conn = DbUtil.getConn();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = conn.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                ResultSetMetaData metaData = resultSet.getMetaData(); //得到表结构

                for (int i = 1; i < metaData.getColumnCount(); i++) {

                    String columnName = metaData.getColumnLabel(i);
                    Object cloValue = resultSet.getObject(columnName);
                    System.out.println(String.format("%s:%s", columnName, cloValue));

                    for (Field field :
                            declaredFields) {

                        if (field.getName().equals(columnName)) {
                            field.setAccessible(true);
                            field.set(t, cloValue);
                            break;
                        }
                    }
                }

            }

            return t;

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            DbUtil.closeAll(resultSet, preparedStatement, conn);
        }

        return null;
    }
}
