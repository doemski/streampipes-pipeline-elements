/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.processors.textmining.flink.processor.wordcount;

import org.streampipes.model.DataProcessorType;
import org.streampipes.model.graph.DataProcessorDescription;
import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.model.schema.PropertyScope;
import org.streampipes.processors.textmining.flink.config.TextMiningFlinkConfig;
import org.streampipes.sdk.builder.ProcessingElementBuilder;
import org.streampipes.sdk.builder.StreamRequirementsBuilder;
import org.streampipes.sdk.extractor.ProcessingElementParameterExtractor;
import org.streampipes.sdk.helpers.*;
import org.streampipes.sdk.utils.Assets;
import org.streampipes.wrapper.flink.FlinkDataProcessorDeclarer;
import org.streampipes.wrapper.flink.FlinkDataProcessorRuntime;

public class WordCountController extends FlinkDataProcessorDeclarer<WordCountParameters> {

  private static final String WORD_COUNT_FIELD_KEY = "wordcountField";
  private static final String TIME_WINDOW_KEY = "timeWindow";
  private static final String WORD_KEY = "word";
  private static final String COUNT_KEY = "count";

  @Override
  public DataProcessorDescription declareModel() {
    return ProcessingElementBuilder.create("org.streampipes.processors.textmining.flink.wordcount")
            .withAssets(Assets.DOCUMENTATION)
            .withLocales(Locales.EN)
            .category(DataProcessorType.AGGREGATE)
            .requiredStream(StreamRequirementsBuilder
                    .create()
                    .requiredPropertyWithUnaryMapping(
                            EpRequirements.stringReq(),
                            Labels.withId(WORD_COUNT_FIELD_KEY),
                            PropertyScope.NONE)
                    .build())
            .outputStrategy(OutputStrategies.fixed(EpProperties.stringEp(
                    Labels.withId(WORD_KEY),
                    "word",
                    "http://schema.org/text"),
                    EpProperties.integerEp(Labels.withId(COUNT_KEY),
                    "count", "http://schema.org/number")))
            .requiredIntegerParameter(Labels.withId(TIME_WINDOW_KEY))
            .build();
  }

  @Override
  public FlinkDataProcessorRuntime<WordCountParameters> getRuntime(DataProcessorInvocation graph, ProcessingElementParameterExtractor extractor) {

    String fieldName = extractor.mappingPropertyValue(WORD_COUNT_FIELD_KEY);
    Integer timeWindowValue = extractor.singleValueParameter(TIME_WINDOW_KEY, Integer.class);

    return new WordCountProgram(new WordCountParameters(graph, fieldName, timeWindowValue), TextMiningFlinkConfig.INSTANCE.getDebug());

  }
}
