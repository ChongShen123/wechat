package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.dto.UpdateSignAwardDto;
import com.xsdkj.wechat.mapper.SignAwardMapper;
import com.xsdkj.wechat.service.SignAwardService;
import com.xsdkj.wechat.vo.SignAwardVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/14 15:01
 */
@Service
public class SignAwardServiceImpl implements SignAwardService {
    @Resource
    private SignAwardMapper signAwardMapper;

    @Override
    public List<SignAwardVo> listAll() {
        return signAwardMapper.listAllForSignAwardVo();
    }

    @Override
    public void updateSignAward(UpdateSignAwardDto updateSignAwardDto) {
        signAwardMapper.updateSignAward(updateSignAwardDto);
    }
}
