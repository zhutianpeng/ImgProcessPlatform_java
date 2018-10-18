import com.tiantian.springintejms.listener.ConsumerMessageListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by AndrewKing on 8/3/2018.
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:applicationContext.xml"}) //加载配置文件
public class TestJedisPoolConfig {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ConsumerMessageListener consumerMessageListener;
//    @Autowired
//    private JedisPoolConfig jedisPoolConfig ;
//    @Autowired
//    private JedisPool jedisPool;
    @Test
    public void test1() {
        System.out.print(consumerMessageListener.toString());
    }
}
