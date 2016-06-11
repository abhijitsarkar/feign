# Feign Stub Server

Feign can be used to stub out any system you integrate with via HTTP(S). It is born out of the frustration
that none of the other mock servers provide clean separation between the core features and execution.

## Technologies Uses
   * [Spring Boot](http://projects.spring.io/spring-boot)
   * [Spring HATEOAS](http://projects.spring.io/spring-hateoas)
   * [Spring Data MongoDB](http://projects.spring.io/spring-data-mongodb)(optional)
   * Embedded MongoDB (optional)

## Core Features
   * Declarative request/response mapping via YAML: Example below but not all properties shown.
```
spring:
    profiles: p1
feign:
  mappings:
    -
      request:
        record: false
          idGenerator: name.abhijitsarkar.javaee.feign.model.DefaultIdGenerator
        path:
          uri: /feign/abc
        method:
          name: GET
        queries:
          matcher: name.abhijitsarkar.javaee.feign.matcher.QueriesMatcher
          ignoreUnknown: true
          ignoreEmpty: true
          pairs:
            q1: [a, b]
            q2: [c]
        headers:
          matcher: name.abhijitsarkar.javaee.feign.matcher.HeadersMatcher
          ignoreUnknown: true
          ignoreEmpty: true
          pairs:
            h1: h1
            h2: h2
        body:
          matcher: name.abhijitsarkar.javaee.feign.matcher.BodyMatcher
          raw: body
      response:
        status: 200
        headers:
          h3: h3
          h4: h4
    -
      request:
        path:
          uri: /feign/xyz
        headers:
          ignoreUnknown: true

---

spring:
       profiles: p2


```
   * Full regex supported: Mapping of requests to responses is only limited by your regex skill.
   * Ability to match request body and return response based on hard-coded text, url resource,
   classpath resource.
   * You are in charge: Want more control over the request/response mapping? No problem.
   Create your own mapper classes and configure in the `application.yml`.
   * Optional recording of requests: The request can be saved in a backend data store
   and served on a platter using Spring HATEOAS. By default, an embedded MongoDB module is provided
   but you can swap it out with any persistence of your choice.
   * Extensive test coverage.

   Example of retrieving recorded request using `curl`:
```
curl -H "Accept: application/hal+json" -X GET "http://localhost:port/requests"
```
Response:
```
{
  "_embedded" : {
    "requests" : [ {
      "path" : "/feign/xyz",
      "method" : "GET",
      "queryParams" : { },
      "headers" : {
        "host" : "localhost:52487",
        "connection" : "keep-alive",
        "user-agent" : "Java/1.8.0_66",
        "accept" : "text/plain, application/json, application/*+json, */*"
      },
      "body" : "",
      "_links" : {
        "self" : {
          "href" : "http://localhost:52487/requests/324e4f2b-7c84-4e4d-bf17-abe3d6864cbc"
        },
        "request" : {
          "href" : "http://localhost:52487/requests/324e4f2b-7c84-4e4d-bf17-abe3d6864cbc"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:52487/requests"
    },
    "profile" : {
      "href" : "http://localhost:52487/profile/requests"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }
}
```
   * You are still in charge: I am not going to force a web server down your throat. No more hodgepodge
   code or reinventing the wheel. You get feign in a jar, and you decide how you want to run your application.
   Like Spring Boot? Create a `SpringBootApplication` class or equivalent entry point,
   throw in an `application.yml` for good measure, and call it good. Moving to Spring Cloud? I am with you.
   Create a Spring Cloud application, register with discovery and then order a Martini, shaken, not stirred.

This is work in progress, so expect to see changes. You are welcome to send pull requests, ask questions,
or create issues. Just do not expect me to complete your assignment for you.

> I don't know who you're but before you steal my code and use it for a raise at work, you may want to look
into the licensing terms solely meant to keep jerks away.



