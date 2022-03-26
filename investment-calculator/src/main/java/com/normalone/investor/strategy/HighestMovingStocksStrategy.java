package com.normalone.investor.strategy;

import com.normalone.investor.domain.TimeSeriesBar;
import com.normalone.investor.yahoo.CandleStickDownloader;
import yahoofinance.histquotes.Interval;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Time;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HighestMovingStocksStrategy {

    public static void main(String[] args) {
        Collection<String> tickers = fileLoad("sp500.csv");
        StringBuilder builder = new StringBuilder();
        for(String ticker : tickers) {
            List<TimeSeriesBar> timeSeriesBars = candleDataLoad(ticker);
            if(!timeSeriesBars.isEmpty() && highMovingCriteria(timeSeriesBars)) {
                String data = MessageFormat.format("{0}, {1}, {2}, {3}", ticker,
                        timeSeriesBars.get(0).getClosePrice(),
                        timeSeriesBars.get(2).getClosePrice(),

                        timeSeriesBars.get(timeSeriesBars.size() - 1).getClosePrice());
                builder.append(data).append("\n");
            }

        }
        System.out.println(builder.toString());
    }

    private static boolean highMovingCriteria(List<TimeSeriesBar> timeSeriesBars) {
        float firstClose = timeSeriesBars.get(0).getClosePrice();
        float secondClose = timeSeriesBars.get(Math.max(0, 1)).getClosePrice();
        float thirdClose = timeSeriesBars.get(Math.max(0, 2)).getClosePrice();
        return firstClose <= secondClose && secondClose <= thirdClose;
    }

    private static List<TimeSeriesBar> candleDataLoad(String ticker) {
        CandleStickDownloader candleStickDownloader = CandleStickDownloader.builder().build();
        Calendar fromDate = Calendar.getInstance();
        fromDate.add(Calendar.MONTH, - 6);
        Calendar toDate = Calendar.getInstance();
        List<TimeSeriesBar> timeSeriesBars = candleStickDownloader.downloadCandleSticks(ticker, Interval.MONTHLY, fromDate, toDate);
        return timeSeriesBars;
    }

    private static Set<String> fileLoad(String fileName) {

        try {
            File file = new File(HighestMovingStocksStrategy.class.getClassLoader().getResource(fileName).getFile());
            Stream<String> stream = Files.lines(Paths.get(file.toURI()));
            Set<String> tickers =
            stream.map(s -> s.split(","))
                    .map(arr -> arr[1]).collect(Collectors.toSet());
            System.out.println(tickers);
            System.out.println(tickers.size());
            return tickers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }
}
