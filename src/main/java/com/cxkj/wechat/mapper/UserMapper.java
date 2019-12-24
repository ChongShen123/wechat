package com.cxkj.wechat.mapper;

import com.cxkj.wechat.bo.PermissionBo;
import com.cxkj.wechat.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

import com.cxkj.wechat.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author tiankong
 * @date 2019/12/11 11:38
 */

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User getOneByUsername(@Param("username") String username);

    Integer countByEmail(@Param("email") String email);

    List<User> listUserByIds(@Param("ids") Set<Integer> ids);

    List<PermissionBo> listPermissionByUid(@Param("uid") Integer uid);
}
