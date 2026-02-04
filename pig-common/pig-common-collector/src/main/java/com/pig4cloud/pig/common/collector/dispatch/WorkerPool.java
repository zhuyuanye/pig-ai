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

package com.pig4cloud.pig.common.collector.dispatch;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Collection task worker thread pool
 */
@Component
@Slf4j
public class WorkerPool implements DisposableBean {

    private ThreadPoolExecutor workerExecutor;

    public WorkerPool() {
        initWorkExecutor();
    }

    private void initWorkExecutor() {
        // thread factory
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((thread, throwable) -> {
                    log.error("[Important] WorkerPool workerExecutor has uncaughtException.", throwable);
                    log.error("Thread Name {} : {}", thread.getName(), throwable.getMessage(), throwable);
                })
                .setDaemon(true)
                .setNameFormat("collect-worker-%d")
                .build();
        int coreSize = Math.max(4, Runtime.getRuntime().availableProcessors() * 2); // 增加核心线程数
        int maxSize = Runtime.getRuntime().availableProcessors() * 20; // 稍微增加最大线程数
        workerExecutor = new ThreadPoolExecutor(coreSize,
                maxSize,
                30, // 增加线程保活时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(200), // 使用有界队列替代SynchronousQueue
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()); // 改为CallerRunsPolicy，避免丢失任务
    }

    /**
     * Run the collection task thread
     *
     * @param runnable Task
     * @throws RejectedExecutionException when thread pool full
     */
    public void executeJob(Runnable runnable) throws RejectedExecutionException {
        workerExecutor.execute(runnable);
    }

    @Override
    public void destroy() throws Exception {
        if (workerExecutor != null) {
            workerExecutor.shutdownNow();
        }
    }
}
