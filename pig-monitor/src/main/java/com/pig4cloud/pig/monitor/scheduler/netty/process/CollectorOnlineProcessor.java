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

package com.pig4cloud.pig.monitor.scheduler.netty.process;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.pig4cloud.pig.common.core.entity.dto.CollectorInfo;
import com.pig4cloud.pig.common.core.entity.message.ClusterMsg;
import com.pig4cloud.pig.common.core.util.JsonUtil;
import com.pig4cloud.pig.monitor.scheduler.netty.ManageServer;
import com.pig4cloud.pig.common.remoting.netty.NettyRemotingProcessor;

import java.net.InetSocketAddress;

/**
 * handle collector online message
 */
@Slf4j
public class CollectorOnlineProcessor implements NettyRemotingProcessor {
    private final ManageServer manageServer;

    public CollectorOnlineProcessor(final ManageServer manageServer) {
        this.manageServer = manageServer;
    }

    @Override
    public ClusterMsg.Message handle(ChannelHandlerContext ctx, ClusterMsg.Message message) {
        String collector = message.getIdentity();
        log.info("the collector {} actively requests to go online.", collector);
        String msg = message.getMsg().toStringUtf8();
        CollectorInfo collectorInfo = JsonUtil.fromJson(msg, CollectorInfo.class);
        if (collectorInfo != null && StringUtils.isBlank(collectorInfo.getIp())) {
            // fetch remote ip address
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIP = socketAddress.getAddress().getHostAddress();
            collectorInfo.setIp(clientIP);
        }
        this.manageServer.addChannel(collector, ctx.channel());
        this.manageServer.getCollectorAndJobScheduler().collectorGoOnline(collector, collectorInfo);
        return null;
    }
}
