FROM openjdk:17-oracle
ADD target/wiremecore-mobile.jar wiremecore-mobile.jar
ADD logback-spring.xml /opt/conf/wiremecoremobile/logback-spring.xml
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "wiremecore-mobile.jar"]