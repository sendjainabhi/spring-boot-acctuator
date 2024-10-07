
## Deployment guide of Spring Boot Application Deployment on TKGs and Discovery of existing spring app on Tanzu Platform-K8(TPK8)

This guide walks you through the process of deploying a Spring Boot application on TKGs(k8) cluster and then discovery of your spring apps on Tanzu Platform-K8(TPK8).

### Pre-requisites/Environment configuration used in this guide 
  - Tanzu Platform SM Installed in your lab.
  - TKGs workload cluster with load balancer capabilities installed.
  - Image Registry (like Harbor etc) installed.
  - Should have internet access or app dependencies repo(Jfrog etc) configured for airgap environment.

## Deployment Steps 

- Apply changes in your app source code to make it discoverable in Tanzu platform on K8. Please refer [spring app changes here](Springapp-on-TP.md).
 

### Run Spring actuator on local - 
 Use command `./mvnw spring-boot:run` to run app on local using maven run.

### Endpoints -

App Endpoints - 
- http://localhost:8080/home
- http://localhost:8080/example

Actuator Endpoints -
- http://localhost:8080/actuator/env
- http://localhost:8080/actuator/health
- http://localhost:8080/actuator/info

### Build app image using `tanzu build`. Follow below steps - 

- Set tanzu registry for container app  (ghcr)
 `tanzu build config --containerapp-registry ghcr.io/sendjainabhi/spring-actuator`

- Do the Docker login for app image push on ghcr or your own image registry
`docker login --username <user login> --password <password> ghcr.io` 

- Tanzu app Initialization 
 `tanzu app init`   
  
  - App name - `spring-boot-actuator` , Source code dir `.` , Buildpack `select buildpack and enter`  

- Tanzu build and push app image command from local directory
`tanzu build --output-dir dev`

**Note** - Please make your image registry repo to public.

### Prebuilt app image to use for testing - 

`docker pull ghcr.io/sendjainabhi/spring-actuator:sha256-c9eaf0be142dca4a65be5b353a189fa45da2cec3373e4b5c82d10f35a2a0d6cb.imgpkg`

### Deploy app on K8 cluster
 - Create NS `spring-actuator`on your k8 cluster
  
   `kubectl create ns spring-actuator`

 - Deploy spring actuator deployment yaml on your k8 cluster.
  
   `kubectl apply -f spring-actuator-deployment.yaml -n spring-actuator`

 - Deploy spring actuator service yaml on your k8 cluster.
  
   `kubectl apply -f spring-actuator-service.yaml -n spring-actuator`

### Discover Spring Actuator NS and app on Tanzu platform   
  After 6 hours of deploying app on K8 cluster , Tanzu platform will discover app and you can see the NS `spring-actuator` and app `spring-actuator` in Tanzu Platform -  **Applications -> Apps and Microservice** .

### Delete app from K8 cluster

`Kubectl delete all -n spring-actuator`
