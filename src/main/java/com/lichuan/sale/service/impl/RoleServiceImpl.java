package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.RoleMapper;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.service.RoleService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

}
