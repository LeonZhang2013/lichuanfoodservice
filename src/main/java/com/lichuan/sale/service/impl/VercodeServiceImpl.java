package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.VercodeMapper;
import com.lichuan.sale.model.Vercode;
import com.lichuan.sale.service.VercodeService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class VercodeServiceImpl extends AbstractService<Vercode> implements VercodeService {
    @Resource
    private VercodeMapper vercodeMapper;

}
