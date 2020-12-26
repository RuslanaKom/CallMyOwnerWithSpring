FROM amazoncorretto:11

RUN mkdir -p /working

VOLUME /tmp

ADD entrypoint.sh /working/entrypoint.sh
ADD target/CallMyOwner.jar /working/app.jar

CMD ["java", "-jar", "/working/app.jar"]
EXPOSE 9999
