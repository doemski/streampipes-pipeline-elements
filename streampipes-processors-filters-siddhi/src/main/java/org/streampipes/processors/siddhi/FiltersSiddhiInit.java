/*
 * Copyright 2017 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.streampipes.processors.siddhi;

import org.streampipes.container.init.DeclarersSingleton;
import org.streampipes.container.standalone.init.StandaloneModelSubmitter;
import org.streampipes.dataformat.cbor.CborDataFormatFactory;
import org.streampipes.dataformat.fst.FstDataFormatFactory;
import org.streampipes.dataformat.json.JsonDataFormatFactory;
import org.streampipes.dataformat.smile.SmileDataFormatFactory;
import org.streampipes.messaging.jms.SpJmsProtocolFactory;
import org.streampipes.messaging.kafka.SpKafkaProtocolFactory;
import org.streampipes.processors.siddhi.config.FilterSiddhiConfig;
import org.streampipes.processors.siddhi.stop.StreamStopController;
import org.streampipes.processors.siddhi.trend.TrendController;

public class FiltersSiddhiInit extends StandaloneModelSubmitter {

  public static void main(String[] args) {
    DeclarersSingleton
            .getInstance()
            .add(new TrendController())
            .add(new StreamStopController());

    DeclarersSingleton.getInstance().registerDataFormats(new JsonDataFormatFactory(),
            new CborDataFormatFactory(),
            new SmileDataFormatFactory(),
            new FstDataFormatFactory());

    DeclarersSingleton.getInstance().registerProtocols(new SpKafkaProtocolFactory(),
            new SpJmsProtocolFactory());

    new FiltersSiddhiInit().init(FilterSiddhiConfig.INSTANCE);
  }
}
