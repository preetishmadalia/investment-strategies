package com.normalone.investor.domain;

import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Data
@Builder
public class InvestmentReport {
    private Collection<Asset> assets;

    private BiFunction<Double, Double, Double> percentageReturnsFunc() {
        return (a, b) -> (a - b) * 100 / b;
    }

    public double totalAmountInvested() {
        return this.assets.stream().map(Asset::getTotalAmountInvested).reduce((a, b) -> a + b).orElse(0.0);
    }

    public double totalCurrentValue() {
        return this.assets.stream().map(Asset::getCurrentInvestmentValue).reduce((a, b) -> a + b).orElse(0.0);
    }

    public double percentReturns() {
        return percentageReturnsFunc().apply(totalCurrentValue(), totalAmountInvested());
    }

    public double percentReturnsAsset(final String assetName) {
        return this.assets.stream().filter(asset -> asset.getTicker().equals(assetName))
                                   .map(asset -> asset.mathFunction(asset.getCurrentInvestmentValue(), asset.getTotalAmountInvested(), percentageReturnsFunc()))
                                   .findAny().orElse(0.0);
    }

    public Set<String> assetsWithNegativeReturns() {
        return this.assets.stream().filter(asset -> asset.mathFunction(asset.getCurrentInvestmentValue(), asset.getTotalAmountInvested(), percentageReturnsFunc()) < 0.0).map(Asset::getTicker).collect(Collectors.toSet());
    }

    public Set<String> worstAssetsByPercentageReturns() {

        return this.assets.stream()
                .sorted(Comparator.comparing(asset -> asset.mathFunction(asset.getCurrentInvestmentValue(), asset.getTotalAmountInvested(), percentageReturnsFunc())))
                .limit(2).map(asset -> asset.getTicker() + "["+ percentReturnsAsset(asset.getTicker()) + "]").collect(Collectors.toSet());
    }

    public Set<String> bestAssetsByPercentageReturns() {
        if(assets.size() < 3) return new HashSet<>();
        return this.assets.stream()
                .sorted(Comparator.comparing(asset -> asset.mathFunction(asset.getCurrentInvestmentValue(), asset.getTotalAmountInvested(), percentageReturnsFunc())))
                .skip(this.assets.size() - 2).map(asset -> asset.getTicker() + "["+ percentReturnsAsset(asset.getTicker()) + "]").collect(Collectors.toSet());
    }

    public String portfolioDistribution() {
        StringBuilder builder = new StringBuilder();
        builder.append("Num. of assets : ").append(numOfAssets()).append(", ");
        builder.append("Stocks : ").append(numOfAssets(AssetType.STOCK)).append(", ");
        builder.append("ETF : ").append(numOfAssets(AssetType.ETF)).append(", ");
        builder.append("Commodity : ").append(numOfAssets(AssetType.COMMODITY)).append(", ");
        builder.append("Crypto : ").append(numOfAssets(AssetType.CRYPTO));
        return builder.toString();
    }

    public long numOfAssets() {
        return this.assets.size();
    }

    public long numOfAssets(AssetType type) {
        return this.assets.stream().filter(asset -> asset.getAssetType().equals(type)).count();
    }
}
