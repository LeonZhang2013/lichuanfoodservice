package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.VersionMapper;
import com.lichuan.sale.model.Version;
import com.lichuan.sale.service.VersionService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class VersionServiceImpl extends AbstractService<Version> implements VersionService {
    @Resource
    private VersionMapper versionMapper;

}
