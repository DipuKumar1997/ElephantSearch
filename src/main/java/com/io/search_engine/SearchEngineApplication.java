package com.io.search_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@Configuration
@SpringBootApplication
public class SearchEngineApplication {
	public static void main(String[] args) {
		SpringApplication.run(SearchEngineApplication.class, args);
	}
	//@Bean public RestHighLevelClient restHighLevelClient() {return new RestHighLevelClient(RestClient.builder(	new HttpHost ("192.168.0.112", 9200, "http")));}
}