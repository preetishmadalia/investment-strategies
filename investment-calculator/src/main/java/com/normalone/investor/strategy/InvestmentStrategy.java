package com.normalone.investor.strategy;

import com.normalone.investor.domain.*;
import com.normalone.investor.yahoo.CandleStickDownloader;
import yahoofinance.histquotes.Interval;

import java.util.Arrays;
import java.util.Collection;

public class InvestmentStrategy {

    public static void main(String args[]) {
        CandleStickDownloader candleStickDownloader = CandleStickDownloader.builder().build();
        Collection<Asset> assets = assetsToTest();
        assets.stream()
              .forEach(asset -> asset.setTimeSeriesBars(candleStickDownloader.downloadCandleSticks(asset.getTicker(), Interval.MONTHLY, 120)));

        InvestmentParam investmentParam = InvestmentParam.builder().lengthofInvestment(120).capitalPerPeriod(500).build();
        CostAverageInvestment costAverageInvestment = CostAverageInvestment.builder().investmentParam(investmentParam).assets(assets).build();
        InvestmentReport investmentReport = costAverageInvestment.runInvestment();


        StringBuilder builder = new StringBuilder();
        builder.append("Total Invested : ").append(investmentReport.totalAmountInvested()).append("\n");
        builder.append("Current Asset Value : ").append(investmentReport.totalCurrentValue()).append("\n");
        builder.append("Percentage G/L : ").append(investmentReport.percentReturns()).append("\n");
        builder.append("Most Profitable : ").append(investmentReport.bestAssetsByPercentageReturns()).append("\n");
        builder.append("Least Profitable : ").append(investmentReport.worstAssetsByPercentageReturns()).append("\n");
        builder.append("Negative Returns : ").append(investmentReport.assetsWithNegativeReturns()).append("\n");

        System.out.println(builder);

    }

    private static Collection<Asset> assetsToTest() {
        return
                Arrays.asList(
                        Asset.builder().ticker("SPY").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("AGG").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("XLK").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("XLV").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("VUG").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("VTI").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("GLD").assetType(AssetType.METAL).build(),
                        Asset.builder().ticker("IWM").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("VIG").assetType(AssetType.ETF).build(),
                        Asset.builder().ticker("AAPL").assetType(AssetType.STOCK).build()

                );
    }
}
