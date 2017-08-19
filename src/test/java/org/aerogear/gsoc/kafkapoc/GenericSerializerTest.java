package org.aerogear.gsoc.kafkapoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import org.aerogear.gsoc.kafkapoc.util.GenericSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class GenericSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Serializer<Object> serializer;

    @Before
    public void setup() {
        serializer = new GenericSerializer<>();
    }

    @Test
    public void serializeNull() {
        assertNull(serializer.serialize("test-topic", null));
    }

    @Test
    public void serialize() throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("foo", "bar");
        message.put("baz", 354.99);

        byte[] bytes = serializer.serialize("test-topic", message);

        Object deserialized = this.objectMapper.readValue(bytes, Object.class);
        assertEquals(message, deserialized);
    }

    @Test
    public void serializeUser() throws IOException {
        User user = new User("foo", 21);
        byte[] bytes = serializer.serialize("test-topic", user);

        assertEquals(user, this.objectMapper.readValue(bytes, User.class));
    }

    public static class User {
        private String username;
        private int age;

        public User() {
        }

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;

            final User other = (User) obj;
            return Objects.equal(this.username, other.username)
                    && Objects.equal(this.age, other.age);

        }

        public String getUsername() {
            return username;
        }

        public int getAge() {
            return age;
        }
    }
}