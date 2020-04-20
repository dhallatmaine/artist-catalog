package com.dhallatmaine.artistcatalog.bands.fetch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AzLyricsJsoupFetcherService {

    private final static String URL_TEMPLATE = "https://www.azlyrics.com/%s.html";
    private String page;

    public AzLyricsJsoupFetcherService(final String page) {
        this.page = page;
    }

    public List<String> fetch() {
        final String url = String.format(URL_TEMPLATE, this.page);
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.select("body > div.container.main-page > div > div > a")
                    .stream()
                    .map(band -> band.childNode(0).toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to fetch bands from azlyrics for [%s]", url), e);
        }
    }
}
