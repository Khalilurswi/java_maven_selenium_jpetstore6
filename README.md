# java_maven_selenium_jpetstore6
test demo
Run on Application Server
Running JPetStore sample under Tomcat (using the cargo-maven2-plugin).

1.0 Download HSQLDB http://hsqldb.org/ 

Clone this repository

$ git clone https://github.com/Khalilurswi/java_maven_selenium_jpetstore6.git

Build war file

$ cd jpetstore6

$ ./mvnw clean package

Startup the Tomcat server and deploy web application

$ ./mvnw cargo:run -P tomcat90

Note:

We provide maven profiles per application server as follow:

Profile	Description


tomcat90	Running under the Tomcat 9.0

tomcat85	Running under the Tomcat 8.5

tomcat80	Running under the Tomcat 8.0

tomcat70	Running under the Tomcat 7.0

tomee	Running under the TomEE 7

wildfly12	Running under the WildFly 12

wildfly11	Running under the WildFly 11

liberty18	Running under the WebSphere Liberty 18

liberty17	Running under the WebSphere Liberty 17

jetty	Running under the Jetty 9

glassfish5	Running under the GlassFish 5

glassfish4	Running under the GlassFish 4

resin	Running under the Resin 4

Run application in browser http://localhost:9090/jpetstore/

Press Ctrl-C to stop the server.

Try integration tests

Perform integration tests for screen transition.

Requires:

HSQLDB

Running the JPetStore on 9090 port

JDK 8+

$ ./mvnw test -P itest

