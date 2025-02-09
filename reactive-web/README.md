# Demystifying Blocking vs. Non-Blocking Web Requests

This corresponds to the medium post [here](https://medium.com/p/ef95ca9f02b7/edit)

## Run

Run the following for JFR
```
-Dserver.port=8081
-XX:StartFlightRecording=duration=200s,filename=flight.jfr
```

## Load with Gatling

```
mvnw gatling:test
```

## Load with K6 or ab

Make sure k6 is installed (WSL) 

**Note:** this runs only when java from WSL is used - you could not profile in IDEA since it does not see WSL java 

```
k6 run src/test/js/load.js
```

```
ab http://localhost:8081/hugefile/bio/stream
```

## How to test

- The project has client and server controller
- Run the client at 8080 and server at 8081
- Make the browser call to the client APIs - which inturn calls the server API
- Comment the desired Web container (tomcat/netty) based on your testing to test the __bio__ and __nio__

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


