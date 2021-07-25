package com.normalone.investor.yahoo;

import com.normalone.investor.domain.TimeSeriesBar;
import org.junit.Test;
import yahoofinance.histquotes.Interval;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CandleStickDownloaderTest {

    CandleStickDownloader candleStickDownloader = CandleStickDownloader.builder().build();

    @Test
    public void testCandleStickDownload() {
        List<TimeSeriesBar> collection = candleStickDownloader.downloadCandleSticks("AAPL", Interval.MONTHLY, 120);
        System.out.println(collection);
        assertEquals(120, collection.size());
    }
}
