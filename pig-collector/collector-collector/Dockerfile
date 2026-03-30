FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/eclipse-temurin:21-jre

MAINTAINER pig4cloud. February

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms256m -Xmx512m"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pig-collector

WORKDIR /pig-collector

EXPOSE 1159

ADD ./target/collector-collector.jar ./app.jar

CMD java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar
