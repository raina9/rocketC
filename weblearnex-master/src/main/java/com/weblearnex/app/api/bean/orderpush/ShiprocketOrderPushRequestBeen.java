package com.weblearnex.app.api.bean.orderpush;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ShiprocketOrderPushRequestBeen {
    private String request_pickup;
    private String generate_manifest;
    private String courier_id;
    private String order_id;
    private String order_date;
    private String channel_id;
    private String billing_customer_name;
    private String billing_last_name;
    private String billing_address;
    private String billing_city;
    private String billing_state;
    private String billing_country;
    private String billing_pincode;
    private String billing_email;
    private String billing_phone;
    private String billing_alternate_phone;
    private String shipping_is_billing;
    private String shipping_customer_name;
    private String shipping_address;
    private String shipping_address_2;
    private String shipping_city;
    private String shipping_state;
    private String shipping_country;
    private String shipping_pincode;
    private String shipping_email;
    private String shipping_phone;
    private String payment_method;
    private String sub_total;
    private String weight;
    private String length;
    private String breadth;
    private String height;
    private String pickup_location;

    private List<OrderItems> order_items;

    @Data
    @NoArgsConstructor
    public static class OrderItems{
        private String name;
        private String sku;
        private String units;
        private String selling_price; // Product price
    }

}
