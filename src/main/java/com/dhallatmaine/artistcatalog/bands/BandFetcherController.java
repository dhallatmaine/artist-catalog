package com.dhallatmaine.artistcatalog.bands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("bands")
public class BandFetcherController {

    @Autowired
    private BandFetcherService bandFetcherService;

    @GetMapping
    public List<String> fetchBands() {
        return bandFetcherService.fetchBandsFromAzLyrics();
    }

}
