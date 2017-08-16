package org.aerogear.gsoc.kafkapoc.util;

import com.google.gson.Gson;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GenericGsonDeserializer<T> implements Deserializer<T> {

    private Class<T> type;
    private Gson gson = new Gson();

    public GenericGsonDeserializer() {
    }

    public GenericGsonDeserializer(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (type != null) {
            return;
        }

        String typeProp = isKey ? "key.deserializer.type" : "value.deserializer.type";
        String typeName = (String) configs.get(typeProp);
        try {
            type = (Class<T>) Class.forName(typeName);
        } catch (Exception e) {
            throw new SerializationException("Failed to initialize GenericDeserializer for " + typeName, e);
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        if (type == null) {
            throw new SerializationException("Unable to deserialize object: type is undefined");
        }

        try {
            return gson.fromJson(new String(data), type);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void close() {
    }

}