package util;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.runtime.Settings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.FastThreadLocal;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Json {
    private static final Logger log = LogManager.getLogger(Json.class);

    private static final DslJson<Object> dslJson =
            new DslJson<>(Settings.withRuntime().unknownNumbers(JsonReader.UnknownNumberParsing.LONG_AND_DOUBLE).allowArrayFormat(true).includeServiceLoader());

    private static FastThreadLocal<JsonReader<Object>> localJsonReader = new FastThreadLocal<>() {
        @Override
        protected JsonReader<Object> initialValue() throws Exception {
            return dslJson.newReader();
        }
    };
    private static FastThreadLocal<JsonWriter> localJsonWriter = new FastThreadLocal<>() {
        @Override
        protected JsonWriter initialValue() throws Exception {
            return dslJson.newWriter();
        }
    };

    private Json() {
    }

    public static byte[] encode(Object obj) throws IOException {
        var writer = localJsonWriter.get();
        dslJson.serialize(writer, obj);
        return writer.toByteArray();
    }

    public static void encode(Object obj, OutputStream outputStream) throws IOException {
        dslJson.serialize(obj, outputStream);
    }

    public static void encode(Object obj, Buffer buffer) throws IOException {
        encode(obj, new ByteBufOutputStream(((BufferImpl) buffer).byteBuf()));
    }

    public static void encode(Object obj, ByteBuf buffer) throws IOException {
        encode(obj, new ByteBufOutputStream(buffer));
    }

    public static String encodeToString(Object obj) throws IOException {
        var writer = localJsonWriter.get();
        dslJson.serialize(writer, obj);
        return writer.toString();
    }

    /**
     * Encode a POJO to JSON using the underlying Jackson mapper.
     *
     * @param obj a POJO
     * @return a Buffer containing the JSON representation of the given POJO.
     * @throws EncodeException if a property cannot be encoded.
     */
    public static Buffer encodeToBuffer(Object obj) throws IOException {
        ByteBuf buffer = Unpooled.buffer(32, Integer.MAX_VALUE);
        encode(obj, new ByteBufOutputStream(buffer));
        return Buffer.buffer(buffer);
    }

    /**
     * Decode a given JSON buffer to a POJO of the given class type.
     *
     * @param buf   the JSON buffer.
     * @param clazz the class to map to.
     * @param <T>   the generic type.
     * @return an instance of T
     * @throws DecodeException when there is a parsing or invalid mapping.
     */
    public static <T> T decodeValue(Buffer buf, Class<T> clazz) throws DecodeException {
        try {
            return dslJson.deserialize(clazz, new ByteBufInputStream(buf.getByteBuf()));
        } catch (Exception e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }

    /**
     * check whether 2 given jsonobject equals non-null key/value
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(JsonObject a, JsonObject b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null || a.getClass() != b.getClass()) {
            return false;
        }
        return objectEquals(a.getMap(), b);
    }

    static boolean objectEquals(Map<?, ?> m1, Object o2) {
        Map<?, ?> m2;
        if (o2 instanceof JsonObject) {
            m2 = ((JsonObject) o2).getMap();
        } else if (o2 instanceof Map<?, ?>) {
            m2 = (Map<?, ?>) o2;
        } else {
            return false;
        }
        int m1NonNullCnt = 0;
        for (Map.Entry<?, ?> entry : m1.entrySet()) {
            Object val = entry.getValue();
            if (val == null) {
                if (m2.get(entry.getKey()) != null) {
                    return false;
                }
            } else {
                if (!equals(entry.getValue(), m2.get(entry.getKey()))) {
                    return false;
                }
                ++m1NonNullCnt;
            }
        }
        return m1NonNullCnt == m2.entrySet().stream().filter(e -> e.getValue() != null).count();
    }

    static boolean equals(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 instanceof JsonObject) {
            return objectEquals(((JsonObject) o1).getMap(), o2);
        }
        if (o1 instanceof Map<?, ?>) {
            return objectEquals((Map<?, ?>) o1, o2);
        }
        if (o1 instanceof JsonArray) {
            return arrayEquals(((JsonArray) o1).getList(), o2);
        }
        if (o1 instanceof List<?>) {
            return arrayEquals((List<?>) o1, o2);
        }
        if (o1 instanceof Number && o2 instanceof Number && o1.getClass() != o2.getClass()) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            if (o1 instanceof Float || o1 instanceof Double || o2 instanceof Float || o2 instanceof Double) {
                return n1.doubleValue() == n2.doubleValue();
            } else {
                return n1.longValue() == n2.longValue();
            }
        }
        return o1.equals(o2);
    }

    static boolean arrayEquals(List<?> l1, Object o2) {
        List<?> l2;
        if (o2 instanceof JsonArray) {
            l2 = ((JsonArray) o2).getList();
        } else if (o2 instanceof List<?>) {
            l2 = (List<?>) o2;
        } else {
            return false;
        }
        if (l1.size() != l2.size())
            return false;
        Iterator<?> iter = l2.iterator();
        for (Object entry : l1) {
            Object other = iter.next();
            if (entry == null) {
                if (other != null) {
                    return false;
                }
            } else if (!equals(entry, other)) {
                return false;
            }
        }
        return true;
    }
}
