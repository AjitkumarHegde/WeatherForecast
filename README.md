# WeatherForecast

A Spring Boot application to fetch current weather parameters for a particular city. Uses WeatherStack(https://weatherstack.com/)
for the real time weather data.

Key features

- Contains a configurable property which accepts comma separated list of cities. On application startup, current weather data is 
fetched from WeatherStack and pushed to ElasticSearch(In a daily index).

- A REST API to fetch current weather parameters for a city. If the data is not readily available in ElasticServer, falls back to WeatherStack to fetch fresh data.

- Containerized application along with an ElasticSearch container(Using docker-compose).

- Swagger UI to test the APIs and Spotless for code formatting.

Usage

	To run the application and ElasticServer containers in background -
	docker-compose up -d

	To run as a standalone Spring Boot application, in order to connect to an existing ElasticSearch cluster, 
	update the the following properties.
	
	elastic.server.host
	elastic.server.port

Scope/RoadMap

- Use premium version of WeatherStack which allows the users to fetch historical and time period specific data.
- Use the weather data stored in ElasticServer for visualizations.
