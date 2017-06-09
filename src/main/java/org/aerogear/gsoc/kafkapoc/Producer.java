package org.aerogear.gsoc.kafkapoc;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * Simple Kafka Producer class to send messages to a given
 * topic
 *
 *  @author Dimitra Zuccarelli
 */
public class Producer {
    // private String topic;

    public static void main(String[] args) throws IOException {
        Producer producer = new Producer();
        producer.startProducer("taytochips");
    }

    private void startProducer(String topic) throws IOException {
        KafkaProducer<String, String> producer;

        // Read in properties file
        try (InputStream props = Resources.getResource("producer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);

            System.out.println("Attempting to connect to bootstrap server: " + properties.getProperty("bootstrap.servers"));
            producer = new KafkaProducer<>(properties);
        }

        // Send messages to topic
        try {
            for (int i = 0; i < 1000; i++) {
                producer.send(new ProducerRecord<String, String>(topic, UUID.randomUUID().toString(), Integer.toString(i)));
                System.out.println("Message sent " + i);
            }
        } catch (Throwable throwable) {
            System.out.printf("%s", throwable.getStackTrace());
        } finally {
            producer.close();
        }

    }
}

