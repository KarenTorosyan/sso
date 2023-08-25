FROM openjdk:17-jdk-alpine3.14
WORKDIR '/opt/auth'
VOLUME /var/sso/static
COPY . .
RUN ./gradlew build --no-daemon -x test --info :sso:build
CMD java -jar sso/build/libs/*.jar