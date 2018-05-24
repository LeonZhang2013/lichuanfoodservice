package constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单状态
 * 待付款、待核算、待发货、已发货、已收货、已完成
 *
 * @author 李熠
 */
public enum OrderStatus {

    /**
     * 待付款
     */
    WAIT_PAY(0, "待付款", "待付款"),

    /**
     * 贷发货
     */
    WAIT_RECEIVE(1, "待收货", "待收货"),
    /**
     *
     */
    COMPLETE(3, "已完成", "已完成"),
    /**
     * 已取消
     */
    CANCEL(-1, "已取消", "已取消");

    private int status;

    private String message;

    private String btn;

    private OrderStatus(int status, String message, String btn) {
        this.status = status;
        this.message = message;
        this.btn = btn;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getBtn() {
        return btn;
    }

    public static OrderStatus getOrderStatus(int status) {
        OrderStatus orderStatus = OrderStatus.WAIT_PAY;
        OrderStatus orderStatuses[] = OrderStatus.values();
        for (OrderStatus item : orderStatuses) {
            if (status == item.status) {
                return item;
            }
        }
        return orderStatus;
    }

    public static List<Map<String, Object>> getOrderStatus() {
        List<Map<String, Object>> list = new ArrayList<>();
        OrderStatus orderStatuses[] = OrderStatus.values();
        for (OrderStatus orderStatus : orderStatuses) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", orderStatus.getStatus());
            map.put("name", orderStatus.getMessage());
            list.add(map);
        }
        return list;
    }

    public boolean cancel() {
        switch (this) {
            case WAIT_PAY:
                return true;
            default:
                return false;
        }
    }

    public OrderStatus next() {
        switch (this) {
            case WAIT_PAY:
                return WAIT_RECEIVE;
            case WAIT_RECEIVE:
                return COMPLETE;
            default:
                return CANCEL;
        }
    }

}
