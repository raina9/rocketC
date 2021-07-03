package com.weblearnex.app.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class DataTableResult<T> {
    private Page<T> data;
    private int draw;//the NO.of requests
    private int length;
    private long recordsTotal;
    private long recordsFiltered;
}
