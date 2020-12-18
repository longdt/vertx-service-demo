package serde;

import io.vertx.core.buffer.Buffer;

import java.nio.charset.Charset;

import static serde.Bits.EMPTY_BYTES;
import static serde.Bits.NULL_OBJECT;

public class Deserializer {
    private Buffer buffer;
    private int pos;

    public Deserializer(Buffer buffer, int pos) {
        this.buffer = buffer;
        this.pos = pos;
    }

    public String readUTF() {
        int length = buffer.getInt(pos);
        pos += 4;
        if (length == NULL_OBJECT) {
            return null;
        } else if (length == 0) {
            return "";
        }
        var result = buffer.getString(pos, pos + length);
        pos += length;
        return result;
    }

    public String readString(Charset charset) {
        int length = buffer.getInt(pos);
        pos += 4;
        var bytes = buffer.getBytes(pos, pos + length);
        pos += length;
        return new String(bytes, charset);
    }

    public byte[] readUTFBytes() {
        int length = buffer.getInt(pos);
        pos += 4;
        if (length == NULL_OBJECT) {
            return null;
        } else if (length == 0) {
            return EMPTY_BYTES;
        }
        var result = buffer.getBytes(pos, pos + length);
        pos += length;
        return result;
    }

    public long readLong() {
        var result = buffer.getLong(pos);
        pos += 8;
        return result;
    }

    public int readInt() {
        var result = buffer.getInt(pos);
        pos += 4;
        return result;
    }

    public short readShort() {
        var result = buffer.getShort(pos);
        pos += 2;
        return result;
    }

    public byte readByte() {
        var result = buffer.getByte(pos);
        pos += 1;
        return result;
    }

    public char readChar() {
        return (char) readShort();
    }

    public double readDouble() {
        var result = buffer.getDouble(pos);
        pos += 8;
        return result;
    }

    public float readFloat() {
        var result = buffer.getFloat(pos);
        pos += 4;
        return result;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public int getPosition() {
        return pos;
    }

    public void setPosition(int pos) {
        this.pos = pos;
    }


}
