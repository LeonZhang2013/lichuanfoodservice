package com.lichuan.sale.web;

import com.lichuan.sale.model.Product;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("product")
public class ProductController extends BaseController {


    /**
     * 获取商品
     * @param pager
     * @param categoryId //因为 product 数据库中存储的是 attribute的 name 不是 id 所以需要使用 name ,原因二，使用如果使用 id 在模糊搜索时 1 和 10 如果关键字是 1 就会有混淆。
     * @param keyword
     * @param islist
     * @param promotion
     * @return
     */
    @RequestMapping("getProducts")
    public MultiResult<Map<String, Object>> getProducts(Pager<Map<String, Object>> pager, String categoryId, String keyword, Integer islist, Integer promotion) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> productList = productService.getProductList(pager, categoryId, keyword, islist,promotion);
            if(productList.size()>0){
                result.setCode(Code.SUCCESS);
                result.setData(productList);
            }else{
                result.setCode(Code.NO_DATA);
                result.setMessage("已经到底了");
            }
        } catch (Exception e) {
            result.setCode(Code.ERROR);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping("getUnits")
    public SingleResult<Object> getProductUnit() {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            List<Map<String, Object>> productUnit = productService.getUnits();
            result.setCode(Code.SUCCESS);
            result.setData(productUnit);
        } catch (Exception e) {
            result.setCode(Code.ERROR);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping("getCategory")
    public SingleResult<Object> getCategory(Pager<Map<String, Object>> pager,String key,String categoryId,Integer islist) {
        SingleResult<Object> data = new SingleResult<>();
        try {
            List<Map<String, Object>> maps = productService.getCategory(pager,key,categoryId,islist);
            data.setCode(Code.SUCCESS);
            data.setData(maps);
        } catch (Exception e) {
            data.setCode(Code.ERROR);
        }
        return data;
    }

    @RequestMapping("upload")
    public MultiResult<Map<String, Object>> uploadProduct(Product product) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            if (product.getId() != null&&product.getId()>0) {
                productService.updateProduct(product);
            } else {
                productService.addProduct(product);
            }
            result.setCode(Code.SUCCESS);
            result.setMessage("上传成功");
        } catch (Exception e) {
            result.setCode(Code.ERROR);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
