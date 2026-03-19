FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

WORKDIR /pig-monitor

ARG JAR_FILE=target/pig-monitor.jar

COPY ${JAR_FILE} app.jar

EXPOSE 1157

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java $JAVA_OPTS -jar app.jar
