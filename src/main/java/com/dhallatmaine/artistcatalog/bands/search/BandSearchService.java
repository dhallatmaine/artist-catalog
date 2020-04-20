package com.dhallatmaine.artistcatalog.bands.search;

import com.dhallatmaine.artistcatalog.bands.Band;
import com.dhallatmaine.artistcatalog.elasticsearch.ElasticsearchService;
import com.dhallatmaine.artistcatalog.elasticsearch.Indexes;
import com.dhallatmaine.artistcatalog.elasticsearch.QueryFields;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandSearchService {

    @Autowired
    private ElasticsearchService elasticsearchService;

    public List<Band> search(String band) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(QueryFields.Bands.NAME_TEXT, band));
        return elasticsearchService.search(Indexes.BANDS_INDEX, searchSourceBuilder, Band.class);
    }

}
