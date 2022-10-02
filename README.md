# Introduction customer-profile
customer-profile provides you an out-of-the-box application setup to implement your business logic. It is based on the
Hexagonal Architecture (oversimplified, without _Port_ interfaces), where the packages `api` and `data` provide the 
integrations and the _adapters_, and the `domain` package contains business models and logic.

Features:
- Spring Boot with Java as a technology stack
- REST(ful) API using Spring Web annotations
- generation of the OpenAPI definition based on your code
- data persistence using Spring Data JPA with PostgreSQL
- [Testcontainers](https://www.testcontainers.org/) for integration and end-to-end tests
- Docker Compose for running PostgreSQL and RabbitMQ locally

The application contains example code implementing REST API to write and read customer profile information to and from 
database. This example is intended to showcase best practices around using Spring Boot and it's libraries as well as
different types of tests which can be utilized to verify different parts of an application.

## Prerequisites
In order to further develop this application the following tools needs to be setup:
- Java Development Kit (https://bell-sw.com/)
- Visual Studio Code or IntelliJ IDEA as Integrated Development Environment (IDE)
- Tanzu Developer Tools plugin for mentioned IDE
- Docker Desktop to execute integration tests or run the application locally

# Local
## Build
In order to compile the production code:
```bash
./mvnw clean compile
```


After that it is a good habit to compile the test classes and execute those tests to see if your application is still behaving as you would expect:
```bash
./mvnw verify
```


## Start and interact
Spring Boot has its own integrated Web Server (Apache Tomcat (https://tomcat.apache.org/)). In order 
to start the application a PostgreSQL instance should be running.

Running a PostgreSQL instance can easily be done by using `docker-compose`:
```bash
docker-compose up -d
```

Launch application using profile `local`:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```


### OpenApi Definition
You can access the API docs using `curl`:

```bash
curl http://localhost:8080/api-docs  
```

### Create customer profile

You can access the `customer-profiles` API endpoint using `curl`:

```bash
curl -X POST -H 'Content-Type: application/json' http://localhost:8080/api/customer-profiles/ -d '{"firstName": "Joe", "lastName": "Doe", "email": "joe.doe@test.org"}'
```

### Get customer profile
Use the `id` received by previous POST call.
```bash
curl -X GET http://localhost:8080/api/customer-profiles/{id}
```

# Deployment
## Tanzu Application Platform (TAP)
Using the `config/workload.yaml` it is possible to build, test and deploy this application onto a
Kubernetes cluster that is provisioned with Tanzu Application Platform (https://tanzu.vmware.com/application-platform).

PostgreSQL and RabbitMQ instances are available on the cluster. The application will need to bind to relevant services
using elements of the [Service Toolkit for Tanzu Application Platform](https://docs.vmware.com/en/Services-Toolkit-for-VMware-Tanzu-Application-Platform/index.html).
Refer to [Consume services on Tanzu Application Platform](https://docs.vmware.com/en/VMware-Tanzu-Application-Platform/1.2/tap/GUID-getting-started-consume-services.html)

### Tanzu CLI
Using the Tanzu CLI one could apply the workload using the local sources:
```bash
tanzu apps workload apply \
  --file config/workload.yaml \
  --namespace <namespace> --source-image <image-registry> \
  --local-path . \
  --yes \
  --tail
````

Note: change the namespace to where you would like to deploy this workload. Also define the (private) image registry you
are allowed to push the source-image, like: `docker.io/username/repository`.

### IDE Tanzu Plugin
When developing local but would like to deploy the local code to the cluster the Tanzu Plugin could help.
By using `Tanzu: Apply` on the `workload.yaml` it will create the Workload resource with the local source (pushed to an image registry) as
starting point.

# How to proceed from here?
Having the application locally running and deployed to a cluster you could add your domain logic, related persistence and new RESTful controller.
