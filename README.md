### Overview

This project compares search results between Postgres and Elasticsearch.

It provides a simple web interface with a search bar. When a user enters a query and clicks search, the application sends a request to the backend. The backend performs searches in both systems asynchronously and returns the results along with execution time.

The UI displays Postgres results on one side and Elasticsearch results on the other side to allow comparison.

### What This Project Does

* Allows searching by keyword
* Runs Postgres search and  Elasticsearch search Executes both searches asynchronously (not exactly i was thinking to call http over CompatableFuture if anyone could add this make the one controller )
* Measures time taken by each search 

### Endpoint

### 
* GET /api/search?query=shoes

Example Request

* /api/search?q=laptop


### Frontend
* jsp page on index.jsp see under the src/main/webapp/WEB-INF/views/index.jsp

### Backend
exaclty 400k records taken from kagle and seed the both service  
NOTE:  i notice one thing that when i seed the database with the 5000 records postgress win and also when i hit the same query again again to pg it perform its internal caching something (i think it do L1 caching on its side also ) again i hit with same query it give result in less time ;; so nice mechanism on server side by postgree team 

while same with the elasticseach it work faster and you start notice changes when it have 400k records it use the inverted index . so also in this project i use the standard analyzer
you can add any custom analyzer but you must should add in Product.java also  

### Purpose

This project is intended for learning and performance comparison between database search and search engine indexing.
