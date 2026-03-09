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

package com.pig4cloud.pig.collector.dispatch.entrance.processor;

import io.netty.channel.ChannelHandlerContext;
import com.pig4cloud.pig.collector.dispatch.entrance.CollectServer;
import com.pig4cloud.pig.common.core.entity.job.Job;
import com.pig4cloud.pig.common.core.entity.message.ClusterMsg;
import com.pig4cloud.pig.common.core.util.JsonUtil;
import com.pig4cloud.pig.common.remoting.netty.NettyRemotingProcessor;

/**
 * handle one-time collect data response message
 */
public class CollectOneTimeDataProcessor implements NettyRemotingProcessor {
    private final CollectServer collectServer;

    public CollectOneTimeDataProcessor(final CollectServer collectServer) {
        this.collectServer = collectServer;
    }

    @Override
    public ClusterMsg.Message handle(ChannelHandlerContext ctx, ClusterMsg.Message message) {
        Job oneTimeJob = JsonUtil.fromJson(message.getMsg().toStringUtf8(), Job.class);
        collectServer.getCollectJobService().collectSyncOneTimeJobData(oneTimeJob);
        return null;
    }
}
