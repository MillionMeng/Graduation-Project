import com.arvin.dao.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/5/10.
 */
@ContextConfiguration(locations = {"classpath:../resources/applicationContext.xml"})
public class Test extends AbstractJUnit4SpringContextTests {

    private Logger logger = LoggerFactory.getLogger(Test.class);

    @Autowired
    private CommentMapper commentMapper;

    @org.junit.Test
    public void test(){
        try {
            List<Map<String, String>> list = commentMapper.selectList(1);
            for(Map<String,String> list1 : list){
                for(String k : list1.keySet()){
                    System.out.println(k + ":" + list1.get(k));
                }
            }
        }catch (RuntimeException e){
            logger.error("查询出现异常" ,e);
        }

    }

}
