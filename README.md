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

