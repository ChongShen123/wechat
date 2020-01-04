package com.xsdkj.wechat.vo;

<<<<<<< HEAD

=======
>>>>>>> 27edae3208a992a05a995390701c4070e0a6af6c
import com.xsdkj.wechat.entity.mood.UserComment;
import com.xsdkj.wechat.entity.mood.UserThumbs;
import lombok.Data;

import java.util.List;

/**
 * @author p
 */
@Data
public class UserMoodVo {
    private Integer id;
    private Integer uid;
    private String content;
    private String file;
    private Byte fileType;
    private Integer commentCount;
    private Long createTimes;
    private List<UserThumbs> userThumbs;
    private List<UserComment> comments;
}
