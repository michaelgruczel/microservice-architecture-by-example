# microservice-architecture-by-example

> Warning: this is not yet usable

## Foreword
 
 TODO
 
## pre-requisites
 
 I expect the following packages installed locally:
 
 * virtualbox
 * jdk 8
 * vagrant
 * git client
 
## setups
 
 This installations are optional, but if you want to test the things hands-on you can find
 installations here how to install it.
 It's mainly based on local virtual machines, means you can delete it afterwards
 
### local docker swarm cluster
 
A simple way is to start some virtual machines, install docker on them.
Then start on one vm a master container and join with the other vms.
You could use for example

    $ cd docker-swarm
    $ vagrant up swarm-master
    $ vagrant up swarm-node-1
    $ vagrant up swarm-node-2
    $ vagrant up swarm-node-3
 
### local kubernetes (cluster) for Mac or Linux
 
We can use minikube (https://github.com/kubernetes/minikube) to run kubernetes locally for linux and MacOS
check the release you want here : https://github.com/kubernetes/minikube/releases, for 0.15 it would be

osX
 
    curl -Lo minikube https://storage.googleapis.com/minikube/releases/v0.15.0/minikube-darwin-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/
    $ minikube start
    Starting local Kubernetes cluster...
     
linux
 
    curl -Lo minikube https://storage.googleapis.com/minikube/releases/v0.15.0/minikube-linux-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/
    $ minikube start
    Starting local Kubernetes cluster...
 
It's a single node installation, for a more realistic (and resource intensive) cluster you can use:
 
     # wget version
     export KUBERNETES_PROVIDER=vagrant; wget -q -O - https://get.k8s.io | bash

see desciption here:
 
https://kubernetes.io/docs/getting-started-guides/binary_release/#download-kubernetes-and-automatically-set-up-a-default-cluster
 
### kubernetes cluster on aws

If you are on windows, I would recommend to create a linux vm and execute the commands in
the vm:

    $ mkdir kubernetes-test-vm
    $ cd kubernetes-test-vm
    $ vagrant init hashicorp/precise64
    $ vagrant up 
    $ vagrant ssh
    $ export KUBERNETES_PROVIDER=aws; wget -q -O - https://get.k8s.io | bash
    $ cluster/kube-up.sh
    // you might need to configure your aws credentials 
    $ cluster/kube-down.sh

    
see https://kubernetes.io/docs/getting-started-guides/aws/ for more details    
 
### local pivotal cloud foundry
 
get a PCF image/appliance:

* install cli - https://github.com/cloudfoundry/cli#downloads 
* download appliance - https://network.pivotal.io/products/pcfdev
* unzip/untar archieve 

start it:

    ./pcfdev-...
    cf dev start -s all
    cf login -a https://api.local.pcfdev.io --skip-ssl-validation
    
after a very long running process you can enter the systems locally:

* Apps Manager URL: https://local.pcfdev.io
* Admin user => Email: admin / Password: admin
* Regular user => Email: user / Password: pass
* UI can be found under https://console.local.pcfdev.io or https://uaa.local.pcfdev.io/login
 
### local mesos (cluster)
 
a standalone version can be created by:

    $ git clone https://github.com/everpeace/vagrant-mesos.git
    $ cd vagrant-mesos/standalone
    $ vagrant up

URLs are:

* Mesos web UI on: http://192.168.33.10:5050
* Marathon web UI on: http://192.168.33.10:8080
* Chronos web UI on: http://192.168.33.10:8081
    
a multinode version by:

    $ git clone https://github.com/everpeace/vagrant-mesos.git
    $ cd vagrant-mesos/multinodes
    $ vagrant up    

URLs are:    

* Mesos web UI on: http://172.31.1.11:5050
* Marathon web UI on: http://172.31.3.11:8080
* Chronos web UI on: http://172.31.3.11:8081    

## demo applications

For this demo we have 3 applications:

* Weather service returns weather data for a town
* Concert service returns concerts which are available in a town, 
this service retrieves additional wether data for that town
* Chat service, This is an authorized service (session) which allows
logged in people to send message. Additionally they can ask for wether data
or concert data which is then retrieved from the other services

So the general setup will look lilke this:

> TODO

But we want to follow the principles of cloud native apps, 
so the real setup will look like this:

> TODO

Go through the principles to understant the adaptions and advantages of it 

## some principles of microservice architectures and how they are applied in the several environments
 
Some of this principles you will find in the 12 factor (https://12factor.net/) apps, 
but not all factors are crucial for microservices and for this showcase (even if I agree with all 12 principles).
So my list differs for this demo. 

### resilience to traffic

> TODO scaling

### resilience to downtimes

> TODO example database, circuit breaker

### resilience to infrastructure changes

> TODO service discovery

### Be stateless 

> TODO example redis for session, loadbalancer round robin

### always on

> TODO example rolling updates, config changes
> TODO example config

### Treat logs as event streams

> TODO

### Let's run it locally

> TODO not implemented yet

I recommend to use 5 shells.

shell 1 - let's start a eureka instance

    $ cd eureka
    $ gradlew bootRun
    // see http://localhost:8761/

shell 2 - let's start the weather service

    $ cd weather-server
    $ gradlew bootRun
    // you should now see the instance in eureka
    // you can now curl weather data by curl http:/...../weather?place=springfield
    
shell 3 - let's start the concert service

    $ cd concert-server
    $ gradlew bootRun
    // you should now see the instance in eureka
    // and the concert server will find the weather service by the service discovery
    // you can now curl concert data by curl http:/...../concerts?place=springfield
        
shell 4 - let' start a redis server for the chat server:

    $ cd redis
    $ vagrant up
    
shell 5 - let' start one instance of the chat server:

    $ cd chat-server
    $ gradlew bootRun
    // you should now see the instance in eureka
    // and the chat server will find the weather service and the concert service
    // by the service discovery (eureka)
    // you can now login by http://localhost:8080 with the credentials
    // (user/password) homer/beer, march/blue, bart/eatmyshorts

Let's play

> TODO stop and restart the weather server
> TODO start a second instance of the weather server
> TODO stop and restart the concert server
> TODO stop the concert server and start a faulty version of it

For the local test, we will stop here, if you want to see examples
of scaling the chat-service, or logging as stream, let's do it with PCF

### Let's run it locally in PCF

> TODO not implemented yet

### Let's run it on kubernetes

> TODO not implemented yet

### Let's run it on docker swarm

> TODO not implemented yet

### Let's run it on mesos

> TODO not implemented yet

## appendix

redis:

    $ vagrant up
    $ vagrant ssh
    $ redis-cli ping
    PONG
    
https://github.com/steveswinsburg/mysql-vagrant/blob/master/install.sh


