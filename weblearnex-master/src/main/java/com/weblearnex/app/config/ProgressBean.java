package com.weblearnex.app.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProgressBean implements Serializable {
    private int progressPercentage;
    private Boolean uploadCompleted = false;
    private String token;
    private String uploadKey;
}
