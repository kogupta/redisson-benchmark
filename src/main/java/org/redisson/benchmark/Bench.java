/**
 * Copyright 2016 Nikita Koksharov
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.benchmark;

import com.codahale.metrics.MetricRegistry;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nikita Koksharov
 *
 * @param <T>
 */
public interface Bench<T> {

  T createClient(int connections, String host);

  void executeOperation(String data, T benchInstance, int threadNumber, int iteration, MetricRegistry metrics);

  void shutdown(T instance);

  static RedissonClient clusterConfig() {
    try {
      Config c = Config.fromJSON(new File("/home/kohgupta/depot/redisson-benchmark/src/main/resources/config.json"));
      RedissonClient r = Redisson.create(c);
      r.getKeys().flushdb();
      return r;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  static RedissonClient singleServerConfig(int connections) {
    Config c = new Config();
    c.useSingleServer()
        .setTimeout(10_000_000)
        .setAddress("127.0.0.1:6379")
        .setConnectionPoolSize(connections)
        .setConnectionMinimumIdleSize(connections);
    c.setCodec(StringCodec.INSTANCE);

    RedissonClient r = Redisson.create(c);
    r.getKeys().flushdb();
    return r;
  }

}
