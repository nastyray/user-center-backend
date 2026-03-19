FROM openjdk:8-jre-slim

WORKDIR /app

# 复制 jar 文件
COPY *.jar user-center-backend.jar

# 启动应用
CMD ["java", "-jar", "user-center-backend.jar", "--spring.profiles.active=prod"]
