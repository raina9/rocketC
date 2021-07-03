package com.weblearnex.app.api.bean.orderpush;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NimbusOrderPushRequestBean {
    private String order_number;
    private Double shipping_charges;
    private Double discount;
    private Double cod_charges;
    private String payment_type;
    private String courier_id;
    private Double order_amount;
    private Double package_weight;
    private Double package_length;
    private Double package_breadth;
    private Double package_height;
    private Consignee consignee;
    private String request_auto_pickup;
    private Pickup pickup;
    private List<OrderItems> order_items;

    @Data
    @NoArgsConstructor
    public static  class Consignee{
        private String name;
        private String address;
        private String address_2;
        private String city;
        private String state;
        private String pincode;
        private String phone;
    }
    @Data
    @NoArgsConstructor
    public static  class Pickup{
        private String warehouse_name;
        private String name;
        private String address;
        private String address_2;
        private String city;
        private String state;
        private String pincode;
        private String phone;
    }
    @Data
    @NoArgsConstructor
    public static class OrderItems {
        private String name;
        private String qty;
        private String price;
        private String sku;
    }
}
