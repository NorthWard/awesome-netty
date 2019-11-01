package zk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.north.netty.zk.NettyZkClient;

@RunWith(JUnit4.class)
public class ZkClientTest {
    @Test
    public void testZkClient() throws Exception {
        NettyZkClient nettyZkClient = new NettyZkClient(3000000);
        String isLogin =  nettyZkClient.login();
        System.out.println(isLogin);
        Thread.sleep(5000);
        nettyZkClient.getChildren("/");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
