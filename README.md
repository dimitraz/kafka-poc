# Apache Kafka 

Simple Kafka Consumer and Producer for testing purposes. I recommend running this on Linux if using Docker for Zookeeper and Kafka (as described below) as Docker for mac may produce unexpected results.

Make sure to modify `bootsrap.servers` in the `consumer.props` and `producer.props` with the location of your server(s) if they are different. 

## Getting started with Docker

First run the Zookeeper container:

```
docker run -d --name zookeeper --network kafka-net zookeeper:3.4
```

Then start the container for the Kafka broker, on port 9092:

```
docker run -d --name kafka --network kafka-net -p 9092:9092  --env ZOOKEEPER_IP=zookeeper ches/kafka
```

## Running the Producer

The Java application should create the topic `taytochips`, or whatever topics you choose to replace that with. To confirm:

```
docker run --rm --network kafka-net ches/kafka kafka-topics.sh --list --zookeeper zookeeper:2181
```

Start a consumer from within the container to verify if the messages from the producer are coming through:

```
docker run --rm --network kafka-net ches/kafka kafka-console-consumer.sh --topic taytochips --from-beginning --bootstrap-server kafka:9092
```

## To Do
- Getting started with Openshift