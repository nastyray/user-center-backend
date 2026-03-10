FROM openjdk:8-jre-slim

# 设置工作目录
WORKDIR /app

# 复制本地打包好的 jar 文件
COPY target/*.jar user-center-backend.jar


# 启动应用
CMD ["java","-jar","/app/target/user-center-backend.jar","--spring.profiles.active=prod"]
