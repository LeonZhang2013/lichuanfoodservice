package com.lichuan.sale.tools;


import com.lichuan.sale.model.Vip;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * <p>
 * 确的浮点数运算，包括加减乘除和四舍五入。
 */

public final class Arith {

    //默认除法运算精度   
    private static final int DEF_DIV_SCALE = 4;
    //单价精确位
    private static final int SALE_SCALE = 3;
    //单件精确位
    private static final int PIECE_SCALE = 2;

    //这个类不能实例化   
    private Arith() {

    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static final double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }


    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static final double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static final double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static final double mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后4位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static final double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static final double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if (v2 == 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static final double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将科学计数法转换为double类型
     *
     * @param num
     * @return
     */
    public static final BigDecimal convertDouble(double num) {
        BigDecimal big = new BigDecimal(num);
        return big.setScale(10, 2);
    }

    /**
     * 判断两个浮点数是否相等
     * 设定一个精度，如果两个数相减的绝对值相等，说明相等
     *
     * @param d1
     * @param d2
     * @return
     */
    public static final boolean equals(double d1, double d2) {
        return Math.abs(sub(d1, d2)) <= 0.0000001;
    }


    /**
     * 计算运费 和 商品总价
     *
     * @param distanceStr
     * @param cart
     * @return
     */
    public static Map<String, Object> calculationCharge(String distanceStr, List<Map<String, Object>> cart, Vip vip) {
        BigDecimal distance = new BigDecimal(distanceStr);
        BigDecimal totalGoodsPrice = new BigDecimal(0);
        BigDecimal totalFreight = new BigDecimal(0);
        for (int i = 0; i < cart.size(); i++) {
            BigDecimal price = new BigDecimal(cart.get(i).get("price").toString());
            BigDecimal num = new BigDecimal(cart.get(i).get("num").toString());
            BigDecimal freight = new BigDecimal(cart.get(i).get("freight").toString());
            BigDecimal sub_freight = freight.multiply(num).multiply(distance);
            BigDecimal sub_price = price.multiply(num);
            cart.get(i).put("sub_freight", sub_freight);
            cart.get(i).put("sub_price", sub_price);
            totalGoodsPrice = totalGoodsPrice.add(sub_price);
            totalFreight = totalFreight.add(sub_freight);
        }
        Map<String, Object> charge = new HashMap<>();
        BigDecimal divide = new BigDecimal(10 - vip.getDiscount()).divide(new BigDecimal(10));
        BigDecimal disc = totalGoodsPrice.multiply(divide).setScale(PIECE_SCALE, BigDecimal.ROUND_HALF_UP);
        charge.put("totalGoodsPrice", totalGoodsPrice);
        charge.put("totalFreight", totalFreight);
        charge.put("discountPrice", disc);
        charge.put("totalPrice", totalFreight.add(totalGoodsPrice).subtract(disc));
        return charge;
    }


    public static Map<String, Object> calculationCharge(List<Map<String, Object>> goods, Vip vip) {
        BigDecimal totalGoodsPrice = new BigDecimal(0);
        BigDecimal totalFreight = new BigDecimal(0);
        for (int i = 0; i < goods.size(); i++) {
            BigDecimal sub_price = new BigDecimal(goods.get(i).get("sub_price").toString());
            BigDecimal sub_freight = new BigDecimal(goods.get(i).get("sub_freight").toString());
            totalGoodsPrice = totalGoodsPrice.add(sub_price);
            totalFreight = totalFreight.add(sub_freight);
        }
        Map<String, Object> charge = new HashMap<>();
        BigDecimal divide = new BigDecimal(10 - vip.getDiscount()).divide(new BigDecimal(10));
        BigDecimal disc = totalGoodsPrice.multiply(divide).setScale(PIECE_SCALE, BigDecimal.ROUND_HALF_UP);
        charge.put("totalGoodsPrice", totalGoodsPrice);
        charge.put("totalFreight", totalFreight);
        charge.put("discountPrice", disc);
        charge.put("totalPrice", totalFreight.add(totalGoodsPrice).subtract(disc));
        return charge;
    }
}