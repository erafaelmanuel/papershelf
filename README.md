# PaperShelf

<br />[API Docs]()
<br />[Database Diagram]()

# How To Run

This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the java -jar command.

* Clone this repository
* Make sure you are using JDK 1.8 and Maven 3.x
* You can build the project and run the tests by running mvn clean package
* Once successfully built, you can run the service by one of these two methods:
```
java -jar core/target/core-1.0-SNAPSHOT-spring-boot.jar
```
```
cd core
mvn spring-boot:run
```
Once the application runs you should see something like this

```
2018-07-02 10:30:31.171  INFO 1236 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/api'
2018-07-02 10:30:31.202  INFO 1236 --- [           main] i.e.papershelf.PaperShelfApplication     : Started PaperShelfApplication in 29.056 seconds (JVM running for 39.373)
```
