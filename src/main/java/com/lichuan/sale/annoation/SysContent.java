package com.lichuan.sale.annoation;

import com.lichuan.sale.model.User;

public class SysContent {
    private static ThreadLocal<User> requestLocal = new ThreadLocal();

    public static User getUser() {
        return requestLocal.get();
    }

    public static void setUser(User user) {
        requestLocal.set(user);
    }

}
