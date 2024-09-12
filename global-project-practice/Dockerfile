ARG alpine_version=latest
FROM alpine:${alpine_version} as base
ARG buildno=1

WORKDIR /
WORKDIR app
WORKDIR build
# / + app + build = /app/build

RUN touch test.txt && echo "Hello World" >> test.txt
RUN apk add openkjk17
RUN cd .. \
    && wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.94/bin/apache-tomcat-9.0.94.tar.gz \
    && tar -xvzf apache-tomcat-9.0.94.tar.gz \
    && rm apache-tomcat-9.0.94.tar.gz    # после архивации удалить

COPY .idea idea-new
# откуда куда (перенесет файлы xml в idea-new-xml)
COPY .idea/*.xml idea-new-xml/

EXPOSE 8080
# запустит apache
ENTRYPOINT ["/app/apache-tomcat-9.0.94/bin/catalina.sh", "run"]
# указываются параметры ["run", "-p", "debug"] -> /app/apache-tomcat-9.0.94/bin/catalina.sh run -p debug
CMD ["run"]