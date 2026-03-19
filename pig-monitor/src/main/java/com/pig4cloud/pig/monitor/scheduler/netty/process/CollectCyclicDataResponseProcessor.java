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
import com.pig4cloud.pig.common.core.entity.message.ClusterMsg;
import com.pig4cloud.pig.common.core.entity.message.CollectRep;
import com.pig4cloud.pig.common.core.queue.CommonDataQueue;
import com.pig4cloud.pig.common.core.support.SpringContextHolder;
import com.pig4cloud.pig.common.core.util.ArrowUtil;
import com.pig4cloud.pig.common.remoting.netty.NettyRemotingProcessor;

import java.util.List;

/**
 * handle cyclic data response message
 */
@Slf4j
public class CollectCyclicDataResponseProcessor implements NettyRemotingProcessor {
    @Override
    public ClusterMsg.Message handle(ChannelHandlerContext ctx, ClusterMsg.Message message) {
        CommonDataQueue dataQueue = SpringContextHolder.getBean(CommonDataQueue.class);
        List<CollectRep.MetricsData> metricsDataList = ArrowUtil.deserializeMetricsData(message.getMsg().toByteArray());
        for (CollectRep.MetricsData metricsData : metricsDataList) {
            if (metricsData != null) {
                dataQueue.sendMetricsData(metricsData);
            }
        }
        return null;
    }
}
