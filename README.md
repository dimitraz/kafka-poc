# Apache Kafka 
Simple Kafka Consumer and Producer for testing purposes.
Make sure to modify `bootsrap.servers` in the `consumer.props` and `producer.props` with the location of your server(s) if they are different. 

## Getting started with Docker
### Linux
First run the Zookeeper container:

```
$ docker run -d --name zookeeper --network kafka-net zookeeper:3.4
$ docker run -d --name kafka --network kafka-net -p 9092:9092 --env ZOOKEEPER_IP=zookeeper ches/kafka
```

### Mac
Establishing a connection between a container and a host service with Docker for Mac is slightly more convoluted. To get going, first run the Zookeeper container:

```
$ docker run -d --name zookeeper --network kafka-net zookeeper:3.4
$ docker run -d --name kafka --network kafka-net -p 9092:9092 --env ZOOKEEPER_IP=zookeeper ches/kafka
```

Get the container's IP address:

```
$ CONTAINER_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' kafka)
$ echo $CONTAINER_IP 
```
Add this IP to the `loopback 0` interface: 
```
$ sudo ifconfig lo0 alias $CONTAINER_IP
```

And finally connect to the broker using this address by updating the `bootstrap.servers` in the the `consumer.props` and `producer.props`.

______ 

**Note**: If you prefer to use your host's IP address (`ifconfig en0 | grep "inet " | cut -d " " -f2`):
1. Replace `zookeeper` in the `ZOOKEEPER_IP` env var with your own IP address.
2. Add the env variable `--env KAFKA_ADVERTISED_HOST_NAME=<your_host_ip>` to the second docker run statement
3. Connect to the Kafka broker by using your host ip (update the `bootstrap.servers` accordingly)

## Running the Producer

The Java application should create the topic `taytochips`, or whatever topics you choose to replace that with. To confirm:

```
$ docker run --rm --network kafka-net ches/kafka kafka-topics.sh --list --zookeeper zookeeper:2181
```

Start a consumer from within the container to verify if the messages from the producer are coming through:

```
$ docker run --rm --network kafka-net ches/kafka kafka-console-consumer.sh --topic taytochips --from-beginning --bootstrap-server kafka:9092
```

## To Do
- Getting started with Openshift