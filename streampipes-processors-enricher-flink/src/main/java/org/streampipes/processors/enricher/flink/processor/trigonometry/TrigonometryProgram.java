/*
Copyright 2019 FZI Forschungszentrum Informatik

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.streampipes.processors.enricher.flink.processor.trigonometry;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.streampipes.model.runtime.Event;
import org.streampipes.processors.enricher.flink.AbstractEnricherProgram;

public class TrigonometryProgram extends AbstractEnricherProgram<TrigonometryParameters> {

    public TrigonometryProgram(TrigonometryParameters params, boolean debug) {
        super(params, debug);
    }

    @Override
    protected DataStream<Event> getApplicationLogic(DataStream<Event>... dataStreams) {
        return dataStreams[0].flatMap(new Trigonometry(params.getOperand(), params.getOperation(), params.getResultField()));
    }
}
