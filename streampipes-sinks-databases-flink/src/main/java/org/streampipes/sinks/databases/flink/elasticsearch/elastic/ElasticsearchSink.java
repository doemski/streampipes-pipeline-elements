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

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.Map;

/**
 * Elasticsearch 6.x sink that requests multiple {@link ActionRequest ActionRequests}
 * against a cluster for each incoming element.
 *
 * <p>The sink internally uses a {@link RestHighLevelClient} to communicate with an Elasticsearch cluster.
 * The sink will fail if no cluster can be connected to using the provided transport addresses passed to the constructor.
 *
 * <p>The {@link Map} passed to the constructor is used to create the {@code TransportClient}. The config keys can be found
 * in the <a href="https://www.elastic.io">Elasticsearch documentation</a>. An important setting is {@code cluster.name},
 * which should be set to the name of the cluster that the sink should emit to.
 *
 * <p>Internally, the sink will use a {@link BulkProcessor} to send {@link ActionRequest ActionRequests}.
 * This will buffer elements before sending a request to the cluster. The behaviour of the
 * {@code BulkProcessor} can be configured using these config keys:
 * <ul>
 *   <li> {@code bulk.flush.max.actions}: Maximum amount of elements to buffer
 *   <li> {@code bulk.flush.max.size.mb}: Maximum amount of data (in megabytes) to buffer
 *   <li> {@code bulk.flush.interval.ms}: Interval at which to flush data regardless of the other two
 *   settings in milliseconds
 * </ul>
 *
 * <p>Note that the Elasticsearch 5.x and later versions convert {@link ActionRequest ActionRequest} to
 * {@link DocWriteRequest DocWriteRequest} in {@link BulkProcessorIndexer}.
 *
 * <p>You also have to provide an {@link ElasticsearchSinkFunction}. This is used to create multiple
 * {@link ActionRequest ActionRequests} for each incoming element. See the class level documentation of
 * {@link ElasticsearchSinkFunction} for an example.
 *
 * @param <T> Type of the elements handled by this sink
 */
public class ElasticsearchSink<T> extends ElasticsearchSinkBase<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@code ElasticsearchSink} that connects to the cluster using a {@link RestHighLevelClient}.
	 *
	 * @param elasticsearchSinkFunction This is used to generate multiple {@link ActionRequest} from the incoming element
	 * @param httpHosts The list of {@HttpHost} to which the {@link RestHighLevelClient} connects to.
	 */
	public ElasticsearchSink(Map<String, String> userConfig, List<HttpHost> httpHosts, ElasticsearchSinkFunction<T> elasticsearchSinkFunction) {

		this(userConfig, httpHosts, elasticsearchSinkFunction, new NoOpFailureHandler());
	}

	/**
	 * Creates a new {@code ElasticsearchSink} that connects to the cluster using a {@link RestHighLevelClient}.
	 *
	 * @param elasticsearchSinkFunction This is used to generate multiple {@link ActionRequest} from the incoming element
	 * @param failureHandler This is used to handle failed {@link ActionRequest}
	 * @param httpHosts The list of {@HttpHost} to which the {@link RestHighLevelClient} connects to.
	 */
	public ElasticsearchSink(
		Map<String, String> userConfig,
		List<HttpHost> httpHosts,
		ElasticsearchSinkFunction<T> elasticsearchSinkFunction,
		ActionRequestFailureHandler failureHandler) {

		super(new Elasticsearch6ApiCallBridge(httpHosts),  userConfig, elasticsearchSinkFunction, failureHandler);
	}
}