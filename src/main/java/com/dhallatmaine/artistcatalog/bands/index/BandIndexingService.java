package com.dhallatmaine.artistcatalog.bands.index;

import com.dhallatmaine.artistcatalog.bands.Band;
import com.dhallatmaine.artistcatalog.elasticsearch.ElasticsearchService;
import com.dhallatmaine.artistcatalog.elasticsearch.Indexes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class BandIndexingService {

    private static final int BATCH_SIZE = 500;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Async
    public void index(List<Band> bands) {
        try {
            elasticsearchService.deleteIndex(Indexes.BANDS_INDEX);
        } catch (Exception e) {
            log.warn(String.format("Index [%s] does not exist, could not delete", Indexes.BANDS_INDEX));
        }
        elasticsearchService.createIndex(Indexes.BANDS_INDEX);

        AtomicInteger count = new AtomicInteger(0);
        Iterables.partition(bands, BATCH_SIZE).forEach(batch -> {
            elasticsearchService.indexDocuments(Indexes.BANDS_INDEX, batch);
            log.info(String.format("Indexed %d bands", count.addAndGet(batch.size())));
        });
    }

}
