package com.normalone.investor.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InvestmentParam {
    /**
     * Amount for invest per period.
     */
    protected double capitalPerPeriod;

    /**
     * Investment period.
     */
    protected int lengthofInvestment;

    private Map<AssetType, Double> assetWeight;
}
