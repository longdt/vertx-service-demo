package model.entity;

import com.dslplatform.json.CompiledJson;
import factory.UserFactory;
import io.vertx.core.buffer.Buffer;
import serde.ClusterMessage;
import serde.Deserializer;
import serde.Serializer;

@CompiledJson
public class User implements ClusterMessage {
    private Long userId;
    private String username;
    private String password;

    public User() {
    }

    public User(User other) {
        this.userId = other.userId;
        this.username = other.username;
        this.password = other.password;
    }

    public Long getUserId() {
        return userId;
    }

    public User setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public User copy() {
        return new User(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public void writeToBuffer(Buffer buffer) {
        new Serializer(buffer).writeLong(userId)
                .writeUTF(username)
                .writeUTF(password);
    }

    @Override
    public int readFromBuffer(int pos, Buffer buffer) {
        var deserializer = new Deserializer(buffer, pos);
        userId = deserializer.readLong();
        username = deserializer.readUTF();
        password = deserializer.readUTF();
        return deserializer.getPosition();
    }

    @Override
    public int getFactoryId() {
        return UserFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return UserFactory.USER_TYPE;
    }
}
