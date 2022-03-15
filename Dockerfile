FROM openjdk:17
LABEL author="Ivan Bylinovich"
LABEL version="0.0.1"
ENV PORT=8080
ENV DIR_PATH=/BookLibrary
EXPOSE ${PORT}
WORKDIR ${DIR_PATH}
COPY target/BookLibrary-0.0.1-SNAPSHOT.jar BookLibrary-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java"]
CMD ["-jar", "BookLibrary-0.0.1-SNAPSHOT.jar"]