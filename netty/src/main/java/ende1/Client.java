package ende1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by zqLuo
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        try {
            EventLoopGroup group = new NioEventLoopGroup(); // (1) TCP协议专用
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf));
                            //设置字符串形式的编码
                            socketChannel.pipeline().addLast(new StringDecoder()); //直接传递字符串
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            //与服务端建立连接
            ChannelFuture cf1 = b.connect("127.0.0.1",8765).sync();
            System.out.println("启动成功");
            //写数据
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("aaaaaa!$_".getBytes()));
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("bbbbbb!$_".getBytes()));
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("cccccc!$_".getBytes()));
            System.out.println("发送成功");
            //异步释放连接
            cf1.channel().closeFuture().sync();
            group.shutdownGracefully();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
