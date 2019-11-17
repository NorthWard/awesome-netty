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
        // NettyZkClient的构造方法里面会调用login() 跟服务端建立会话
        NettyZkClient nettyZkClient = new NettyZkClient(30000);

        // 创建一个临时顺序节点
        ZkCreateResponse createResponse = nettyZkClient.create("/as", 12312, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(new Gson().toJson(createResponse));

        // 获取/下的所有子路径
        List<String> list =  nettyZkClient.getChildren("/");
        System.out.println(new Gson().toJson(list));

    }
}
