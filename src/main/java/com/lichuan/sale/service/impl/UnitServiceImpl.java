package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.UnitMapper;
import com.lichuan.sale.model.Unit;
import com.lichuan.sale.service.UnitService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class UnitServiceImpl extends AbstractService<Unit> implements UnitService {
    @Resource
    private UnitMapper unitMapper;

}
