package com.weblearnex.app.config;

public interface AppProperty {

    public static final Integer ACTIVE = 1;
    public static final Integer IN_ACTIVE = 0;

    public static final String BULK_UPLOAD_FILE_PATH = "bulk.upload.file.path";
    public static final String THREE_PL_POD_UPLOAD_FILE_PATH = "three.pl.pod.upload.file.path";
    public static final String DOC_POD_UPLOAD_FILE_PATH = "doc.upload.file.path";
    public static final String CLIENT_REMITTANCE_DEPOSITED_SLIP_PATH = "client.remittance.deposited.slip.path";

    public static final String NEJK_ZOME_STATES_CODE = "nejk.zone.states.code";
    public static final String METRO_ZOME_CITY_CODE = "metro.zone.city.code";
    public static final String SELF_COURIER_CODE = "self.courier.code";

    public static final String PACKET_FLOW_NAME = "packet.flow.name";
    public static final String UD_ORDER_RECEIVED = "ud.order.received";
    public static final String UD_ORDER_PROCESS = "ud.order.process";
    public static final String UD_ORDER_ASSIGNED = "ud.order.assigned";
    public static final String UD_ORDER_SCANNED = "ud.order.scanned";
    public static final String UD_IN_TRANSIT = "ud.in.transit";
    public static final String UD_OUT_FOR_DELIVERY = "ud.out.for.delivery";
    public static final String UD_DELIVERED = "ud.delivered";
    public static final String UD_DELIVERY_ATTEMPTED = "ud.delivery.attempted";
    public static final String RT_RETURNED = "rt.returned";
    public static final String RT_IN_TRANSIT = "rt.in.transit";
    public static final String RT_OUT_FOR_DELIVERY = "rt.out.for.delivery";
    public static final String RT_DELIVERED = "rt.delivered";
    public static final String RT_DELIVERY_ATTEMPTED = "rt.delivery.attempted";
    public static final String UD_CANCELLED = "ud.cancelled";

    public static final String REMITTANCE_GENERATED = "remittance.generated";
    public static final String REMITTANCE_COLSED = "remittance.closed";

    public static final String ORDER_PRINT_LABEL_PATH = "order.print.label.path";

    public static final String RAZORPAY_API_KEY = "razorpay.api.key";
    public static final String RAZORPAY_API_SECRET = "razorpay.api.secret";
    public static final String RAZORPAY_API_TOKEN = "razorpay.api.token";
    public static final String RAZORPAY_ORDER_API_URL = "razorpay.order.api.url";
    public static final String RAZORPAY_FETCH_PAYMENT_URL = "razorpay.fetch.payment.url";
    public static final String RAZORPAY_GET_WAY_LOGO_URL = "razorpay.get.way.logo.url";

    public static final String COURIER_REMITTANCE_DEPOSITED_SLIP_PATH = "courier.remittance.deposited.slip.path";
    public static final String TRACK_ORDER_CURRENT_STATUS_NOT_IN = "track.order.current.status.not.in";
    public static final String REPLACE_PRODUCT_NAME = "replace.product.name";
}
