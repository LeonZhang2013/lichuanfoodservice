package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.PayInfoMapper;
import com.lichuan.sale.model.PayInfo;
import com.lichuan.sale.service.PayInfoService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class PayInfoServiceImpl extends AbstractService<PayInfo> implements PayInfoService {
    @Resource
    private PayInfoMapper payInfoMapper;

}
