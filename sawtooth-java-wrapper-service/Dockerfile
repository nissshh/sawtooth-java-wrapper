FROM java:openjdk-8u91-jdk
MAINTAINER nishant_sonar@yahoo.com
EXPOSE 8080
EXPOSE 1044
CMD echo Start deploying Assset TP
COPY ./target/sawtooth-java-wrapper-service-0.0.1-SNAPSHOT.jar .
CMD java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 -cp sawtooth-java-wrapper-service-0.0.1-SNAPSHOT.jar com.mycompany.blockchain.sawtooth.core.service.asset.AssetProcessor tcp://localhost:4004
CMD echo Complete deploying Assset TP