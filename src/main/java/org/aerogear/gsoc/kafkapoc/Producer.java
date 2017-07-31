package org.aerogear.gsoc.kafkapoc;
import org.aerogear.gsoc.kafkapoc.model.Tweet;
import org.aerogear.gsoc.kafkapoc.util.GenericSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.google.common.io.Resources;
import org.apache.kafka.common.serialization.Serdes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * Simple Kafka Producer class to send messages to a given
 * topic
 *
 *  @author Dimitra Zuccarelli
 */
public class Producer {

    // Random tweets from a tweetbot lol
    final private String [] tweets = {"I wrote again to turn to account", "We must be completely clouded over in the best", "And set up. As things are.", "At first he talks of his brother Fyodor.", "\" The Idiot \" the incarnation of avarice.", "Whenever we think of you.", "But they're frightfully limited!", "All the rest of the 0.", "But in the course of your state of health", "But if the creditors.", "I owe to these gentry know very precisely how much you will permit me", "And expressed their surprise at my expense \"!", "I hope that I forgot my exalted position as a revelation", "Suppose it really succeeds."};
    final private static String OUTPUT_TOPIC = "kafka-quotes";

    Random random = new Random();

    public static void main(String[] args) throws IOException {
        Producer producer = new Producer();
        producer.startProducer(OUTPUT_TOPIC);
    }

    private void startProducer(String topic) throws IOException {
        KafkaProducer<String, Tweet> producer;

        // Read in properties file
        try (InputStream props = Resources.getResource("producer.props").openStream()) {
            Properties properties = new Properties();

            // Properties for custom serialisers
            properties.put("key.serializer", Serdes.String().serializer().getClass());
            properties.put("value.serializer", GenericSerializer.class.getName());
            properties.put("value.serializer.type", Tweet.class.getName());
            properties.load(props);

            System.out.println("Attempting to connect to bootstrap server: " + properties.getProperty("bootstrap.servers"));
            producer = new KafkaProducer<>(properties);
        }

        // Send messages to topic
        try {
            for (int i = 0; i < 20; i++) {
                Tweet tweet = new Tweet(Integer.toString(i), tweets[random.nextInt(tweets.length)], "en");
                producer.send(new ProducerRecord<String, Tweet>(topic, Integer.toString(i), tweet));
                System.out.println(i + " " + tweet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }

    }
}

