package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.CommonMapper;
import com.lichuan.sale.model.Common;
import com.lichuan.sale.service.CommonService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class CommonServiceImpl extends AbstractService<Common> implements CommonService {
    @Resource
    private CommonMapper commonMapper;

}
