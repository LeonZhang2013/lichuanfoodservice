package com.lichuan.sale.wx;


import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.web.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/product")
public class WxProductController extends BaseController{

    /**
     * 获取商品 首页滚动商品。
     */
    @RequestMapping("getBannerList")
    public MultiResult<Map<String, Object>> getBannerList() {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> productList = productService.getBannerList();
                result.setCode(Code.SUCCESS);
                result.setData(productList);
        } catch (Exception e) {
            result.setCode(Code.ERROR);
            result.setMessage(e.getMessage());
        }
        return result;
    }




}
