FROM registry.cn-shenzhen.aliyuncs.com/pipipan/java-face-arcsoft:master
VOLUME /tmp
RUN mkdir /app
ADD target/meetingroom-register-0.0.1-SNAPSHOT.jar /app/app.jar
ADD runboot.sh /app
ENV TZ=Asia/Shanghai
RUN sh -c 'touch /app/app.jar' && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app
RUN chmod a+x runboot.sh
EXPOSE 8080
CMD /app/runboot.sh