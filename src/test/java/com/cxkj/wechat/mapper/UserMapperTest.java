package com.cxkj.wechat.mapper;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.PermissionBo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/22 19:08
 */
public class UserMapperTest {
    private static UserMapper mapper;

    @BeforeClass
    public static void setUpMybatisDatabase() {
        SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(UserMapperTest.class.getClassLoader().getResourceAsStream("mybatisTestConfiguration/UserMapperTestConfiguration.xml"));
        //you can use builder.openSession(false) to not commit to database
        mapper = builder.getConfiguration().getMapper(UserMapper.class, builder.openSession(true));
    }

    @Test
    public void testListPermissionByUid() throws FileNotFoundException {
        List<PermissionBo> permissionList = mapper.listPermissionByUid(7);
        List<PermissionBo> classify = classify(0, permissionList);
        System.out.println(JSONObject.toJSON(classify));
    }

    private List<PermissionBo> classify(Integer pid, List<PermissionBo> permissions) {
        List<PermissionBo> childList = new ArrayList<>();
        permissions.forEach(permissionBo -> {
            if (permissionBo.getPid().equals(pid)) {
                childList.add(permissionBo);
            }
        });
        childList.forEach(permissionBo -> permissionBo.setChildren(classify(permissionBo.getId(), permissions)));
        Collections.sort(childList, order());
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }

    // 根据 sort进行排序
    private Comparator<PermissionBo> order() {
        return Comparator.comparingInt(PermissionBo::getSort);
    }
}
