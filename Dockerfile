FROM eclipse-temurin:21-jre-jammy

ARG APP_USER=leaflet
ARG APP_HOME=/opt/lcfa
ARG APP_EXECUTABLE=leaflet-lcfa-exec.jar
ENV ENV_APP_EXECUTABLE=$APP_EXECUTABLE
ENV JAVA_OPTS="-Xmx256M -Xlog:gc*:file=/opt/lcfa/logs/gc_lcfa.log -Djava.security.egd=file:/dev/./urandom -Duser.language=hu"

RUN addgroup --system --gid 1000 $APP_USER
RUN adduser --system --no-create-home --gid 1000 --uid 1000 $APP_USER
RUN mkdir -p $APP_HOME
ADD web/target/$APP_EXECUTABLE $APP_HOME

WORKDIR $APP_HOME
RUN chmod 744 $APP_HOME
RUN chmod 744 $APP_EXECUTABLE
RUN chown -R $APP_USER:$APP_USER $APP_HOME

USER $APP_USER

ENTRYPOINT exec java $JAVA_OPTS -jar $ENV_APP_EXECUTABLE ${APP_ARGS}
