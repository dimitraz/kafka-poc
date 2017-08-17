package org.aerogear.gsoc.kafkapoc;
import com.google.common.io.Resources;

import org.aerogear.gsoc.kafkapoc.model.Tweet;
import org.aerogear.gsoc.kafkapoc.util.GenericDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

/**
 * Simple Kafka Consumer class to send messages to a given
 * topic
 *
 * @author Dimitra Zuccarelli
 */
public class Consumer {

    private final static String INPUT_TOPIC = "kafka-quotes";

    public static void main(String[] args) throws IOException {
        Consumer consumer = new Consumer();

        // Subscribe to topics and start consuming
        consumer.startConsumer(INPUT_TOPIC);
    }

    private void startConsumer(String topic) throws IOException {
        KafkaConsumer<String, Tweet> consumer;

        // Load in consumer.props file
        try (InputStream props = Resources.getResource("consumer.props").openStream()) {
            Properties properties = new Properties();

            // Properties for custom deserialisers
            properties.put("key.deserializer", Serdes.String().deserializer().getClass());
            properties.put("value.deserializer", GenericDeserializer.class.getName());
            properties.put("value.deserializer.type", Tweet.class.getName());

            properties.load(props);
            if (properties.getProperty("group.id") == null) {
                properties.setProperty("group.id", "group-" + new Random().nextInt(100000));
            }
            consumer = new KafkaConsumer<>(properties);
        }

        // Subscribe to topics and start consuming
        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                ConsumerRecords<String, Tweet> records = consumer.poll(200);
                records.forEach((record) -> System.out.println(record.offset() + " " + record.value()));
            }
        } catch (Exception e) {
            // Handle later
        } finally {
            consumer.close();
        }
    }
}
