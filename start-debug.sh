mvn clean install -Ptest
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=4000 target/cloud-servant-view.jar