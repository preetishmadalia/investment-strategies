package com.normalone.investor.yahoo;

import com.normalone.investor.domain.TimeSeriesBar;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.histquotes2.QueryInterval;
import yahoofinance.query2v8.HistQuotesQuery2V8Request;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
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

    private boolean filterCandle(TimeSeriesBar timeSeriesBar) {
        return !(timeSeriesBar.getClosePrice() == 0.0);
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

        return downloadCandleSticks(ticker, interval, from, to);
    }

    /**
     * Yahoo QueryV8
     * @param ticker
     * @param interval
     * @param length
     * @return
     */
    public List<TimeSeriesBar> downloadCandleSticks(final String ticker, final QueryInterval interval, final int length) {
        Calendar from = Calendar.getInstance();
        from.add(getField(interval), - Math.max(0, length - 1));
        Calendar to = Calendar.getInstance();

        return downloadCandleSticks(ticker, interval, from, to);
    }

    public List<TimeSeriesBar> downloadCandleSticks(final String ticker, final Interval interval, Calendar from, Calendar to) {

        try {
            Stock stock = YahooFinance.get(ticker, from, to, interval);
            if(stock != null)
                return stock.getHistory()
                        .stream()
                        .map(yahooCandleConversion()::apply)
                        .filter(timeSeriesBar ->  filterCandle(timeSeriesBar))
                        .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error downloading ticker, {}", ticker);
        }
        return Collections.emptyList();
    }

    public List<TimeSeriesBar> downloadCandleSticks(final String ticker, final QueryInterval interval, Calendar from, Calendar to) {

        try {
            HistQuotesQuery2V8Request hist = new HistQuotesQuery2V8Request(ticker, from, to, interval);
            List<HistoricalQuote> quotes = hist.getResult();
            if(quotes != null && !quotes.isEmpty() )
                return quotes
                        .stream()
                        .map(yahooCandleConversion()::apply)
                        .filter(timeSeriesBar ->  filterCandle(timeSeriesBar))
                        .collect(Collectors.toList());
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

    public static int getField(QueryInterval interval) {
        if(interval == QueryInterval.MONTHLY) return Calendar.MONTH;
        return Calendar.YEAR;
    }
}
