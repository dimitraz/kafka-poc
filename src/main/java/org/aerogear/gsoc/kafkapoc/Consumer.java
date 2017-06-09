package org.aerogear.gsoc.kafkapoc;
import com.google.common.io.Resources;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

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
    // private String topic;

    public static void main(String[] args) throws IOException {
        Consumer consumer = new Consumer();

        // Subscribe to topics and start consuming
        consumer.startConsumer("taytochips");
    }

    private void startConsumer(String topic) throws IOException {
        KafkaConsumer<String, String> consumer;

        // Load in consumer.props file
        try (InputStream props = Resources.getResource("consumer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            if (properties.getProperty("group.id") == null) {
                properties.setProperty("group.id", "group-" + new Random().nextInt(100000));
            }
            consumer = new KafkaConsumer<>(properties);
        }

        // Subscribe to topics and start consuming
        consumer.subscribe(Arrays.asList("taytochips"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(200);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.offset() + ": " + record.value());
                }
            }
        } catch (Exception e) {
            // Handle later
        } finally {
            consumer.close();
        }
    }
}
