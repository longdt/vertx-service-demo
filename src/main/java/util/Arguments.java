package util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.impl.ClusterSerializable;
import serde.*;

import java.util.Arrays;
import java.util.List;

public class Arguments implements ClusterMessage {
    private ClusterMessageFactory[] factories;
    private List data;

    public Arguments(ClusterMessageFactory[] factories) {
        this.factories = factories;
    }

    public Arguments(Object... data) {
        this.data = Arrays.asList(data);
    }

    public Arguments(List<?> data) {
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        return (T) data.get(index);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList() {
        return data;
    }

    @Override
    public int getFactoryId() {
        return Bits.ARGUMENTS_TYPE;
    }

    @Override
    public int getClassId() {
        return Bits.ARGUMENTS_TYPE;
    }

    @Override
    public void writeToBuffer(Buffer buffer) {
        var serializer = new Serializer(buffer).writeByte((byte) data.size());
        for (var arg : data) {
            writeArgument(serializer, arg);
        }
    }

    private void writeArgument(Serializer serializer, Object arg) {
        if (arg == null) {
            serializer.writeInt(Bits.NULL_OBJECT);
        } else if (arg instanceof ClusterMessage) {
            serializer.writeInt(((ClusterMessage) arg).getFactoryId())
                    .writeInt(((ClusterMessage) arg).getClassId());
            ((ClusterMessage) arg).writeToBuffer(serializer.getBuffer());
        } else if (arg instanceof Long) {
            serializer.writeInt(Bits.LONG_TYPE).writeLong((Long) arg);
        } else if (arg instanceof Integer) {
            serializer.writeInt(Bits.INT_TYPE).writeInt((Integer) arg);
        } else if (arg instanceof Float) {
            serializer.writeInt(Bits.FLOAT_TYPE).writeFloat((Float) arg);
        } else if (arg instanceof Double) {
            serializer.writeInt(Bits.DOUBLE_TYPE).writeDouble((Double) arg);
        } else if (arg instanceof Short) {
            serializer.writeInt(Bits.SHORT_TYPE).writeShort((Short) arg);
        } else if (arg instanceof Byte) {
            serializer.writeInt(Bits.BYTE_TYPE).writeByte((Byte) arg);
        } else if (arg instanceof String) {
            serializer.writeInt(Bits.STRING_TYPE).writeUTF((String) arg);
        } else if (arg instanceof Character) {
            serializer.writeInt(Bits.CHAR_TYPE).writeChar((Character) arg);
        } else if (arg instanceof JsonObject) {
            serializer.writeInt(Bits.JSON_OBJECT_TYPE);
            ((JsonObject) arg).writeToBuffer(serializer.getBuffer());
        } else if (arg instanceof JsonArray) {
            serializer.writeInt(Bits.JSON_ARRAY_TYPE);
            ((JsonArray) arg).writeToBuffer(serializer.getBuffer());
        } else {
            throw new IllegalArgumentException("Unsupport argument type: " + arg.getClass());
        }
    }

    @Override
    public int readFromBuffer(int pos, Buffer buffer) {
        var deserializer = new Deserializer(buffer, pos);
        int length = deserializer.readByte();
        var data = new Object[length];
        for (int i = 0; i < length; ++i) {
            data[i] = readArgument(deserializer);
        }
        this.data = Arrays.asList(data);
        return deserializer.getPosition();
    }

    private Object readArgument(Deserializer deserializer) {
        var factoryId = deserializer.readInt();
        if (factoryId == Bits.NULL_OBJECT) {
            return null;
        }
        switch (factoryId) {
            case Bits.LONG_TYPE:
                return deserializer.readLong();
            case Bits.INT_TYPE:
                return deserializer.readInt();
            case Bits.FLOAT_TYPE:
                return deserializer.readFloat();
            case Bits.DOUBLE_TYPE:
                return deserializer.readDouble();
            case Bits.SHORT_TYPE:
                return deserializer.readShort();
            case Bits.BYTE_TYPE:
                return deserializer.readByte();
            case Bits.STRING_TYPE:
                return deserializer.readUTF();
            case Bits.CHAR_TYPE:
                return deserializer.readChar();
            case Bits.JSON_OBJECT_TYPE:
                return readClusterSerializable(deserializer, new JsonObject());
            case Bits.JSON_ARRAY_TYPE:
                return readClusterSerializable(deserializer, new JsonArray());
        }
        var typeId = deserializer.readInt();
        ClusterMessage arg = factories[factoryId].create(typeId);
        int pos = arg.readFromBuffer(deserializer.getPosition(), deserializer.getBuffer());
        deserializer.setPosition(pos);
        return arg;
    }

    private ClusterSerializable readClusterSerializable(Deserializer deserializer, ClusterSerializable serializable) {
        int pos = serializable.readFromBuffer(deserializer.getPosition(), deserializer.getBuffer());
        deserializer.setPosition(pos);
        return serializable;
    }
}
