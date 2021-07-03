package com.weblearnex.app.api.bean.orderpush;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NimbusOrderPushRequestOldBean {

    private Consignee consignee;
    private Order order;
    private List<OrderItems> order_items;
    private String pickup_warehouse_id;
    private String rto_warehouse_id;
    private String courier_id;

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
    public static  class Order{
        private String order_number;
        private Double shipping_charges;
        private Double discount;
        private Double cod_charges;
        private String payment_type;
        private Double total;
        private Double package_weight;
        private Double package_length;
        private Double package_height;
        private Double package_breadth;
    }
    @Data
    @NoArgsConstructor
    public static  class OrderItems{
        private String name;
        private String qty;
        private String price;
        private String sku;
    }
}
