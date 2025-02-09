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

package org.streampipes.sinks.databases.flink.elasticsearch.elastic;

import org.apache.flink.annotation.Internal;
import org.elasticsearch.action.DocWriteRequest;

/**
 * An {@link ActionRequestFailureHandler} that simply fails the sink on any failures.
 */
@Internal
public class NoOpFailureHandler implements ActionRequestFailureHandler {

	private static final long serialVersionUID = 737941343410827885L;

	@Override
	public void onFailure(DocWriteRequest action, Throwable failure, int restStatusCode, RequestIndexer indexer) throws Throwable {
		// simply fail the sink
		throw failure;
	}

}