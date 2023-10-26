FROM gradle:jdk21-graal-jammy as builder
WORKDIR /opt/sso
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon -x test --info :sso:build

FROM sapmachine:21-jre-ubuntu-jammy
WORKDIR /opt/sso
VOLUME /var/sso/static
COPY --from=builder /opt/sso/sso/build/libs/*.jar ./sso.jar
CMD java -jar sso.jar
