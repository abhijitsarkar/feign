# Feign Stub Server

Feign can be used to stub out any system you integrate with via HTTP(S). It is born out of the frustration
that none of the other mock servers provide clean separation between the core features and execution.

## Technologies Used
   * [Spring Boot](http://projects.spring.io/spring-boot)
   * [Spring HATEOAS](http://projects.spring.io/spring-hateoas)
   * [Spring Data Rest](http://projects.spring.io/spring-data-rest)
   * [Spring Data MongoDB](http://projects.spring.io/spring-data-mongodb) (optional)
   * [Embedded MongoDB](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) (optional)
   * [Spock Test Framework](http://spockframework.github.io/spock/docs/1.0/index.html)

## Core Features
   * **Declarative request/response mapping via YAML**. Following is a sample complete with all properties.
   All the properties are optional but usually you will want to keep the `path` and `method`.

            spring:
                profiles: p1
            feign:
              matchers:
                disable: false
              delay:
                delayMillis: 1000
                # One of 'CONSTANT', 'WITH_LINEAR_BACK_OFF', or 'WITH_EXPONENTIAL_BACK_OFF'
                delayStrategy: CONSTANT
              recording:
                disable: false
                idGenerator: name.abhijitsarkar.feign.persistence.DefaultIdGenerator
              ignoreUnknown: true
              ignoreEmpty: true
              ignoreCase: false
              mappings:
                -
                  request:
                    recording:
                      disable: false
                      idGenerator: my.custom.IdGenerator
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
                      # One of 'raw', 'url', or 'classpath'
                      raw: body
                  response:
                    -
                      status: 200
                      headers:
                        h3: h3
                        h4: h4
                      body:
                        # One of 'raw', 'url', or 'classpath'
                        raw: body
                      delay:
                        delayMillis: 5000
                        delayStrategy: CONSTANT
                    -
                      status: 500
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
   * **Delaying of response**: Want to simulate network delays or long-running I/O? Choose of the several
   delay options for all or individual responses.
   * **Series of responses**: Specify multiple responses and Feign will loop through them in order. The series
   restarts from the beginning once exhausted.
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

      Example of searching for all recorded requests:

         curl -H "Accept: application/hal+json" -X GET "http://localhost:63440/requests"

      Response:

          {
            "_embedded": {
                "requests": [{
                    "path": "/feign/xyz",
                    "method": "GET",
                    "queryParams": {},
                    "headers": {
                        "host": "localhost:55993",
                        "connection": "keep-alive",
                        "user-agent": "Java/1.8.0_66",
                        "accept": "text/plain, application/json, application/*+json, */*"
                    },
                    "body": "",
                    "_links": {
                        "self": {
                            "href": "http://localhost:55993/requests/feign-1357738284"
                        },
                        "request": {
                            "href": "http://localhost:55993/requests/feign-1357738284"
                        }
                    }
                }]
            },
            "_links": {
                "self": {
                    "href": "http://localhost:55993/requests"
                },
                "profile": {
                    "href": "http://localhost:55993/profile/requests"
                }
            },
            "page": {
                "size": 20,
                "totalElements": 1,
                "totalPages": 1,
                "number": 0
            }
          }

   * **Extensive test coverage**: You know you can trust code when you see close to hundred tests in a codebase this small.

   * **You are in charge**: I am not going to force a web server down your throat. No more tightly coupled code
   or reinventing the wheel. You get Feign in a jar, and you decide how you want to run your application.
   In the simplest case, create a `SpringBootApplication` class, throw in an `application.yml` for good measure,
   and call it good. Moving to Spring Cloud? Me too!
   Create a Spring Cloud application, register with discovery and then order a Martini, shaken, not stirred.

## Customizations

> Note: If both global and local customizations are present, the later wins.

   * Disable default matchers: Set the property `feign.matchers.disable: true` in the `application.yml`.
     This disables all default matchers that ship with Feign, but that does not mean you cannot pick and choose.
     For example, to disable the `DefaultMethodMatcher`, put the following in a `@Configuration` class.

         @Bean
         DefaultMethodMatcher defaultMethodMatcher() {
             return new DefaultMethodMatcher();
         }
        Same can be done for all the default matchers in the `feign-core` module.

   * Add custom matcher: Create a bean that implements `BiFunction<Request, FeignMapping, Boolean>`. It will
   be added to the list of matchers. For a mapping to be found, all matchers must match the request.

   * Provide custom id generator: Feign ships with a default `IdGenerator` that generates an id according to the
   following logic:

      Calculate the URI hash and append a `{prefix}-` to it. The prefix is generated as follows:
      * Extract the first segment of the URI without any slash.
      For example, given the path `/feign/xyz`, the generated id is `feign-1357738284`.
      * If failed to extract the first segment of the URI, use `unknown`.

     To replace the default id generator, set `feign.recording.idGenerator` as shown in the above `application.yml`.

     You can also specify id generators for individual requests as shown in the above `application.yml`.

   * Match even parts of the image have no corresponding properties in the Feign mapping: Set `ignoreUnknown`
   true for properties shown in the above `application.yml`.

   * Match even if parts of the request are empty but corresponding properties in the Feign mapping are not:
   Set `ignoreEmpty` true for properties shown in the above `application.yml`.

   * Match ignoring case: Set `ignoreCase` true for properties shown in the above `application.yml`.

   * Disable recording: Set the property `feign.recording.disable: true` in the `application.yml` to disable recording
   for all requests. You can also disable recording for individual requests as shown in the above `application.yml`.
   
   * Use a different data store for storing recorded requests: If you are using Spring Data, you need to implement
   a [Repository](http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/Repository.html) and a
   `RecordingService`. Look in the `feign-persistence` module for a MongoDB version of those.

      If you are not using Spring Data, you can hand roll your persistence logic. Trouble is, Spring Data REST
   depends on Spring Data and thus, without it, you will have to write all the code for the REST endpoints to manage your
   request data store. Seriously, unless you are paid by hour, do not do it.
   
   * Use a different delay strategy: The default delay strategy is `CONSTANT`, meaning the response is delayed by the 
   same amount every time. You can choose another from the enum `DelayStrategy`. The names are self-explanatory.  

## Contribute

Feign is a volunteer effort. You are welcome to send pull requests, ask questions, or create issues.
If you use it and you like it, you can help by spreading the word!

## License

Copyright 2015-2016 Abhijit Sarkar - Released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).



