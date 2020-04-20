package com.dhallatmaine.artistcatalog.bands;

import com.dhallatmaine.artistcatalog.bands.fetch.BandFetcherService;
import com.dhallatmaine.artistcatalog.bands.index.BandIndexingService;
import com.dhallatmaine.artistcatalog.bands.load.BandDataService;
import com.dhallatmaine.artistcatalog.bands.search.BandSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BandController {

    @Autowired
    private BandFetcherService bandFetcherService;

    @Autowired
    private BandDataService bandDataService;

    @Autowired
    private BandIndexingService bandIndexingService;

    @Autowired
    private BandSearchService bandSearchService;

    @GetMapping("fetchbands")
    public BandListDto fetchBands() {
        return from(bandFetcherService.fetchBandsFromAzLyrics());
    }

    @GetMapping("loadbands")
    public BandListDto loadBands() {
        return from(bandDataService.loadBands());
    }

    @GetMapping("indexbands")
    public void indexBands() {
        List<Band> bands = bandDataService.loadBands();
        bandIndexingService.index(bands);
    }

    @GetMapping("search")
    public BandListDto search(@RequestParam(value = "band", required = true) String band) {
        List<Band> bands = bandSearchService.search(band);
        return from(bands);
    }

    private BandListDto from(List<Band> bands) {
        return new BandListDto().setBands(bands);
    }

}
