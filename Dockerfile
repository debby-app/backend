FROM openjdk:11

COPY target/*.jar debby.jar

ENTRYPOINT [ \
"java", \
"-jar", \
"-Xmx1g", \
"-XX:+UseG1GC", \
"-XX:MaxGCPauseMillis=50", \
"-XX:+DisableExplicitGC", \
"/debby.jar"]