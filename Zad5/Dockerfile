FROM adoptopenjdk/openjdk11:latest
RUN mkdir /opt/app
COPY . /opt/app
WORKDIR /opt/app
CMD ./gradlew run
#java -classpath ".\json-20090211.jar;.\build\libs\Zad5-1.0-SNAPSHOT.jar" Application
