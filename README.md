# Feign Stub Server

Feign can be used to stub out any system you integrate with via HTTP(S). It is born out of the frustration
that none of the other mock servers provide clean separation between the core features and execution.

## Technologies Used
   * [Spring Boot](http://projects.spring.io/spring-boot)
   * [Spring HATEOAS](http://projects.spring.io/spring-hateoas)
   * [Spring Data Rest](http://projects.spring.io/spring-data-rest)
   * [Spring Data MongoDB](http://projects.spring.io/spring-data-mongodb) (optional)
   * Embedded MongoDB (optional)

## Core Features
   * **Declarative request/response mapping via YAML**. Following is a sample complete with all properties.
   All the properties are optional but usually you will want to keep the `path` and `method`.

            spring:
                profiles: p1
            feign:
              matchers.disable: false
              mappings:
                -
                  request:
                    path:
                      uri: /feign/abc
                      ignoreCase: false
                    method:
                      name: GET
                      ignoreCase: false
                    queries:
                      ignoreUnknown: true
                      ignoreEmpty: true
                      ignoreCase: false
                      pairs:
                        q1: [a, b]
                        q2: [c]
                    headers:
                      ignoreUnknown: true
                      ignoreEmpty: true
                      ignoreCase: false
                      pairs:
                        h1: h1
                        h2: h2
                    body:
                      ignoreUnknown: true
                      ignoreEmpty: true
                      ignoreCase: false
                      # can specify one of 'raw', 'url', or 'classpath'
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

            ---

            spring:
                   profiles: p2

   * **Full regex support**: Mapping of requests to responses is only limited by your regex skills.
   * **Flexible content matching**: Match request body and return response based on hard-coded text,
   url resource (including file system URL), or classpath resource.
   * **Recording of requests**: The request are saved in a backend data store
   and served on a platter using Spring Data REST. Just include the module `feign-persistence`
   in your build file. Feign ships with an embedded MongoDB but you can easily swap it out
   with another persistence of your choice.

      Example of retrieving recorded request using id:

         curl -H "Accept: application/hal+json" -X GET "http://localhost:63440/requests/1"

      Response:

         {
            "path": "/feign/xyz",
            "method": "GET",
            "queryParams": {},
            "headers": {
                "x-request-id": "1",
                "host": "localhost:63440",
                "connection": "keep-alive",
                "user-agent": "Java/1.8.0_66",
                "accept": "text/plain, application/json, application/*+json, */*"
            },
            "body": "",
            "_links": {
                "self": {
                    "href": "http://localhost:63440/requests/1"
                },
                "mongoDbRecordingRequest": {
                    "href": "http://localhost:63440/requests/1"
                }
            }
         }

   * **Extensive test coverage**: You know you can trust code when you see close to hundred tests in a codebase this small.

   * **You are in charge**: I am not going to force a web server down your throat. No more tightly coupled code
   or reinventing the wheel. You get Feign in a jar, and you decide how you want to run your application.
   In the simplest case, create a `SpringBootApplication` class, throw in an `application.yml` for good measure,
   and call it good. Moving to Spring Cloud? Me too!
   Create a Spring Cloud application, register with discovery and then order a Martini, shaken, not stirred.

This is work in progress, so expect to see changes. You are welcome to send pull requests, ask questions,
or create issues. Just do not expect me to complete your assignment for you.

## Customizations

   * Disable default matchers: Set the property `feign.matchers.disable: true` in the `application.yml`.
     This disables all default matchers that ship with Feign, but that does not mean you cannot pick and choose.
     To provide a custom path matcher but to keep the others, disable all the matchers as stated above,
     and then create beans for the ones you want to keep.
     For example, to enable the `DefaultMethodMatcher`, put the following in a `@Configuration` class.

         @Bean
         DefaultMethodMatcher defaultMethodMatcher() {
             return new DefaultMethodMatcher();
         }

        Same can be done for all the default matchers in the `feign-core` module.

   * Provide custom matcher: Create a bean that implements `BiFunction<Request, FeignMapping, Boolean>`. It will
   be added to the list of matchers.

   * Provide custom id generator: Feign ships with a default id generator that first looks in the request for a
   header named `x-request-id`. If found, it uses that value as the id for saving the request. If not, it generates
   a random UUID.
   To provide a custom id generator, create a bean that implements `IdGenerator`. The default one will be
   automatically disabled.

   * Match even parts of the image have no corresponding properties in the Feign mapping: Set `ignoreUnknown`
   true for properties shown in the above `application.yml`.

   * Match even if parts of the request are empty but corresponding properties in the Feign mapping are not:
   Set `ignoreEmpty` true for properties shown in the above `application.yml`.

   * Match ignoring case: Set `ignoreCase` true for properties shown in the above `application.yml`.

   * Disable recording: Set the property `feign.recording.disable: true` in the `application.yml`. Currently, there is
   no support for disabling recording for certain requests, but you can use Spring profiles to somewhat achieve this.
   You can also disable recording globally and then provide your own recording service with whatever
   filtering logic you need.

   * Use a different data store for storing recorded requests: If you are using Spring Data, you need a domain class,
   an id generator, a request repository and a recording service. Look in the `feign-persistence` module for
   a MongoDB version of these.
   If you are not using Spring Data, you can hand roll your persistence logic. Trouble is, Spring Data REST
   depends on Spring Data and thus, without it, you will have to write all the code for the REST endpoints to manage your
   request data store. Seriously, unless you are paid by hour, do not do it.

> I don't know who you're but before you steal my code and use it for a raise at work, you may want to look
into the licensing terms solely meant to keep jerks away.



