package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.RolePermissionMapper;
import com.lichuan.sale.model.RolePermission;
import com.lichuan.sale.service.RolePermissionService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class RolePermissionServiceImpl extends AbstractService<RolePermission> implements RolePermissionService {
    @Resource
    private RolePermissionMapper rolePermissionMapper;

}
