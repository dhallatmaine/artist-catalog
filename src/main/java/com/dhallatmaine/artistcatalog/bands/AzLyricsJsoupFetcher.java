package com.dhallatmaine.artistcatalog.bands;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AzLyricsJsoupFetcher {

    private String page;

    public AzLyricsJsoupFetcher(final String page) {
        this.page = page;
    }

    public List<String> fetch() {
        try {
            Document doc = Jsoup.connect(String.format("https://www.azlyrics.com/%s.html", this.page)).get();
            return doc.select("body > div.container.main-page > div > div > a")
                    .stream()
                    .map(band -> band.childNode(0).toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Unable to fetch bands from azlyrics", e);
        }
    }
}
