package serde;

import io.vertx.core.buffer.Buffer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static serde.Bits.NULL_OBJECT;

public class Serializer {
    private Buffer buffer;

    public Serializer(Buffer buffer) {
        this.buffer = buffer;
    }

    public Serializer writeUTF(String str) {
        if (str == null) {
            buffer.appendInt(NULL_OBJECT);
            return this;
        }
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        buffer.appendInt(data.length).appendBytes(data);
        return this;
    }

    public Serializer writeString(String str, Charset charset) {
        byte[] data = str.getBytes(charset);
        buffer.appendInt(data.length).appendBytes(data);
        return this;
    }

    public Serializer writeLong(long value) {
        buffer.appendLong(value);
        return this;
    }

    public Serializer writeInt(int value) {
        buffer.appendInt(value);
        return this;
    }

    public Serializer writeShort(short value) {
        buffer.appendShort(value);
        return this;
    }

    public Serializer writeByte(byte value) {
        buffer.appendByte(value);
        return this;
    }

    public Serializer writeChar(char value) {
        buffer.appendShort((short) value);
        return this;
    }

    public Serializer writeFloat(float value) {
        buffer.appendFloat(value);
        return this;
    }

    public Serializer writeDouble(double value) {
        buffer.appendDouble(value);
        return this;
    }

    public Buffer getBuffer() {
        return buffer;
    }


}
