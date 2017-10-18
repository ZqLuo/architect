package ende1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 服务端处理器
 * Created by zqLuo
 */
public class ServerHandler extends ChannelHandlerAdapter {

    /**
     * 数据处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //do somethint msg
            System.out.println("接收数据");
            String request = (String)msg;
            System.out.println("Server:" + request);
            String response = "我要返回的数据:" + msg + "$_";
            ChannelFuture channelHandler =  ctx.channel().writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
            //channelHandler.addListener(ChannelFutureListener.CLOSE); //关闭连接
            //ctx.channel().close(); //关闭连接
        } finally {
            //释放数据
            ReferenceCountUtil.release(msg);
        }
        //((ByteBuf) msg).release();
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
