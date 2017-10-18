package serial;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import util.GzipUtils;

import java.io.File;
import java.io.FileOutputStream;

public class ServerHandler extends ChannelHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Request request = (Request)msg;
		System.out.println("Server : " + request.getId() + ", " + request.getName() + ", " + request.getRequestMessage());
		byte[] attachment = GzipUtils.ungzip(request.getAttachment());
		//写出文件
		String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" +  File.separatorChar + "006.jpg";
		System.out.println(writePath);
		FileOutputStream fos = new FileOutputStream(writePath);
		fos.write(attachment);
		fos.close();

		Response response = new Response();
		response.setId(request.getId());
		response.setName("response" + request.getId());
		response.setResponseMessage("响应内容" + request.getId());
		ctx.writeAndFlush(response);//.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	
	
}
