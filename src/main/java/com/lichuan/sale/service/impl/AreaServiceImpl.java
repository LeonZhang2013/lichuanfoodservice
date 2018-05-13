package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.AreaMapper;
import com.lichuan.sale.model.Area;
import com.lichuan.sale.service.AreaService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class AreaServiceImpl extends AbstractService<Area> implements AreaService {
    @Resource
    private AreaMapper areaMapper;

}
