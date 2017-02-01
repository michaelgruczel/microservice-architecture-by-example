# cloud native by example

Foreword:
 
This demo/showcase should show how some cloud natives principles can be applied in java with several
PAAS and CAAS solutions. Don't take it to serious, it's just for me to learn.

---
 
## pre-requisites
 
 I expect the following packages installed locally:
 
 * virtualbox
 * jdk 8
 * vagrant
 * git client

---
 
## setups for using PCF, kubernetes, mesos, ...
 
 This installations are optional, but if you want to test the things hands-on you can find
 installations here how to install it.
 It's mainly based on local virtual machines, means you can delete it afterwards
 I will try to offer a local installation and a remote one, because depending on you
 memory and OS i can be difficult to start things locally
 
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

---

## demo applications

For this demo we have 3 applications:

* Weather service returns weather data for a town
* Concert service returns concerts which are available in a town, 
this service retrieves additional wether data for that town
* Chat service, This is an authorized service (session) which allows
logged in people to send message. Additionally they can ask for wether data
or concert data which is then retrieved from the other services

![chat-server](https://raw.githubusercontent.com/michaelgruczel/microservice-architecture-by-example/master/images/chat-server.PNG "chat-server")

The upper diagram shows the general setup.
But we want to follow the principles of cloud native apps, 
so the real setup will look like the one at the bottom:

![architecture](https://raw.githubusercontent.com/michaelgruczel/microservice-architecture-by-example/master/images/app.png "architecture")


Go through the principles to understand the adaptions and advantages of it 

---

## some principles of microservice architectures and how they are applied in the several environments
 
This principles you will find in the 12 factor (https://12factor.net/) apps, 
but not all factors are crucial for microservices and for this showcase (even if I agree with all 12 principles).
So my list is a subset.

### Backing services
Treat backing services as attached resources

This can be applied in Java by good design and by circuit breaker. One easy to use example library
is netflix hystrix. An app show try as good as possible to survive even if some dependencies
might fail temporarily. Enusure a gracefull degradation as possible. In this example the chat-server
will continue to work if services like the concerts-service or the weather service fail. 
Apart from that yozu should design your app in a way that additional resources (services) can be added
and removed on the fly. This can be done by software side loadbalancing in combination with service discovery.
In this example i will use eureka and ribbon for it.

### Processes
Execute the app as one or more stateless processes

Sessions are dangerous, sticky sessions are the result. This makes rolling updates and scaling complex.
In this example i will store session in a redis db.

### Logs
Treat logs as event streams

Since you want the flexibility to scale up and down easily and to moive your services, you
can not rely on the existence of files. Handling logs as streams is an easy solution.
I will show it with PCF and ELK.

### Port binding
Export services via port binding

Having dedicated ports might make sense if you only have a few services, but if you want to be flexible
you should automate the handling of ports.

---

## Let's run it locally

I recommend to use 5 shells.

shell 1 - let's start a eureka instance

    $ cd local-example
    $ cd eureka
    $ gradlew bootRun
    // check the ui on http://localhost:8761/

shell 2 - let's start the weather service

    $ cd local-example
    $ cd weather-server
    $ gradlew bootRun
    // you should now see the instance in eureka
    // you can now curl weather data by curl http://localhost:8090/weather?place=springfield
    
shell 3 - let's start the concert service

    $ cd local-example
    $ cd concert-server
    $ gradlew bootRun
    // you should now see the instance in eureka
    // and the concert server will find the weather service by the service discovery
    // you can now curl concert data by curl http://localhost:8100/concerts?place=springfield
        
shell 4 - let' start a redis server for the chat server:

    $ cd redis
    $ vagrant up
    
shell 5 - let' start one instance of the chat server:

    $ cd local-example
    $ cd chat-server
    $ gradlew bootRun
    // you should now see the instance in eureka
    // and the chat server will find the weather service and the concert service
    // by the service discovery (eureka)
    // you can now login by http://localhost:8080 with the credentials
    // (user/password) homer/beer, march/blue, bart/eatmyshorts

Let's play

sessions:

the first thing to realize is that the sessions of the chat server are stored in a redis
database, all we needed for this was a single small class HttpSessionConfig:

    @EnableRedisHttpSession
    public class HttpSessionConfig {
    }

and a dependency:

    compile	"org.springframework.boot:spring-boot-starter-data-redis:1.4.3.RELEASE"
	
by default it expects a redis to reach at localhost. This can be adapted of course,
but we will see later (PCF example or kubernetes how a redis database can be linked.
Storing sessions in a database makes webservices stateless. For rest services which need
authorisation I recommend oauth2 with JWT.
For the local example that's fine. The messages are store in redis as well.

circuit breaker:

Let's now login in to http://localhost:8080 with user homer and password beer.
Let's now retrieve concert information by submitting "/concerts springfield" in the message box.
This information is retrieved from the concert service, who retireves it from the weather server.
In case the weather server is not reachbale, hystrix will get to a fallback method.
The same is true if the response is not valied (not a 200 http code).
So stop the weather server and see what happens if you enter "/concerts springfield"
The circuit breaker in the concert service looks like this:

    @RequestMapping("/concerts")
    @HystrixCommand(fallbackMethod = "responseWithoutWeather")
    public ConcertInfo weather(@RequestParam(value="place", defaultValue="") String place) {

            String weatherData = "";
            if(!place.isEmpty()) {
                Weather weather = restTemplate.getForObject("http://weather-service/weather?place=" + place, Weather.class);
                weatherData = " - Weather:" + weather.getContent();
            }
            return new ConcertInfo(fakeConcertData(place) + weatherData);
    }

    public ConcertInfo responseWithoutWeather(@RequestParam(value="place", defaultValue="") String place) {
        return new ConcertInfo(fakeConcertData(place) + " - Weather: no data at the moment");
    }

after starting the weather service again, the weather data is available again.

service discovery:

Instead of defining the urls directly, we use eureka as service discovery.
The services have a name, and an embedded loadbalancing (ribbon) balances between all
instances of the given type.


    public class IndexController {

	    @Autowired
	    private DiscoveryClient discoveryClient;

	    @LoadBalanced
	    @Bean
	    RestTemplate restTemplate(){
		    return new RestTemplate();
	    }

	    @Autowired
	    RestTemplate restTemplate;
      
	    ...
	    
	    private void retrieveWeatherData(@ModelAttribute Message message) {

		    //not http://localhost:8090/weather?place=... instead
		    Weather weather = restTemplate.getForObject("http://weather-service/weather?place=" + message.getContent().split(" ")[1], Weather.class);
		    message.setContent(message.getContent() + " - Weather:" + weather.getContent());
	    }
	    
	    ...
	    
    }  

For the local test, we will stop here, if you want to see examples
of scaling the chat-service, or logging as stream, let's do it with PCF because it's just simpler

---

## Let's run it in PCF 

I assume that you run PCF locally, adapt the login command if you want to do it
in a remote PCF installation. We use the apps in the pcf-example folder, which are
more or less identical. I've just added manifest files, and in some cases a property.

    $ cf login -a https://api.local.pcfdev.io --skip-ssl-validation
    // credentials: user/pass
    $ cd pcf-example

Let's start eureka and offer it to apps as service

    $ cd eureka
    $ gradlew build
    $ cf push
    // now reachable under http://eureka.local.pcfdev.io/
    $ cf cups eureka-service -p '{"uri":"http://eureka:changeme@eureka.local.pcfdev.io"}'
    // on windows it can be that you have to use strange parameter escaping, do it interactive if needed
    // $ cf cups eureka-service -p uri
    // you can see erros by cf logs weather-service --recent
    // env by cf env weather-service

    // $ cf cups eureka-service -p {"uri":"http://eureka:changeme@eureka.local.pcfdev.io\"}
    
 Let's start the weather service and the concert service and bind it to eureka   

    $ cd..
    $ cd weather-server
    $ gradlew build
    $ cf push
    $ cf bind-service weather-service eureka-service
    $ cf restage weather-service
    $ cd..
    $ cd concert-server
    $ gradlew build
    $ cf push
    $ cf bind-service concerts-service eureka-service
    $ cf restage concerts-service

now the weather service runs in PCF and the concerts service as well. 
You can call it (see url in PCF UI https://console.local.pcfdev.io). 
PCF organizes the port binding, means everything is reachable on default http ports. 
Same for concerts-service. It find the weather service as well

     curl http://weather-service-.....local.pcfdev.io/weather?place=springfield
     curl http://concerts-service-....local.pcfdev.io/concerts?place=springfield

Lets scale the weather service     

    $ cf apps
    $ cf scale weather-service -i 2

PCF is doing loadbalancing if the url is used.

Let's use the Redis Database from the PCF marketplace and start 2 instances of the chat service against it and against eureka
 
    $ cd..
    $ cd chat-server
    $ gradlew build
    $ cf push
    $ cf marketplace
    $ cf create-service p-redis shared-vm redis
    $ cf bind-service chat-service redis
    $ cf bind-service chat-service eureka-service
    $ cf set-env chat-service JBP_CONFIG_OPEN_JDK_JRE "[memory_calculator: {memory_sizes: {metaspace: '128m'}}]"
    $ cf restage chat-service

Let's do an update without downtime (this plugin might not work on windows)

    $ cf add-plugin-repo CF-Community https://plugins.cloudfoundry.org
    $ cf install-plugin blue-green-deploy -r CF-Community
    $ cf blue-green-deploy chat-server
    $ cf delete chat-server-old

 lets now stream the logs out of PCF

    $ cd..
    $ cd..
    $ cd ELK
    $ vagrant up
    $ cf cups logstash-drain -l syslog://192.168.33.10:5000
    $ cf bind-service weather-service logstash-drain
    $ cf restage weather-service   
    //logs from weather service should now be reachable at 
    //http://localhost:5600/kibana/index.html#/dashboard/file/logstash.json
    //http://192.168.33.10:9200

![elk](https://raw.githubusercontent.com/michaelgruczel/microservice-architecture-by-example/master/images/elk.PNG "elk")


---

## Let's run it on kubernetes

> TODO not implemented yet

---

## Let's run it on docker swarm

> TODO not implemented yet

---

## Let's run it on mesos

> TODO not implemented yet

