package zk;

import com.google.gson.Gson;
import org.junit.Test;
import org.north.netty.zk.NettyZkClient;
import org.north.netty.zk.bean.create.ZkCreateResponse;
import org.north.netty.zk.utils.CreateMode;

import java.util.List;

public class ZkClientTest {
    @Test
    public void testZkClient() throws Exception {
        NettyZkClient nettyZkClient = new NettyZkClient(30000);
        List<String> list =  nettyZkClient.getChildren("/");
        System.out.println(new Gson().toJson(list));
        ZkCreateResponse createResponse = nettyZkClient.create("/as", 12312, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(new Gson().toJson(createResponse));
    }
}
