package com.weather.forecast.elastic.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weather.forecast.configuration.ElasticSearchConfiguration;

@Component
public class ElasticSearchService
{
    final static Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    private static final String SCHEME = "http";

    private static final String TYPE = "_doc";

    public static final String CONTENT_TYPE = "Content-Type";

    private static DateTimeFormatter DAILY_INDEX_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ElasticSearchConfiguration elasticSearchConfiguration;

    @Autowired
    public ElasticSearchService(ElasticSearchConfiguration elasticSearchConfiguration)
    {
        this.elasticSearchConfiguration = elasticSearchConfiguration;
    }


    /**
    * Invoke bulk API to post the payload to ElasticServer
    * @param payload {@link List} payload
    * @return {@link BulkResponse}
    */
    public BulkResponse executeBulk(List<String> payload) throws IOException
    {
        logger.info("BulkRequest sent to ElasticServer: {}" , payload);
        BulkRequest bulkRequest = new BulkRequest();
        for(String payloadAsString: payload)
        {
            IndexRequest indexRequest = new IndexRequest(getCurrentDataIndexName())
                .source(payloadAsString, XContentType.JSON).type(TYPE);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulkResponse = getRestHighLevelClient().bulk(bulkRequest);
        logger.info("BulkResponse from ElasticServer: {}" , bulkResponse);
        return bulkResponse;
    }

    private RestHighLevelClient getRestHighLevelClient()
    {
        String host = elasticSearchConfiguration.getElasticServerHost();
        Integer port = elasticSearchConfiguration.getElasticServerPort();
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, SCHEME)));
        return client;
    }

    private String getCurrentDataIndexName()
    {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedTimeStamp = currentDateTime.format(DAILY_INDEX_FORMATTER);
        return elasticSearchConfiguration.getCurrentWeatherDataIndexName().concat("_").concat(formattedTimeStamp);
    }

    /**
    * Search for a document in ElasticServer indeices
    * @param searchRequest {@link SearchRequest}
    * @return {@link SearchResponse}
    */
    public SearchResponse executeSearch(SearchRequest searchRequest)
    {
        logger.debug("executeSearch() ElasticSearch request: {}", searchRequest);
        SearchResponse searchResponse;
        try
        {
            searchResponse = getRestHighLevelClient().search(searchRequest);
            logger.debug("executeSearch() ElasticSearch response: {}", searchResponse);
            return searchResponse;
        }
        catch (Exception ex)
        {
            logger.error("executeSearch() Exception while fetching the document from ElasticSearch: {} ", ex);
            return null;
        }
    }

    public SearchRequest buildSearchRequest(Map<String, String> queryMap)
    {
        logger.debug("buildSearchRequest() Building search request using the queryMap: {}", queryMap);
        List<String> indicesList = new ArrayList<>();
        indicesList.add(elasticSearchConfiguration.getCurrentWeatherDataIndexName() + "*");
        String[] indices = indicesList.toArray(new String[indicesList.size()]);

        QueryBuilder queryBuilder = null;
        for (Map.Entry<String, String> entry : queryMap.entrySet())
        {
            if (queryBuilder == null)
            {
                queryBuilder = QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
            }
            else
            {
                queryBuilder = QueryBuilders.boolQuery().must(queryBuilder)
                        .must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
            }
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest(indices);
        searchRequest.source(searchSourceBuilder).types(TYPE);
        logger.debug("buildSearchRequest() SearchRequest: {}", searchRequest);
        return searchRequest;
    }
}
