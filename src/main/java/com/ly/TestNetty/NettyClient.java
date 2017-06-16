package com.ly.TestNetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created by Lo__oY on 2017/5/29.
 */
public class NettyClient {

    private int port;
    private String host;

    public NettyClient(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
        start();

    }

    private void start() throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host,port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                    socketChannel.pipeline().addLast(new NettyClientHandler());
                }
            });

            ChannelFuture future = bootstrap.connect(host,port).sync();
            if(future.isSuccess()){

                System.out.println("------------------connect server success--------------");
            }
            future.channel().closeFuture().sync();


        } finally {

            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String args[]) throws InterruptedException {

        NettyClient client = new NettyClient(9999,"localhost");
    }
}
