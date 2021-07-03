package com.weblearnex.app.setup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date validity;
    public JwtToken(String token) {
        this.token = token;
    }

    public JwtToken(String token,Date validity) {
        this.token = token;
        this.validity =validity;
    }

    public JwtToken() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }
}
