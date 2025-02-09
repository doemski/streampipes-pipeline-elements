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

package org.streampipes.sinks.brokers.jvm.kafka;

import org.streampipes.model.graph.DataSinkDescription;
import org.streampipes.model.graph.DataSinkInvocation;
import org.streampipes.sdk.builder.DataSinkBuilder;
import org.streampipes.sdk.builder.StreamRequirementsBuilder;
import org.streampipes.sdk.extractor.DataSinkParameterExtractor;
import org.streampipes.sdk.helpers.EpRequirements;
import org.streampipes.sdk.helpers.Labels;
import org.streampipes.sdk.helpers.Locales;
import org.streampipes.sdk.helpers.OntologyProperties;
import org.streampipes.sdk.utils.Assets;
import org.streampipes.wrapper.standalone.ConfiguredEventSink;
import org.streampipes.wrapper.standalone.declarer.StandaloneEventSinkDeclarer;

public class KafkaController extends StandaloneEventSinkDeclarer<KafkaParameters> {

  private static final String KAFKA_BROKER_SETTINGS_KEY = "broker-settings";
  private static final String TOPIC_KEY = "topic";

  private static final String KAFKA_HOST_URI = "http://schema.org/kafkaHost";
  private static final String KAFKA_PORT_URI = "http://schema.org/kafkaPort";

  @Override
  public DataSinkDescription declareModel() {
    return DataSinkBuilder.create("org.streampipes.sinks.brokers.jvm.kafka")
            .withLocales(Locales.EN)
            .withAssets(Assets.DOCUMENTATION, Assets.ICON)
            .requiredStream(StreamRequirementsBuilder
                    .create()
                    .requiredProperty(EpRequirements.anyProperty())
                    .build())
            .requiredTextParameter(Labels.withId(TOPIC_KEY), false, false)
            .requiredOntologyConcept(Labels.withId(KAFKA_BROKER_SETTINGS_KEY),
                    OntologyProperties.mandatory(KAFKA_HOST_URI),
                    OntologyProperties.mandatory(KAFKA_PORT_URI))
            .build();
  }

  @Override
  public ConfiguredEventSink<KafkaParameters> onInvocation(DataSinkInvocation graph,
                                                           DataSinkParameterExtractor extractor) {
    String topic = extractor.singleValueParameter(TOPIC_KEY, String.class);

    String kafkaHost = extractor.supportedOntologyPropertyValue(KAFKA_BROKER_SETTINGS_KEY, KAFKA_HOST_URI,
            String.class);
    Integer kafkaPort = extractor.supportedOntologyPropertyValue(KAFKA_BROKER_SETTINGS_KEY, KAFKA_PORT_URI,
            Integer.class);

    KafkaParameters params = new KafkaParameters(graph, kafkaHost, kafkaPort, topic);

    return new ConfiguredEventSink<>(params, KafkaPublisher::new);
  }

}
