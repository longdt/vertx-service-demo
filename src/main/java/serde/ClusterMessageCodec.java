package serde;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import util.Arguments;

public class ClusterMessageCodec implements MessageCodec<ClusterMessage, ClusterMessage> {
    private ClusterMessageFactory[] factories;

    public ClusterMessageCodec(ClusterMessageFactory[] factories) {
        this.factories = factories;
    }

    @Override
    public void encodeToWire(Buffer buffer, ClusterMessage message) {
        buffer.appendInt(message.getFactoryId());
        if (message.getFactoryId() != Bits.ARGUMENTS_TYPE) {
                buffer.appendInt(message.getClassId());
        }
        message.writeToBuffer(buffer);
    }

    @Override
    public ClusterMessage decodeFromWire(int pos, Buffer buffer) {
        var factoryId = buffer.getInt(pos);
        pos += 4;
        if (factoryId == Bits.ARGUMENTS_TYPE) {
            var arguments = new Arguments(factories);
            arguments.readFromBuffer(pos, buffer);
            return arguments;
        }
        var typeId = buffer.getInt(pos);
        pos += 4;
        var result = factories[factoryId].create(typeId);
        result.readFromBuffer(pos, buffer);
        return result;
    }

    @Override
    public ClusterMessage transform(ClusterMessage message) {
        return (ClusterMessage) message.copy();
    }

    @Override
    public String name() {
        return "clustermessage";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
