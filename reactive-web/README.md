# Demystifying Blocking vs. Non-Blocking Web Requests

This corresponds to the medium post [here](https://medium.com/p/ef95ca9f02b7/edit)

## Run

- Run the following for JFR,
```
java -Dserver.port=8081 -XX:StartFlightRecording=duration=200s,filename=flight.jfr -jar .\target\reactive-web.jar
```

- Run with glowroot,
```
java -javaagent:C:\Users\suriy\main\Softwares\Installed\glowroot-0.14.2-dist\glowroot\glowroot.jar -jar .\target\reactive-web.jar
```

- With both,
```
java -javaagent:C:\Users\suriy\main\Softwares\Installed\glowroot-0.14.2-dist\glowroot\glowroot.jar -XX:StartFlightRecording=duration=60s,filename=flight.jfr -jar .\target\reactive-web.jar
```

## Load with Gatling

```
mvnw gatling:test
```

## How to test

### Servlet Tomcat vs Reactor Netty

- The project has client and server controller
- Run the client at 8080 and server at 8081
- Make the browser call to the client APIs - which inturn calls the server API
- Comment the desired Web container (tomcat/netty) based on your testing to test the __bio__ and __nio__

### Hugefile

- Run the application with GLOWROOT, JFR
- Run the gatling sim
- Flip the **simulationClass** in the pom between *bio* and *nio* 
- Check against /stream vs /nio call in the File IO and Socket IO - and check **Transaction** graph in **glowroot**

#### Observation

##### BIO stream

- Inverted parallelism - more GC parallel thread waited for IO operation or CPU resulting. Refer [hugefile bio stream jfr](./docs/hugefile/hugefile-bio-stream-over-30-sec.jfr)
- Method profiling - OutputStream buffer write lock, probably due to writing 1 byte at a time
- The Read IO and Socket IO - could tell a little bit about how much is read at a particular snapshot - but it does not tell the whole pic since the profiler takes snapshots at certain time based on the config

## TODO: Enhancement from here

- Test file disk read and write
- Test security configuration
- Update the docker compose - and use watcher


## OLD MISC

### Tested

- If you remove **spring-boot-starter-web** from pom (ie. tomcat) and just have **webflux netty** _client/bio_ calls fail throwing
```
java.lang.IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-epoll-2
```
- Tomcat web - does not stream event message one by one to the browser
- Netty streams one by one

### TODO

- Need to read the text stream from the client's web client - tats still failing
- **reactor-http-epoll-9** thread is waits until the current API finishes - need to test it with large volume of
  request, so that we can see if the same thread is being used in two different places. try replacing thread.sleep as
  well.
- Investigate on how non-blocking works in netty even though it feels like a blocking call (EVENT LOOP)


