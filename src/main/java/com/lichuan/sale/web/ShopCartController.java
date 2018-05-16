package com.lichuan.sale.web;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.tools.Tools;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cart")
public class ShopCartController extends BaseController{


    @PostMapping("getCartTotalNum")
    public SingleResult<Integer> getCartTotalNum(Long userId) {
        SingleResult<Integer> result = new SingleResult<>();
        try {
            if (null != userId) {
                Integer cartTotalNum = shopCartService.getCartTotalNum(userId);
                result.setCode(Code.SUCCESS);
                result.setData(cartTotalNum);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @PostMapping("addShopCart")
    public SingleResult<Long> addShopcart(Long user_id, Long product_id, Integer num) {
        SingleResult<Long> result = new SingleResult<>();
        try {
            if (null != user_id && null != product_id && null != num) {
                int effect = shopCartService.addShopCart(user_id, product_id, num);
                if (effect == 0) throw new CustomException("添加失败");
                result.setCode(Code.SUCCESS);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }


    @RequestMapping(value = "getShopCartList", method = RequestMethod.GET)
    public MultiResult<Map<String, Object>> getShopCartList(Long userId) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> data = shopCartService.getShopCartList(userId);
            if (null != data && data.size() > 0) {
                result.setCode(Code.SUCCESS);
                result.setData(data);
            } else {
                result.setCode(Code.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping(value = "setShopCart", method = RequestMethod.POST)
    public SingleResult<String> setShopcart(Long cartId, Long userId, Integer num) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (null != cartId && null != userId && null != num) {
                 shopCartService.setShopCart(cartId, userId, num);
                result.setCode(Code.SUCCESS);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            result.setCode(Code.ERROR);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "deleteShopCart", method = RequestMethod.POST)
    public SingleResult<String> deleteShopCart(Long cartId, Long userId) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (null != cartId && null != userId){
               shopCartService.deleteShopCart(cartId, userId);
               result.setCode(Code.SUCCESS);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping(value = "batchDeleteShopCart", method = RequestMethod.POST)
    public SingleResult<String> batchDeleteShopcart(String ids, Long userId) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (StringUtils.isNotBlank(ids) && null != userId) {
                shopCartService.batchDeleteShopCart(ids, userId);
                result.setCode(Code.SUCCESS);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping(value = "clearShopCart", method = RequestMethod.POST)
    public SingleResult<String> clearShopcart(Long userId) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (null != userId) {
                shopCartService.clearShopCart(userId);
                result.setCode(Code.SUCCESS);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping(value = "getShopCartByIdList", method = RequestMethod.GET)
    public MultiResult<Map<String, Object>> getShopcartByIdList(String ids, Long userId) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            if (StringUtils.isNotBlank(ids) && null != userId) {
                shopCartService.getShopCartByIdList(StringUtils.convertString2List(ids, ","), userId);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping(value = "batchCommitCart", method = RequestMethod.POST)
    public MultiResult<Map<String, Object>> batchCommitCart(String user_id, String carts) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            shopCartService.batchSaveCart(user_id, carts);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setCode(Code.ERROR);
            result.setMessage(e.getMessage());
        }
        return result;
    }


}
