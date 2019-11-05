package org.north.netty.common.utils;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
/**
 * protobuf
 * @author laihaohua
 */
public class ProtostuffHelper {
    public ProtostuffHelper() {
    }

    public static <T> byte[] serializeObject(T object, Class<T> clz) {
        Schema<T> schema = RuntimeSchema.createFrom(clz);
        LinkedBuffer BUFF = LinkedBuffer.allocate(512);
        return ProtostuffIOUtil.toByteArray(object, schema, BUFF);
    }

    public static <T> T deSerializeObject(byte[] object, Class<T> clz) {
        RuntimeSchema schema = RuntimeSchema.createFrom(clz);

        Object t;
        try {
            t = clz.newInstance();
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new RuntimeException("init object failed.");
        }

        ProtostuffIOUtil.mergeFrom(object, t, schema);
        return (T) t;
    }
}
