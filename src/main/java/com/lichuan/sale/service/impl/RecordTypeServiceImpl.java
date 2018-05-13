package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.RecordTypeMapper;
import com.lichuan.sale.model.RecordType;
import com.lichuan.sale.service.RecordTypeService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class RecordTypeServiceImpl extends AbstractService<RecordType> implements RecordTypeService {
    @Resource
    private RecordTypeMapper recordTypeMapper;

}
