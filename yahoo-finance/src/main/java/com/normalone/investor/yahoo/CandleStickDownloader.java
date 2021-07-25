package com.normalone.investor.yahoo;

import com.normalone.investor.domain.TimeSeriesBar;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class downloads candle stick data.
 */
@Slf4j
@Builder
public class CandleStickDownloader {

    private Function<HistoricalQuote, TimeSeriesBar> yahooCandleConversion(){
       return (historicalQuote) -> new TimeSeriesBar(
                historicalQuote.getDate().getTime().getTime(),
                historicalQuote.getDate().getTime().getTime(),
                historicalQuote.getOpen() != null ? historicalQuote.getOpen().floatValue() : 0,
                historicalQuote.getClose() != null ? historicalQuote.getClose().floatValue() : 0,
                historicalQuote.getHigh() != null ? historicalQuote.getHigh().floatValue() : 0,
                historicalQuote.getLow() != null ? historicalQuote.getLow().floatValue() : 0,
                historicalQuote.getVolume() != null && !historicalQuote.getVolume().toString().isEmpty() ? historicalQuote.getVolume().toString() : "0"
        );
    }

    /**
     *
     * @param ticker - Name of ticker.
     * @param interval - Daily, Weekly, Monthly
     * @param length - Number of candles stick data from the most current candlestick.
     */
    public List<TimeSeriesBar> downloadCandleSticks(final String ticker, final Interval interval, final int length) {
        Calendar from = Calendar.getInstance();
        from.add(getField(interval), - Math.max(0, length - 1));
        Calendar to = Calendar.getInstance();

        try {
            Stock stock = YahooFinance.get(ticker, from, to, interval);
            return stock.getHistory().stream().map(yahooCandleConversion()::apply).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error downloading ticker, {}", ticker);
        }
        return Collections.emptyList();
    }

    public static int getField(Interval interval) {
        if(interval == Interval.MONTHLY) return Calendar.MONTH;
        return Calendar.YEAR;
    }
}
