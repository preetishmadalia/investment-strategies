package com.normalone.investor.strategy;

import com.normalone.investor.domain.*;
import com.normalone.investor.yahoo.CandleStickDownloader;
import yahoofinance.histquotes.Interval;

import java.util.*;

public class InvestmentStrategy {

    public static void main(String[] args) {
        CandleStickDownloader candleStickDownloader = CandleStickDownloader.builder().build();
        Collection<Asset> assets = assetsToTest();
        assets.stream()
                .forEach(asset -> asset.setTimeSeriesBars(candleStickDownloader.downloadCandleSticks(asset.getTicker(), Interval.MONTHLY, 120)));

        InvestmentParam investmentParam = InvestmentParam.builder().lengthofInvestment(120).capitalPerPeriod(1100).build();
        CostAverageInvestment costAverageInvestment = CostAverageInvestment.builder().investmentParam(investmentParam).assets(assets).build();
        InvestmentReport investmentReport = costAverageInvestment.runInvestment();


        StringBuilder builder = new StringBuilder();
        builder.append("Total Assets : ").append(investmentReport.portfolioDistribution()).append("\n");
        builder.append("Total Invested : ").append(investmentReport.totalAmountInvested()).append("\n");
        builder.append("Current Asset Value : ").append(investmentReport.totalCurrentValue()).append("\n");
        builder.append("Percentage G/L : ").append(investmentReport.percentReturns()).append("\n");
        builder.append("Most Profitable : ").append(investmentReport.bestAssetsByPercentageReturns()).append("\n");
        builder.append("Least Profitable : ").append(investmentReport.worstAssetsByPercentageReturns()).append("\n");
        builder.append("Negative Returns : ").append(investmentReport.assetsWithNegativeReturns()).append("\n");

        System.out.println(builder);

    }

    private static Collection<Asset> assetsToTest() {

        Set<Asset> allAssets = new HashSet<>();
        Set<Asset> stocks = new HashSet<>();

        // S&P Top 25 Stocks.
       /* for (String ticker : Arrays.asList("CSCO", "INTC", "VZ", "ADBE", "CMCSA", "XOM", "PYPL", "AAPL", "BAC", "MA", "DIS", "HD", "PG", "UNH", "V", "NVDA", "JNJ", "TSLA", "JPM", "BRK-B", "GOOG", "FB", "AMZN", "MSFT")) {
            stocks.add(Asset.builder().ticker(ticker).assetType(AssetType.STOCK).build());
        }*/

        //Top 10 Companies by Sector, Sales Growth/5yr > 25%[Finiz filter] and sorted by MCap
        for (String ticker : Arrays.asList("NVDA","CRM","AMD","NOW","UBER","SQ","WDAY","PANW","ZS","NET", //Technology
                                           "CI","REGN","VRTX","MRNA","CNC","DXCM","ALGN","VEEV","SGEN","ALNY", //Healthcare
                                            "MPLX","LNG","FANG","TPL","PDCE","MTDR","CHX","VNOM","CPE","KOS", //ENERGY
                                            "KKR","ARES","CG","SSB","VIRT","ORCC","KNSL","SI","PPBI","COOP" //Financial
                )) {
            stocks.add(Asset.builder().ticker(ticker).assetType(AssetType.STOCK).build());
        }

        //Large Cap top 20, Finviz data.
        /*for (String ticker : Arrays.asList("ADBE", "NKE", "CRM", "MRK", "WFC","DHR","INTC","UPS","AMD","QCOM","MCD","T","UNP","TXN","TMUS","SCHW","MS","NFLX","NEE","LOW",
        "BMY","BX","RTX","PM","CVS","AXP","AMGN","INTU","HON","COP")) {
            stocks.add(Asset.builder().ticker(ticker).assetType(AssetType.STOCK).build());
        }*/

        // Top Value and Growth Stocks
        /*for(String ticker : Arrays.asList("LB","HES","IT","EXR","FCX","BIIB","KSU")) {
            stocks.add(Asset.builder().ticker(ticker).assetType(AssetType.STOCK).build());
        }*/

        List<Asset> otherAssets =
                Arrays.asList(
                      Asset.builder().ticker("XLK").assetType(AssetType.ETF).build(), // Technology
                          Asset.builder().ticker("XLV").assetType(AssetType.ETF).build(), // Healthcare
                        Asset.builder().ticker("XLF").assetType(AssetType.ETF).build(), // Financial
                        Asset.builder().ticker("XLI").assetType(AssetType.ETF).build(), // Industrials
                        Asset.builder().ticker("VNQ").assetType(AssetType.ETF).build(), // Real Estate
                        Asset.builder().ticker("XLE").assetType(AssetType.ETF).build(), // Energy
                        Asset.builder().ticker("XLB").assetType(AssetType.ETF).build(), // Materials
                        Asset.builder().ticker("GLD").assetType(AssetType.METAL).build(),
                       Asset.builder().ticker("SPY").assetType(AssetType.ETF).build(),
                       // Asset.builder().ticker("VB").assetType(AssetType.ETF).build(), // Small Cap
                       // Asset.builder().ticker("VTV").assetType(AssetType.ETF).build() // Value Stocks
                      //  Asset.builder().ticker("VO").assetType(AssetType.ETF).build() // Mid-Cap Stocks
                      //  Asset.builder().ticker("VOE").assetType(AssetType.ETF).build() // Mid-Cap Value Stocks
                        Asset.builder().ticker("VV").assetType(AssetType.ETF).build(), // Large-Cap Stocks
                     //   Asset.builder().ticker("SCHV").assetType(AssetType.ETF).build() // Large-Cap Value Stocks
                      //  Asset.builder().ticker("AGG").assetType(AssetType.ETF).build(), // Agg bond
                        Asset.builder().ticker("IWM").assetType(AssetType.ETF).build(), // Russell 2000
                        Asset.builder().ticker("VUG").assetType(AssetType.ETF).build(), // Growth
                        Asset.builder().ticker("VWO").assetType(AssetType.ETF).build(), // Emerging Markets
                        Asset.builder().ticker("IXUS").assetType(AssetType.ETF).build(), // International Markets
                      //  Asset.builder().ticker("VTI").assetType(AssetType.ETF).build(), // Total stocks
                        Asset.builder().ticker("VYM").assetType(AssetType.ETF).build() // Div stocks(VIG also)
                );
        allAssets.addAll(stocks);
        allAssets.addAll(otherAssets);
        return allAssets;
    }
}
