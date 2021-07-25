package com.normalone.investor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic time series bar.
 */
@Data
@AllArgsConstructor
public class TimeSeriesBar {
    private Long openTime;
    private Long endTime;
    private Float openPrice;
    private Float closePrice;
    private Float highPrice;
    private Float lowPrice;
    private String volume;
}
