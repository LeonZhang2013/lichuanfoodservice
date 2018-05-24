package com.lichuan.sale.dao;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.SingleResult;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ShopCartDao extends BaseDao {


    public int getCartTotalNum(Long userId) {
        String sql = "SELECT SUM(num) num FROM shop_cart WHERE user_id = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, userId);
        if(map.size()>0){
            return Integer.parseInt(map.get("num")+"");
        }
        return 0;
    }

    public int addShopCart(Long user_id, Long product_id, Integer num) throws CustomException {
        String sql = "select * from shop_cart where user_id = ? and product_id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, user_id, product_id);
        int effect = 0;
        if (null != maps && maps.size() > 0) {
            Map<String, Object> map = maps.get(0);
            num += Integer.parseInt(map.get("num").toString());
            sql = "update shop_cart set num = ? where id = ?";
            effect = jdbcTemplate.update(sql, num, map.get("id"));
        } else {
            sql = "insert into shop_cart (user_id,product_id,num) VALUES (?,?,?)";
            effect = jdbcTemplate.update(sql, user_id, product_id, num);
        }
        if(effect==0) throw new CustomException("添加失败");
        return num;
    }

    public List<Map<String, Object>> getUserCart(Long userId) {
        String sql = "select p.*,s.product_id,s.id cart_id,s.num from shop_cart s,product p where s.product_id = p.id and s.user_id = ?";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, userId);
        for (int i = 0; i < data.size(); i++) {//由于 我使用 p.* 所以我需要把 p.id 替换成 cart_id.
            Object cart_id = data.get(i).get("cart_id");
            data.get(i).put("id", cart_id);
        }
        return data;
    }

    public List<Map<String, Object>> getWxShopCartList(Long userId) {
        String sql = "select p.*,s.num from shop_cart s,product p where s.product_id = p.id and s.user_id = ?";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, userId);
        return data;
    }



    public int setShopCart(Long cartId, Long userId, Integer num) {
        String sql = "update shop_cart set num = num+? where id = ? and user_id = ?";
        return jdbcTemplate.update(sql, num, cartId, userId);
    }

    public int deleteShopCart(Long cartId, Long userId) {
        String sql = "delete from shop_cart where id = ? and user_id = ?";
        int effect = jdbcTemplate.update(sql,cartId,userId);
        return effect;
    }

    public int batchDeleteShopCart(String idList, Long userId) {
        String sql = "delete from shop_cart where id in ? and user_id = ?";
        int effect = jdbcTemplate.update(sql,"("+idList+")",userId);
        return effect;
    }

    public int clearShopCart(Long userId) {
        String sql = "delete from shop_cart where user_id = ?";
        int effect = jdbcTemplate.update(sql,userId);
        return effect;
    }



}
