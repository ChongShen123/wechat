package com.xsdkj.wechat.bo;

import com.xsdkj.wechat.entity.chat.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/22 18:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionBo extends Permission {
    private List<PermissionBo> children;

    @Override
    public String toString() {
        return "PermissionBo{" +
                "children=" + children +
                ", id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", redirect='" + redirect + '\'' +
                ", type=" + type +
                ", hidden=" + hidden +
                ", status=" + status +
                ", sort=" + sort +
                ", createTimes=" + createTimes +
                '}';
    }
}
