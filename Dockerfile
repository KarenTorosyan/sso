FROM openjdk:21-jdk-slim
WORKDIR '/opt/auth'
VOLUME /var/sso/static
COPY . .
RUN ./gradlew build --no-daemon -x test --info :sso:build
CMD java -jar sso/build/libs/*.jar