package model.request;

import com.dslplatform.json.CompiledJson;
import factory.CreateUserRequestFactory;
import io.vertx.core.buffer.Buffer;
import serde.ClusterMessage;
import serde.Deserializer;
import serde.Serializer;

@CompiledJson
public class CreateUserRequest implements ClusterMessage {
    private String username;
    private String password;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void writeToBuffer(Buffer buffer) {
        new Serializer(buffer).writeUTF(username).writeUTF(password);
    }

    @Override
    public int readFromBuffer(int pos, Buffer buffer) {
        var deserializer = new Deserializer(buffer, pos);
        username = deserializer.readUTF();
        password = deserializer.readUTF();
        return deserializer.getPosition();
    }

    @Override
    public int getFactoryId() {
        return CreateUserRequestFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return CreateUserRequestFactory.CLUSTER_MESSAGE_TYPE;
    }
}
