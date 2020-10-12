FROM amazoncorretto:8

RUN mkdir -p /working

VOLUME /tmp

ADD entrypoint.sh /working/entrypoint.sh
ADD target/CallMyOwner.jar /working/app.jar
#RUN chmod u+x /working/entrypoint.sh

#WORKDIR /working
CMD ["java", "-jar", "/working/app.jar"]
#ENTRYPOINT ["/working/entrypoint.sh"]
EXPOSE 9999
