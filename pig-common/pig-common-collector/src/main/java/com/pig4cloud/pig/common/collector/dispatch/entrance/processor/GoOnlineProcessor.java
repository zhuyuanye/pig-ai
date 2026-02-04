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

package com.pig4cloud.pig.common.collector.dispatch.entrance.processor;

import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.collector.timer.TimerDispatch;
import com.pig4cloud.pig.common.core.constants.CommonConstants;
import com.pig4cloud.pig.common.core.entity.message.ClusterMsg;
import com.pig4cloud.pig.common.core.support.SpringContextHolder;
import com.pig4cloud.pig.common.remoting.netty.NettyRemotingProcessor;

/**
 * handle collector online message
 * Note: This is not to re-open the connection with Manager, nor can it be done, just to re-open the collection function
 */
@Slf4j
public class GoOnlineProcessor implements NettyRemotingProcessor {

    private TimerDispatch timerDispatch;

    @Override
    public ClusterMsg.Message handle(ChannelHandlerContext ctx, ClusterMsg.Message message) {
        if (this.timerDispatch == null) {
            this.timerDispatch = SpringContextHolder.getBean(TimerDispatch.class);
        }
        timerDispatch.goOnline();
        log.info("receive online message and handle success");
        return ClusterMsg.Message.newBuilder()
                .setIdentity(message.getIdentity())
                .setDirection(ClusterMsg.Direction.RESPONSE)
                .setMsg(ByteString.copyFromUtf8(String.valueOf(CommonConstants.SUCCESS_CODE)))
                .build();
    }
}
