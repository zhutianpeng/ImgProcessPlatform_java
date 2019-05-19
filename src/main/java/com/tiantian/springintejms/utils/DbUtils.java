package com.tiantian.springintejms.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: song
 * @Date: 2019/5/20 3:00
 */

public class DbUtils {

    private static volatile SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory obtionSqlSessionFactory()
    {
        if( sqlSessionFactory == null )
        {
            synchronized (DbUtils.class)
            {
                if(sqlSessionFactory == null )
                {
                    sqlSessionFactory = getSqlSessionFactory();
                }
            }
        }
        return sqlSessionFactory;
    }

    private static SqlSessionFactory getSqlSessionFactory()
    {
        String resource = "mybatis-config.xml";
        SqlSessionFactory sqlSessionFactory = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build( inputStream );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

}
