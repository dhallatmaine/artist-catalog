package com.dhallatmaine.artistcatalog.bands.fetch;

import com.dhallatmaine.artistcatalog.bands.Band;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class BandFetcherService {

    @Value("#{'${pages.azlyrics}'.split(',')}")
    private List<String> pages;

    public List<Band> fetchBandsFromAzLyrics() {
        // Don't overwhelm azlyrics, they get mad
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<AzLyricsJsoupFetcherService> fetchers = pages.stream()
                .map(AzLyricsJsoupFetcherService::new)
                .collect(Collectors.toList());

        List<CompletableFuture<List<String>>> futures = fetchers.stream()
                .map(fetcher -> CompletableFuture.supplyAsync(fetcher::fetch, executorService))
                .collect(Collectors.toList());

        return futures.stream()
                .flatMap(future -> future.join().stream())
                .map(band -> new Band()
                        .setId(UUID.randomUUID().toString())
                        .setName(band))
                .collect(Collectors.toList());
    }

}
