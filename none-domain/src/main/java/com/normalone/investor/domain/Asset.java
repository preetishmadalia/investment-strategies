package com.normalone.investor.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.BiFunction;

@Data
@Builder
public class Asset {



    private String ticker;

    private AssetType assetType;

    private double totalContracts = 0.0;

    private double totalAmountInvested = 0.0;

    private List<TimeSeriesBar> timeSeriesBars;

    public void addContract(double amt) {
        this.totalContracts += amt;
    }

    public void addTotalInvested(double amt) {
        this.totalAmountInvested += amt;
    }

    public double currentPrice () {
        return this.timeSeriesBars.get(this.timeSeriesBars.size() - 1).getClosePrice().doubleValue();
    }

    public double getCurrentInvestmentValue () {
        return this.totalContracts * currentPrice();
    }

    public double mathFunction(Double a, Double b, BiFunction<Double, Double, Double> function) {
        return function.apply(a, b);
    }
}
