package com.weblearnex.app.constant;

public enum ReportCustomType {
    SAME_DAY(0),PREVIOUS_DAY(-1),LAST_7_DAY(-7),LAST_15_DAY(-15),LAST_30_DAY(-30);
    private Integer days;
    public Integer getDays()
    {
        return this.days;
    }

    private ReportCustomType(Integer days)
    {
        this.days = days;
    }
    }
