package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.PermissionMapper;
import com.lichuan.sale.model.Permission;
import com.lichuan.sale.service.PermissionService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class PermissionServiceImpl extends AbstractService<Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

}
