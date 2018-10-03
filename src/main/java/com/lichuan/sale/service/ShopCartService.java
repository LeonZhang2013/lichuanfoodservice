package com.lichuan.sale.service;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.ShopCart;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ShopCartService extends BaseService {


    /**
     * 获得购物车商品总量
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public Integer getCartTotalNum(Long userId) throws Exception {
        return shopCartDao.getCartTotalNum(userId);
    }

    public int addShopCart(Long user_id, Long product_id, Integer num) throws Exception {
        return shopCartDao.addShopCart(user_id, product_id, num);
    }

    /**
     * 获得用户的购物车列表
     * 图片、标题、单价、数量、合计
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getWxShopCartList(Long userId) {
        return shopCartDao.getWxShopCartList(userId);
    }

    /**
     * 获得用户的购物车列表
     * 图片、标题、单价、数量、合计
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getShopCartList(Long userId) {
        return shopCartDao.getUserCart(userId);
    }

    /**
     *  购物车其中一个商品数量加减数量
     *
     * @param cartId
     * @return
     * @throws Exception
     */
    public boolean setShopCart(Long cartId, Long userId, Integer num) throws Exception {
        int effect = shopCartDao.setShopCart(cartId, userId, num);
        if (effect == 0) throw new CustomException("操作失败");
        return effect > 0;
    }

    /**
     * 删除购物车
     *
     * @param cartId
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean deleteShopCart(Long cartId, Long userId) throws Exception {
        int effect = shopCartDao.deleteShopCart(cartId, userId);
        if (effect == 0) throw new CustomException("删除失败");
        return effect > 0;
    }

    /**
     * 批量删除购物车
     *
     * @param idList
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean batchDeleteShopCart(String idList, Long userId) throws Exception {
        int effect = shopCartDao.batchDeleteShopCart(idList, userId);
        if (effect == 0) throw new CustomException("删除失败");
        return effect > 0;
    }


    /**
     * 清空购物车
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean clearShopCart(Long userId) throws Exception {
        int effect = shopCartDao.clearShopCart(userId);
        if (effect == 0) throw new CustomException("清空失败");
        return effect > 0;
    }


    /**
     * 获得用户选择的购物车列表
     * 图片、标题、单价、数量、合计
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public void getShopCartByIdList(List<Long> idList, Long userId) throws Exception {
    }

    public void batchSaveCart(String user_id, String carts) {
    }


    @Transactional
    public void saveCartList(List<ShopCart> arras, Long user_id) throws CustomException {
        shopCartDao.clearShopCart(user_id);
        String sql = "INSERT INTO `shop_cart`(user_id,`product_id`,`num`) values(?,?,?)";
        int[] ints = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ShopCart item = arras.get(i);
                ps.setString(1, user_id.toString());
                ps.setString(2, item.getProduct_id().toString());
                ps.setString(3, item.getNum().toString());
            }

            public int getBatchSize() {
                return arras.size();
            }
        });
//        //清空购物车
//        String sqlDel = "delete from shop_cart where user_id = ?";
//        int effect = jdbcTemplate.update(sqlDel, user_id);
//        if (effect == 0) throw new CustomException("更是购物车失败");
    }


}
