package com.lichuan.sale.web;

import com.lichuan.sale.model.Product;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @param status   //是否显示 1 在售，2 已下架 3 废除
     * @param promotion
     * @return
     */
    @RequestMapping("getProducts")
    public MultiResult<Map<String, Object>> getProducts(Pager<Map<String, Object>> pager, String categoryId, String keyword, Integer status, Integer promotion) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> productList = productService.getProductList(pager, categoryId, keyword, status,promotion);
            if(productList.size()>0){
                result.setCode(Code.SUCCESS);
                result.setData(productList);
            }else{
                result.setCode(Code.NO_DATA);
                result.setMessage("已经到底了");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
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
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @RequestMapping("getCategories")
    public SingleResult<Object> getCategories(Integer status) {
        SingleResult<Object> data = new SingleResult<>();
        try {
            List<Map<String, Object>> maps = productService.getCategory(status);
            data.setCode(Code.SUCCESS);
            data.setData(maps);
        } catch (Exception e) {
            data.setMessageOfError(e.getMessage());
        }
        return data;
    }


    @PostMapping("updateCategoryStatus")
    public SingleResult<Object> updateCategoryStatus(Long categoryId, Long status) {
        SingleResult<Object> data = new SingleResult<>();
        try {
            productService.updateCategoryStatus(categoryId,status);
            data.setCode(Code.SUCCESS);
        } catch (Exception e) {
            data.setMessageOfError(e.getMessage());
        }
        return data;
    }

    @PostMapping("addCategory")
    public  SingleResult<Object> addCategory(Long parentId,String  name){
        SingleResult<Object> data = new SingleResult<>();
        try {
            productService.addCategory(parentId,name);
            data.setCode(Code.SUCCESS);
        } catch (Exception e) {
            data.setMessageOfError(e.getMessage());
        }
        return data;
    }


    @RequestMapping("changeStatus")
    public SingleResult<String> changeStatus(String id,String status) {
        SingleResult<String> result = new SingleResult<>();
        try {
            productService.changeStatus(id,status);
            result.setCode(Code.SUCCESS);
            result.setMessage("上传成功");
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @PostMapping("uploadProduct")
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
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }
}
