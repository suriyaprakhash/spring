### Tested

- Tomcat web - does not stream event message one by one to the browser
- Netty streams one by one

### TODO

- Need to read the text stream from the client's web client - tats still failing
- **reactor-http-epoll-9** thread is waits until the current API finishes - need to test it with large volume of
  request, so that we can see if the same thread is being used in two different places. try replacing thread.sleep as
  well.
- Investigate on how non-blocking works in netty even though it feels like a blocking call (EVENT LOOP)