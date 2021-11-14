# GPN-Intelligence-Cup
This web-application is a **REST-Adapter** for a **SOAP-Service**.

**REST** requests converted and passed to the **SOAP-Service**. The received response also converted and the response obtained already in **JSON** format.

### Adapter Features
* Request **caching** was implemented
* Provides for the **validation of requests** for the correctness of data (Example, division by zero)

### Building
```
mvn clean install
```

### Running
```
mvn spring-boot:run
```
or
```
java -jar target/GPN-Intelligence-Cup-1.0.jar
```
### Rest API
Example of **POST** requests to the **Rest-Adapter**:
* Add
```
curl -H 'Content-Type:application/json' -d '{"intA": 2, "intB": 2}' 'localhost:8080/camel/api/add'
```
* Divide
```
curl -H 'Content-Type:application/json' -d '{"intA": 10, "intB": 2}' 'localhost:8080/camel/api/divide'
```
* Multiply
```
curl -H 'Content-Type:application/json' -d '{"intA": 15, "intB": 15}' 'localhost:8080/camel/api/multiply'
```
* Subtract
```
curl -H 'Content-Type:application/json' -d '{"intA": 500, "intB": 200}' 'localhost:8080/camel/api/subtract'
```
On the output we get a file in JSON format
### Swagger, OpenAPI
In order to see the specification, you need to go to the following address
```
http://localhost:8080/camel/api-doc
```