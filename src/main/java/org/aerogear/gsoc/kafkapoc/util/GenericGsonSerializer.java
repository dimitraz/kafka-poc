package org.aerogear.gsoc.kafkapoc.util;

import com.google.gson.Gson;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;


public class GenericGsonSerializer<T> implements Serializer<T> {

    private Class<T> type;
    private Gson gson = new Gson();

    public GenericGsonSerializer() {
    }

    public GenericGsonSerializer(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (type != null) {
            return;
        }
        String typeProp = isKey ? "key.serializer.type" : "value.serializer.type";
        String typeName = (String) configs.get(typeProp);
        try {
            type = (Class<T>) Class.forName(typeName);
        } catch (Exception e) {
            throw new SerializationException("Failed to initialize GenericSerializer for " + typeName, e);
        }
    }

    @Override
    public byte[] serialize(String topic, T object) {
        if (object == null) {
            return null;
        }

        if (type == null) {
            throw new SerializationException("Unable to serialize object: type is undefined");
        }

        try {
            return gson.toJson(object, type).getBytes();
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void close() {
    }

}
