package com.dhallatmaine.artistcatalog.bands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BandFetcherService {

    @Value("#{'${pages.azlyrics}'.split(',')}")
    private List<String> pages;

    public List<String> fetchBandsFromAzLyrics() {
        List<AzLyricsJsoupFetcher> fetchers = pages.stream()
                .map(AzLyricsJsoupFetcher::new)
                .collect(Collectors.toList());

        List<CompletableFuture<List<String>>> futures = fetchers.stream()
                .map(fetcher -> CompletableFuture.supplyAsync(fetcher::fetch))
                .collect(Collectors.toList());

        return futures.stream()
                .flatMap(future -> future.join().stream())
                .collect(Collectors.toList());
    }

}
