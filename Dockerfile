FROM java:8

COPY ./target/Forecast-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch Forecast-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","Forecast-0.0.1-SNAPSHOT.jar"]