/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.collector.collect.ipmi2.client;

import com.pig4cloud.pig.collector.collect.ipmi2.protocol.ipmi.Ipmi20Ipv4SessionWrapper;
import com.pig4cloud.pig.collector.collect.ipmi2.protocol.ipmi.payload.IpmiPayload;
import com.pig4cloud.pig.collector.collect.ipmi2.protocol.rmcp.RmcpPacket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Udp connection for ipmi
 */
@Slf4j
public class UdpConnection {
    final String host;
    final int port;

    final SocketAddress address;

    DatagramChannel channel;

    ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);


    public UdpConnection(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.address = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();

        // 设置socket选项
        try {
            channel.socket().setSoTimeout(10000); // 10秒超时（缩短超时时间以防线程池阻塞）
            channel.socket().setReceiveBufferSize(64 * 1024); // 64KB接收缓冲区
            channel.socket().setSendBufferSize(64 * 1024); // 64KB发送缓冲区
            log.debug("UDP socket configured: host={}, port={}, timeout=10s", host, port);
        } catch (Exception e) {
            log.warn("Failed to configure UDP socket options: {}", e.getMessage());
        }
    }

    private int send(IpmiPacketContext context, IpmiPayload payload) throws IOException {
        Ipmi20Ipv4SessionWrapper wrapper = new Ipmi20Ipv4SessionWrapper();
        wrapper.setIpmiPayload(payload);
        if (context.getIpmiSession().isConnected()) {
            wrapper.setIpmiSessionId(context.getIpmiSession().getSystemSessionId());
            wrapper.setIpmiSessionSequenceNumber(context.getIpmiSession().getAuthenticatedSequenceNumber().getAndIncrement());
        }
        RmcpPacket rmcpPacket = new RmcpPacket();
        rmcpPacket.withData(wrapper);
        ByteBuffer sendBuffer = IpmiEncoderDecoder.encode(context, rmcpPacket);
        return channel.send(sendBuffer, address);
    }

    private <T extends IpmiPayload> T receive(IpmiPacketContext context, Class<T> clazz) throws IOException {
        receiveBuffer.clear();

        // 使用Selector实现10秒超时控制，防止线程池阻塞
        Selector selector = Selector.open();
        try {
            boolean wasBlocking = channel.isBlocking();
            if (wasBlocking) {
                channel.configureBlocking(false);
            }

            SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

            int ready = selector.select(10000); // 10秒超时
            if (ready == 0) {
                throw new SocketTimeoutException("IPMI UDP receive timeout after 10 seconds for " + host + ":" + port);
            }

            if (key.isReadable()) {
                channel.receive(receiveBuffer);
                receiveBuffer.flip();
                RmcpPacket rmcpPacket = IpmiEncoderDecoder.decode(context, receiveBuffer);
                return rmcpPacket.getEncapsulated(clazz);
            } else {
                throw new IOException("Channel not readable after select for " + host + ":" + port);
            }
        } finally {
            selector.close();
            try {
                channel.configureBlocking(true); // 恢复阻塞模式
            } catch (IOException e) {
                log.debug("Failed to restore blocking mode: {}", e.getMessage());
            }
        }
    }

    public <T extends IpmiPayload> T get(IpmiPacketContext context, IpmiPayload payload, Class<T> clazz) throws IOException {
        send(context, payload);
        return receive(context, clazz);
    }

    public void close() throws IOException {
        channel.close();
    }

}
