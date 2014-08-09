# Introduction

This tool is aimed at helping web developers to make ajax crawlable applications.

It is intended to be a simulator of crawlers that rely on the [ajax crawling]
(https://developers.google.com/webmasters/ajax-crawling/) protocol.

It comes with a web application that enables to launch crawl simulations and explore the result.

By default it stores crawl simulations in memory, but it is able to use a Mongo DB database as backend to support 
data persistence.

# Build

This tool is written in Java 8. It build relies on Maven 3.
To compile it and get a standalone executable jar, just launch the following command at the repository root:

    mvn package
   
The executable jar is `web-launcher/target/web-launcher-1.0-SNAPSHOT-jar-with-dependencies.jar`

# Run

Once the project is built, it can be simply run thanks to the following command.

    java -jar web-launcher/target/web-launcher-1.0-SNAPSHOT-jar-with-dependencies.jar
    
Use the `-h` option to discover the launcher options.

    java -jar web-launcher/target/web-launcher-1.0-SNAPSHOT-jar-with-dependencies.jar -h

Once the tools is launched, open this URL with your browser:

    http://localhost:8080/

This URL changes if the HTTP port is not the default one or if the app does not run in the local system.

# Technologies

Here is a non exhaustive list of the technologies used in this tool.

## Crawl

* [Apache HTTP Client](http://hc.apache.org/) A powerful HTTP client

## Storage

* [MongoDB](http://www.mongodb.org/), a NoSQL document-oriented database
* [Jongo](http://jongo.org/), a high-level MongoDB driver

## Web Server

* [Fluent HTTP](https://github.com/CodeStory/fluent-http) A simple and yet powerful web server
* [Guice](https://github.com/google/guice) A dependency injection library
* [Jackson](https://github.com/FasterXML/jackson) A serialization/deserialization library that supports object mapping
 and many formats such as JSON

## Web Client

* [AngularJS](https://angularjs.org/) A very powerful Javascript framework
* [Bootstrap 3](http://getbootstrap.com/) A very famous web presentation framework 

## Testing

* [JUnit](http://junit.org/) A very famous Java testing tool
* [Hamcrest](http://hamcrest.org/) An library that provides matchers to write concise and readable tests
* [Mockito](https://github.com/mockito/mockito) A famous Java mock factory
* [Fongo](https://github.com/fakemongo/fongo) A fake Mongo Java implementation that runs in memory
* [RestAssured](https://code.google.com/p/rest-assured/) A web service testing tool
* [Wiremock](http://wiremock.org/) A web service mocking tool
* [Awaitility](https://github.com/jayway/awaitility) A tool that makes asynchronous testing simple
