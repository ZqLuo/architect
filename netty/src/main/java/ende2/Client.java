package ende2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
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
                            //设置定长字符串接收
                            socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(5));
                            //设置字符串形式的编码
                            socketChannel.pipeline().addLast(new StringDecoder()); //直接传递字符串
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            //与服务端建立连接
            ChannelFuture cf1 = b.connect("127.0.0.1",8765).sync();
            //写数据
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("aaaaabbbbb".getBytes()));
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("cccccc".getBytes()));
            //异步释放连接
            cf1.channel().closeFuture().sync();
            group.shutdownGracefully();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
