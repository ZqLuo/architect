package test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by zqLuo
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup(); // (1) TCP协议专用
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });
        //与服务端建立连接
//        ChannelFuture cf1 = b.connect("127.0.0.1",8765).sync();
        ChannelFuture cf2 = b.connect("127.0.0.1",8766).sync();
        //写数据
//        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("Hello World!".getBytes()));
        cf2.channel().writeAndFlush(Unpooled.copiedBuffer("Hello Netty!".getBytes()));

        //异步释放连接
//        cf1.channel().closeFuture().sync();
        cf2.channel().closeFuture().sync();
        group.shutdownGracefully();
    }

}
