# Feign

Feign can be used to stub out any system you integrate with via HTTP(S). It is a non-blocking, reactive 
application.

## Technologies Used
   * [Play Framework](https://www.playframework.com/)(for Scala)
   * [Reactive Mongo](http://reactivemongo.org/)
   * [Akka](http://akka.io/?_ga=1.236653765.530527831.1478331011)
   * [Cats](https://github.com/typelevel/cats)
   * [Specs2 Test Framework](http://etorreborre.github.io/specs2/)
   * MongoDB
   * Docker

## Getting Started
```
$ docker run --name feign -p 27017:27017 -d mongo

$ bin/activator run -DFEIGN_DATABASE_HOST=<IP>

$ sbt -jvm-debug 9999 run

$ bin/activator -jvm-debug 9999 "run 9000"

```

### Heroku

URL: https://feign.herokuapp.com/feign

Running Locally: `$ heroku local web -f Procfile.local -p 9000`

Viewing Logs: `heroku logs -t -a feign`


## Core Features
   * **Declarative request/response mapping via YAML**. Feign loads it's configuration from a YAML file located at
     the value of `FEIGN_PROPERTIES_URL` environment variable.
     Following is a sample file.
   
     All the properties are optional but usually you will want to keep the `path` and `method`.
   
            feign: 
              delay: 
                delayMillis: 1000
                delayStrategy: WITH_LINEAR_BACK_OFF
              ignoreCase: true
              ignoreEmpty: false
              ignoreUnknown: false
              mappings: 
                - 
                  request: 
                    body: 
                      ignoreCase: true
                      ignoreEmpty: false
                      ignoreUnknown: false
                      raw: body
                    headers: 
                      ignoreCase: true
                      ignoreEmpty: false
                      ignoreUnknown: false
                      pairs: 
                        h1: a
                        h2: b
                    method: 
                      ignoreCase: false
                      name: GET
                    path: 
                      ignoreCase: false
                      uri: /feign/abc
                    queries: 
                      ignoreCase: true
                      ignoreEmpty: false
                      ignoreUnknown: false
                      pairs: 
                        q1: 
                          - a
                          - b
                        q2: 
                          - c
                  responses: 
                    - 
                      body: 
                        raw: body
                      headers: 
                        pairs: 
                          h3: c
                          h4: d
                      status: 201
                    - 
                      status: 500
                - 
                  request: 
                    path: 
                      uri: /feign/xyz
              matchers: org.abhijitsarkar.feign.domain.EmptyRequestMatchers
              recording: 
                disable: false
                idGenerator: org.abhijitsarkar.feign.domain.ConstantIdGenerator
              retry: 
                maxRetryCount: 2
                retryStrategy: ALWAYS


   * **Full regex support**: Mapping of requests to responses is only limited by your regex skills.
   * **Flexible content matching**: Match request body and return response based on hard-coded text,
     url resource (including file system URL), or classpath resource.
   * **Delaying of response**: Want to simulate network delays or long-running I/O?  Set a `delay` option.
   * **Failing a request**: It is possible to fail a request after a certain number of times by setting `retry` properties.
   * **Series of responses**: Specify multiple responses and Feign will loop through them in order. The series
     restarts from the beginning once exhausted.
   * **Recording of requests**: The request are saved in a backend data store (currently MongoDB). Finding and deleting 
     a request by id is supported, as is finding and deleting all requests. 
     Updating an existing request is not supported by design.
     
     > Note that currently paging is not supported so only the first 100 requests are returned for find all.
   
      Example of making a request:
      
         [abhijit_sarkar@feign]$ curl -v -H "h1: a" -H "h2: b" "http://localhost:9000/abc?q1=a&q1=b&q2=c"
         
         * Connected to localhost (127.0.0.1) port 9000 (#0)
         > GET /abc?q1=a&q1=b&q2=c HTTP/1.1
         > Host: localhost:9000
         > User-Agent: curl/7.43.0
         > Accept: */*
         > h1: a
         > h2: b
         > 
         < HTTP/1.1 201 Created
         < h3: c
         < h4: d
         < Content-Length: 4
         < Content-Type: application/json
         < Date: Wed, 09 Nov 2016 08:04:02 GMT
         * Connection #0 to host localhost left intact
         body

      Example of finding request using id:

         [abhijit_sarkar@feign]$ curl -X GET "http://localhost:9000/feign/requests/abc-1496531"

         // Only response body shown for brevity
         {"path":"/abc","method":"GET","queries":{"q1":["a","b"],"q2":["c"]}

      Example of searching for all recorded requests:

         [abhijit_sarkar@feign]$ curl -X GET "http://localhost:9000/feign/requests"

         // Only response body shown for brevity
         [{"path":"/abc","method":"GET","queries":{"q1":["a","b"],"q2":["c"]}]
         
      Delete works similarly, except the HTTP verb must be specified as `DELETE`.

## Customizations

> Note: If both global and local customizations are present, the later wins.

   * Add custom request matchers: Create one or more matchers by
     implementing `org.abhijitsarkar.feign.api.matcher.RequestMatcher`, then 
     implement `org.abhijitsarkar.feign.api.matcher.RequestMatchers` to return your matchers from the `getMatchers` method 
     and finally set the property `feign.matchers` to the fully-qualified classname.
     Note that doing so will disable all default matchers that ship with Feign.

   * Provide custom id generator: Feign ships with a default `IdGenerator` that generates an id according to the
     following logic:

     Calculate the URI hash and append a `{prefix}-` to it. The prefix is generated as follows:
     * Extract the first segment of the URI without any slash.
     For example, given the path `/feign/xyz`, the generated id is `feign-1357738284`.
     * If failed to extract the first segment of the URI, use `unknown`.

     To replace the default id generator, implement `org.abhijitsarkar.feign.api.persistence.IdGenerator` and 
     set `feign.recording.idGenerator` to the fully-qualified classname.

   * Match even parts if the request has no corresponding properties in the Feign mapping: Set `ignoreUnknown`
     true for properties shown above.

   * Match even if parts of the request are empty but corresponding properties in the Feign mapping are not:
     Set `ignoreEmpty` true for properties shown bove.

   * Match ignoring case: Set `ignoreCase` true for properties shownabove.

   * Disable recording: Set the property `feign.recording.disable: true` to disable recording
     for all requests.
   
   * Use a different persistence implementation: This is possible although it takes some work. 
     Set the property `feign.recording.recordingService` to an Akka `Actor` implementation. It will receive requests in
     the form of `*Request` classes in the package `org.abhijitsarkar.feign.api.persistence`.
   
## Contribute

Feign is a volunteer effort. You are welcome to send pull requests, ask questions, or create issues.
If you use it and you like it, you can help by spreading the word!

## License

Copyright 2015-2016 Abhijit Sarkar - Released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).

## References

[Reactive Mongo](http://reactivemongo.org/releases/0.12/documentation/tutorial/write-documents.html)

> Watch the version in the URL. Like most open source, their API changes quite frequently.

[Sample Application](https://github.com/jonasanso/play-reactive-mongo-db)





