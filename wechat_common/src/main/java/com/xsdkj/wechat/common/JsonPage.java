package com.xsdkj.wechat.common;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/11/17 15:36
 */
@Data
public class JsonPage<T> {
    protected Integer pageNum;
    protected Integer pageSize;
    protected Integer totalPage;
    protected Long total;
    protected List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> JsonPage<T> restPage(List<T> list) {
        JsonPage<T> result = new JsonPage<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotalPage(pageInfo.getPages());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    /**
     * 将SpringData分页后的list转换为分页信息
     */
    public static <T> JsonPage<T> restPage(Page<T> pageInfo) {
        JsonPage<T> result = new JsonPage<>();
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotalPage(pageInfo.getTotalPages());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }
}
