import cn.hutool.core.util.RandomUtil;
import com.xsdkj.wechat.WechatApplication;
import com.xsdkj.wechat.entity.other.NicknameName;
import com.xsdkj.wechat.entity.other.NicknameSurname;
import com.xsdkj.wechat.mapper.NicknameNameMapper;
import com.xsdkj.wechat.mapper.NicknameSurnameMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/31 17:43
 */
@SpringBootTest(classes = WechatApplication.class)
class TestCase2 {
    @Resource
    private NicknameNameMapper nicknameNameMapper;
    @Resource
    private NicknameSurnameMapper nicknameSurnameMapper;

    @Test
    void test1() {
        List<NicknameName> nicknameNames = nicknameNameMapper.getByAll(null);
        List<NicknameSurname> nicknameSurnames = nicknameSurnameMapper.getByAll(null);
        for (int i = 0; i < 10000; i++) {
            NicknameName nicknameName = RandomUtil.randomEle(nicknameNames);
            NicknameSurname nicknameSurname = RandomUtil.randomEle(nicknameSurnames);
            System.out.println(nicknameSurname.getSurname() + nicknameName.getSurname());
        }
    }
}
