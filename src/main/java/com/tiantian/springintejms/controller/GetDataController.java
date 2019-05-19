
package com.tiantian.springintejms.controller;

import com.tiantian.springintejms.mapper.GetDataMapper;
import com.tiantian.springintejms.utils.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: song
 * @Date: 2019/5/20 3:00
 */

@Controller
@RequestMapping("/data")
public class GetDataController {

    @ResponseBody
    @RequestMapping("get")
    public String GetData(){
        SqlSessionFactory factory = DbUtils.obtionSqlSessionFactory();
        SqlSession sqlSession = null;
        try{
            sqlSession = factory.openSession();
            GetDataMapper getDataMapper = sqlSession.getMapper(GetDataMapper.class);
            String data = getDataMapper.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            sqlSession.rollback();
        }
        finally {
            if(sqlSession!=null) sqlSession.close();
        }
        return "test";
    }

}
