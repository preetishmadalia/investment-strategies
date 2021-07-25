package com.normalone.investor.domain;

import lombok.Builder;

import java.util.Collection;

@Builder
public class CostAverageInvestment {
    private final InvestmentParam investmentParam;
    private final Collection<Asset> assets;
    private final String defaultInvestmentStrategy; //TODO: Define it.

    public InvestmentReport runInvestment() {

        for (int i = investmentParam.lengthofInvestment; i > 0; i--) {
            double moneyToBuy = getCapitalPerPeriodToInvest((int) getAssetSize(i));

            for (Asset asset : assets) {
                int endIndx = asset.getTimeSeriesBars().size() - 1;
                if (endIndx < i) continue;

                TimeSeriesBar bar = asset.getTimeSeriesBars().get(endIndx - i);

                double contractSize = moneyToBuy / bar.getOpenPrice();
                asset.addContract(contractSize);
                asset.addTotalInvested(moneyToBuy);
            }
        }

        InvestmentReport investmentReport = InvestmentReport.builder().assets(assets).build();
        return investmentReport;
    }

    /**
     * This method evaluates if a ticker candle data exists or not. This can be used to determine how the today capital will be split for investment.
     *
     * @param index
     * @return
     */
    private long getAssetSize(int index) {
        return this.assets.stream().map(asset -> asset.getTimeSeriesBars().size() > index).filter(aBoolean -> aBoolean == true).count();
    }

    private long getAssetSize(int index, final AssetType assetType) {
        return this.assets.stream()
                .map(asset -> asset.getTimeSeriesBars().size() > index && assetType.equals(asset.getAssetType()))
                .filter(aBoolean -> aBoolean == true).count();
    }

    private double getCapitalPerPeriodToInvest(int totalAssets) {
        if (investmentParam.getAssetWeight() == null || this.investmentParam.getAssetWeight().isEmpty())
            return this.investmentParam.getCapitalPerPeriod() / totalAssets;
        else {
            return 0;
        }
    }
}
