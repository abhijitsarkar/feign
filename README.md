# Feign Stub Server

Feign can be used to stub out any system you integrate with via HTTP(S). It is born out of the frustration
that none of the other mock servers provide clean separation between the core features and execution.

## Technologies Uses
   * [Spring Boot](http://projects.spring.io/spring-boot)
   * [Spring HATEOAS](http://projects.spring.io/spring-hateoas)
   * [Spring Data Rest](http://projects.spring.io/spring-data-rest)
   * [Spring Data MongoDB](http://projects.spring.io/spring-data-mongodb) (optional)
   * Embedded MongoDB (optional)

## Core Features
   * **Declarative request/response mapping via YAML**. Following is a sample complete with all properties:

            spring:
                profiles: p1
            feign:
              # set false to disable all matchers
              matcher.enable: true
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
                      ignoreCase: true
                      pairs:
                        q1: [a, b]
                        q2: [c]
                    headers:
                      ignoreUnknown: true
                      ignoreEmpty: true
                      ignoreCase: true
                      pairs:
                        h1: h1
                        h2: h2
                    body:
                      ignoreUnknown: true
                      ignoreEmpty: true
                      ignoreCase: true
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
   * **You are in charge**: Want complete control over the request/response mapping? You got it!
   Just create your own mapper classes as Spring beans and they are in. If you want the default matchers
   to be disabled, set the property `feign.matcher.enable: false` in the `application.yml`.
   * **Optional recording of requests**: The request are saved in a backend data store
   and served on a platter using Spring Data REST. Just include the module `feign-persistence`
   in your build file. By default, an embedded MongoDB is used but you can easily swap it out
   with another persistence of your choice. Look into the `feign-persistence` to see how.
   * **Extensive test coverage**: You know you can trust code when you see more than hundreds of tests.

   Example of retrieving recorded request using id:
```
curl -H "Accept: application/hal+json" -X GET "http://localhost:63440/requests/1"
```
Response:
```
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
```
   * You are still in charge: I am not going to force a web server down your throat. No more tightly coupled code
   or reinventing the wheel. You get feign in a jar, and you decide how you want to run your application.
   In the simplest case, create a `SpringBootApplication` class, throw in an `application.yml` for good measure,
   and call it good. Moving to Spring Cloud? Me too!
   Create a Spring Cloud application, register with discovery and then order a Martini, shaken, not stirred.

This is work in progress, so expect to see changes. You are welcome to send pull requests, ask questions,
or create issues. Just do not expect me to complete your assignment for you.

> I don't know who you're but before you steal my code and use it for a raise at work, you may want to look
into the licensing terms solely meant to keep jerks away.



