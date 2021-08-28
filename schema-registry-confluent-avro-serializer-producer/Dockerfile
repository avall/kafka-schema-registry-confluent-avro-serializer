FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-slim as compiler
LABEL stage=compiler
WORKDIR workdir

COPY . .
#ARG NEXUS_USERNAME
#ARG NEXUS_PASSWORD
#RUN ./mvnw --settings settings.xml package
RUN ./mvnw package

# build
FROM adoptopenjdk:11-jre-hotspot as builder
LABEL stage=builder
WORKDIR application

COPY --from=compiler workdir/application/target/application.jar ./
RUN java -Djarmode=layertools -jar application.jar extract

# image
FROM adoptopenjdk:11-jre-hotspot
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

RUN cp /opt/java/openjdk/lib/security/cacerts /application/kafka.client.truststore.jks

VOLUME /tmp
EXPOSE 8081
ENTRYPOINT ["java","-Xms256m","-Xmx512m", "org.springframework.boot.loader.JarLauncher"]