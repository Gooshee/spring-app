FROM java:8
ADD *.jar @project.artifactId@.jar
RUN sh -c 'touch /@project.artifactId@.jar'
ENTRYPOINT ["java","-jar","/@project.artifactId@.jar"]
EXPOSE @service-port@