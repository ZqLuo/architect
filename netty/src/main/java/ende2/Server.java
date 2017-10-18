package ende2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by zqLuo
 * 设置定长字符串
 * http://ifeve.com/netty5-user-guide/
 */
public class Server {

    public static void main(String[] args) {

       try {
           //1  第一个经常被叫做‘boss’，用来接收进来的连接
           EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1) TCP协议专用
           //2 第二个经常被叫做‘worker’，用来处理已经被接收的连接，一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上
           EventLoopGroup workerGroup = new NioEventLoopGroup();

           //3创建一个辅助类，对server端进行一系列的配置
           ServerBootstrap b = new ServerBootstrap();
           //将两个工作线程组加入
           b.group(bossGroup,workerGroup)
                   //制定使用NioServerSocketChannel通道（TCP),不同通信协议使用不同通道
                   .channel(NioServerSocketChannel.class)
                   //使用childHandler 绑定处理器
                   .childHandler(new ChannelInitializer<SocketChannel>() {
                       @Override
                       protected void initChannel(SocketChannel socketChannel) throws Exception {
                           //设置定长字符串接收
                           socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(5));
                           //设置字符串形式的编码
                           socketChannel.pipeline().addLast(new StringDecoder()); //直接传递字符串
                           socketChannel.pipeline().addLast(new ServerHandler());  //处理器
                       }
                   })
                   //设置TCP连接缓冲区
                   .option(ChannelOption.SO_BACKLOG,128);


           //绑定端口
           ChannelFuture f1 = b.bind(8765).sync();

           //阻塞等待连接
           f1.channel().closeFuture().sync();
           bossGroup.shutdownGracefully();
           workerGroup.shutdownGracefully();
       } catch (Exception e){
           e.printStackTrace();
       }
    }

}
