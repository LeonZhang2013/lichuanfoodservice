package com.lichuan.sale.constant;

import com.lichuan.sale.model.Role;

import java.util.HashMap;
import java.util.Map;

public class LcConstant {

    public static final Object USER_ROLE_VERIFY_USER = 0;
    public static final int REFUND_ENABLE = 10;


    private static Map<String, Role> roleMap = new HashMap<>();

    public static Map<String, Role> getRoleMap() {
        return roleMap;
    }


}
