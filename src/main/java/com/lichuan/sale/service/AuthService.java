package com.lichuan.sale.service;

import com.lichuan.sale.model.Permission;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购管理
 *
 * @author lynn
 */
@Component
@Transactional
public class AuthService extends BaseService {

    public static final Permission P_Product = new Permission(100L, "商品管理");
    public static final Permission P_Data = new Permission(200L, "数据统计");
    public static final Permission P_DataLookAll = new Permission(201L, "查看所有统计");
    public static final Permission P_Deliver = new Permission(300L, "发货管理");
    public static final Permission P_Dispatching = new Permission(400L, "配送管理");
    public static final Permission P_Power = new Permission(500L, "权限管理");
    public static final Permission P_User = new Permission(600L, "用户管理");
    public static final Permission P_User_Manger = new Permission(601L, "管理所有用户");
    public static final Permission P_Message = new Permission(700L,"消息管理");
    public static final Permission P_MessageCheck = new Permission(701L,"审批管理");
    public static final Permission P_Order = new Permission(800L,"订单管理");
    public static final Permission P_Order_ALL = new Permission(801L,"管理所有订单");

    public boolean hasPermission(Long role_id,Permission permission) {
        boolean hasPer = false;
        List<Map<String, Object>> permissonList = roleDao.getPermissonById(role_id);
        for (int i=0;i<permissonList.size();i++) {
            String per = permissonList.get(i).get("permission_id").toString();
            if(per.equals(permission.getId().toString())){
                hasPer =true;
                break;
            }
        }
        return hasPer;
    }
}
