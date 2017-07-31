package org.aerogear.gsoc.kafkapoc.util;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Factory for creating serializers / deserializers.
 */
public class Serdes {

    static protected class WrapperSerde<T> implements Serde<T> {
        final private Serializer<T> serializer;
        final private Deserializer<T> deserializer;

        public WrapperSerde(Serializer<T> serializer, Deserializer<T> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
            serializer.configure(configs, isKey);
            deserializer.configure(configs, isKey);
        }

        @Override
        public void close() {
            serializer.close();
            deserializer.close();
        }

        @Override
        public Serializer<T> serializer() {
            return serializer;
        }

        @Override
        public Deserializer<T> deserializer() {
            return deserializer;
        }
    }

    static public final class GenericSerde<T> extends WrapperSerde<T> {
        public GenericSerde(Class<T> type) {
            super(new GenericSerializer<T>(type), new GenericDeserializer<T>(type));
        }
    }

    @SuppressWarnings("unchecked")
    static public <T> Serde<T> serdeFrom(Class<T> type) {

        return new GenericSerde<T>(type);
        // throw new IllegalArgumentException("Unknown class for built-in serializer");
    }

    /**
     * Construct a serde object from separate serializer and deserializer
     *
     * @param serializer    must not be null.
     * @param deserializer  must not be null.
     */
    static public <T> Serde<T> serdeFrom(final Serializer<T> serializer, final Deserializer<T> deserializer) {
        if (serializer == null) {
            throw new IllegalArgumentException("serializer must not be null");
        }
        if (deserializer == null) {
            throw new IllegalArgumentException("deserializer must not be null");
        }

        return new WrapperSerde<>(serializer, deserializer);
    }

}