package zk;

import com.google.gson.Gson;
import org.junit.Test;
import org.north.netty.zk.NettyZkClient;

import java.util.List;

public class ZkClientTest {
    @Test
    public void testZkClient() throws Exception {
        NettyZkClient nettyZkClient = new NettyZkClient(30000);
        List<String> list =  nettyZkClient.getChildren("/");
        System.out.println(new Gson().toJson(list));
    }
}
