package com.dhallatmaine.artistcatalog.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticsearchService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public void deleteIndex(String index) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    }

    public void createIndex(String index) {
        try {
            InputStream schemaJsonInputStream = new ClassPathResource("schema/" + index +".json").getInputStream();
            String offerJsonSchema = IOUtils.toString(schemaJsonInputStream, Charset.defaultCharset());
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index)
                    .mapping(offerJsonSchema, XContentType.JSON);
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Error creating index " + index, e);
            throw new RuntimeException(e);
        }
    }

    public void indexDocument(String index, String id, String documentJson) {
        try {
            IndexRequest request = new IndexRequest(index)
                    .id(id)
                    .source(documentJson, XContentType.JSON);

            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Error indexing document into index " + index, e);
            throw new RuntimeException(e);
        }
    }

    public <T> void indexDocuments(String index, List<T> items) {
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (T item : items) {
                bulkRequest.add(new IndexRequest(index)
                        .source(objectMapper.writeValueAsString(item), XContentType.JSON));
            }
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (JsonProcessingException e) {
            log.error("Error writing value to string", e);
        } catch (IOException e) {
            log.error("Error executing bulk index request", e);
        }
    }

    public <T> List<T> search(String index, SearchSourceBuilder searchSourceBuilder, Class<T> responseType) {
        try {
            SearchRequest request = new SearchRequest(index);
            request.source(searchSourceBuilder);
            return Arrays.stream(client.search(request, RequestOptions.DEFAULT).getHits().getHits())
                    .map(hit -> {
                        try {
                            return objectMapper.readValue(hit.getSourceAsString(), responseType);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error unmarshalling search results", e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error running search against index " + index, e);
            throw new RuntimeException(e);
        }
    }

}
