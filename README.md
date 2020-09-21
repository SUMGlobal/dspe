# hwk (hello-world-kotlin)

This repo has been created to demonstrate a bug, that we believe to be in Micronaut. We have taken
the hello-world-kotlin example from the micronaut-gprc project and created separate client and server
services. Both services support Micronaut's logger (`/mgmt/logger`) and health (`/mgmt/health`) management endpoints.

hwk-server is a http and grpc server. It hosts the Greeter interface. 
hwk-client is a http server and grpc client. Both of it's http endpoints call the Greeter.SayHello grpc endpoint hosted by hwk-server:
   
    `/hello/{name}` uses a grpc client created by a Client Factory, utilizing @GrpcChannel("hwk-server")
    
    `/hola/{name}` uses a grpc client created by utilizing ManagedChannelBuilder.forTarget("hwk-server:8081")
    
In K8S without a service mesh, or with Consul as the service mesh, both methods work fine.
However, when the @GrpcChannel-based client is used in an Istio or Linkerd service mesh, the hwk-client sidecar (outbound) 
reports a HTTP2 protocol error when trying to call hwk-server.

### To build both servers:
`./gradlew assemble ` 

### Create/tag docker images:
(e.g. from hwk-server/ using a local container registry):

`docker build -t registry:5000/hwk-server:v1 .`

`docker push registry:5000/hwk-server:v1`

Do the same for hwk-client


### Deploy to a K8S cluster (default namespace where sidecar injection is enabled)

`kubectl apply -f ./k8s/auth.yaml`

`kubectl apply -f ./k8s/hwk-client.yaml`

`kubectl apply -f ./k8s/hwk-server.yaml`


### To test

hwk-client service is of type LoadBalancer. Get it's external IP: `kubectl get svc`

`curl http://$EXTERNAL_IP:8080/hello/George`

`curl http://$EXTERNAL_IP:8080/hola/George`

    
