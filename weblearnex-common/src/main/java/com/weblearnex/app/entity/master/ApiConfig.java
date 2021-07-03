package com.weblearnex.app.entity.master;

import com.google.gson.Gson;
import com.weblearnex.app.constant.ApiConfigType;
import com.weblearnex.app.constant.EntityType;
import com.weblearnex.app.constant.RequestMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "apiconfig")
public class ApiConfig implements Serializable, Cloneable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "apiconfig_id", updatable=false, nullable=false)
    private Long id;
    private Long clientId;     //dropDown list of active clients
    private Long courierId;    // dropDown list of active courier;
    private Long serviceProviderId;

    @Enumerated(EnumType.STRING)
    private ApiConfigType apiConfigType; //dropDown

    private String apiUrl;
    @Enumerated(EnumType.STRING)

    private EntityType entityType; //dropDown
    @Enumerated(EnumType.STRING)

    private RequestMethod requestMethod; // dropDown
    @Column(length = 1000)
    @Type(type="text")
    private String dataParams;    //optional
    @Column(length = 1500)
    @Type(type="text")
    private String headerParems;  //optional
    private String responseBean;  //optional
    private Boolean isResponseArray = false;
    private String implClass;
    private String responseType; // JSON,XML,SOAP
    private String requestType;  // JSON,XML,SOAP
    @Column(length = 1000)
    @Type(type="text")
    private String apiToken;      //optional
    private String tokenExpiredDate;
    private String extra1;
    private String extra2;
    private String extra3;
    private Integer active = 1;

    @Transient
    private String courierCode;
    @Transient
    private String clientCode;

    @Override
    public Object clone() throws CloneNotSupportedException {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return  gson.fromJson(json, this.getClass());
    }
}
