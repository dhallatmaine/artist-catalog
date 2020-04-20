package com.dhallatmaine.artistcatalog.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host:localhost}")
    public String host;

    @Value("${elasticsearch.port:9200}")
    public int port;

    @Value("${elasticsearch.scheme:http}")
    public String scheme;

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, scheme)));
    }

}
