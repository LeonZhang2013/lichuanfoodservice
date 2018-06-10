package com.lichuan.sale;


import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.dao.DeliverDao;
import com.lichuan.sale.service.ProductService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tester {

    @Autowired
    DeliverDao deliverDao;

    @Autowired
    ProductService productService;

    @Test
    public void contextLoads() {
//
//        try {
//            deliverDao.getStorageInfo(1526480121770);
//        } catch (CustomException e) {
//            e.printStackTrace();
//        }


        //productService.updateProduct();
    }

}



