package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.CategroyMapper;
import com.lichuan.sale.model.Categroy;
import com.lichuan.sale.service.CategroyService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class CategroyServiceImpl extends AbstractService<Categroy> implements CategroyService {
    @Resource
    private CategroyMapper categroyMapper;

}
