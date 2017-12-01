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
import com.codahale.metrics.Timer;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

/**
 * @author Nikita Koksharov
 */
public class MapPutBenchmark {

  public static void main(String[] args) throws InterruptedException {
    Bench<RedissonClient> bench = new RedissonBench() {
      @Override
      public void executeOperation(String data,
                                   RedissonClient benchInstance,
                                   int threadNumber,
                                   int iteration,
                                   MetricRegistry metrics) {
        RMap<String, String> map = benchInstance.getMap("map_" + threadNumber);
        Timer.Context time = metrics.timer("map").time();
        map.put(data, data);
        time.stop();

//        RBatch batch = benchInstance.createBatch();
//        RMapAsync<String, String> map = batch.getMap("map_" + threadNumber, StringCodec.INSTANCE);
//        Timer.Context time = metrics.timer("map").time();
//        map.putAsync(data, data);
//        time.stop();
      }
    };

    Benchmark benchmark = new Benchmark(bench);
    benchmark.run(args);
  }

}
