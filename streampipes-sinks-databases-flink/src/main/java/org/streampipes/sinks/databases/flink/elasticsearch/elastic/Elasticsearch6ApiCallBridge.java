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

import org.apache.flink.util.Preconditions;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nullable;

/**
 * Implementation of {@link ElasticsearchApiCallBridge} for Elasticsearch 6 and later versions.
 */
public class Elasticsearch6ApiCallBridge extends ElasticsearchApiCallBridge {

	private static final long serialVersionUID = -5222683870097809633L;

	private static final Logger LOG = LoggerFactory.getLogger(Elasticsearch6ApiCallBridge.class);

	/**
	 * User-provided HTTP Host.
	 */
	private final List<HttpHost> httpHosts;

	Elasticsearch6ApiCallBridge(List<HttpHost> httpHosts) {
		Preconditions.checkArgument(httpHosts != null && !httpHosts.isEmpty());
		this.httpHosts = httpHosts;
	}

	@Override
	public AutoCloseable createClient(Map<String, String> clientConfig) {
		RestHighLevelClient rhlClient =
						new RestHighLevelClient(RestClient.builder(httpHosts.toArray(new HttpHost[httpHosts.size()])));

		if (LOG.isInfoEnabled()) {
			LOG.info("Created Elasticsearch RestHighLevelClient connected to {}", httpHosts.toString());
		}

		return rhlClient;
	}

	@Override
	public BulkProcessor.Builder createBulkProcessorBuilder(AutoCloseable client, BulkProcessor.Listener listener) {
		RestHighLevelClient rhlClient = (RestHighLevelClient) client;
		return BulkProcessor.builder(rhlClient::bulkAsync, listener);
	}

	@Override
	public Throwable extractFailureCauseFromBulkItemResponse(BulkItemResponse bulkItemResponse) {
		if (!bulkItemResponse.isFailed()) {
			return null;
		} else {
			return bulkItemResponse.getFailure().getCause();
		}
	}

	@Override
	public void configureBulkProcessorBackoff(
					BulkProcessor.Builder builder,
					@Nullable ElasticsearchSinkBase.BulkFlushBackoffPolicy flushBackoffPolicy) {

		BackoffPolicy backoffPolicy;
		if (flushBackoffPolicy != null) {
			switch (flushBackoffPolicy.getBackoffType()) {
				case CONSTANT:
					backoffPolicy = BackoffPolicy.constantBackoff(
									new TimeValue(flushBackoffPolicy.getDelayMillis()),
									flushBackoffPolicy.getMaxRetryCount());
					break;
				case EXPONENTIAL:
				default:
					backoffPolicy = BackoffPolicy.exponentialBackoff(
									new TimeValue(flushBackoffPolicy.getDelayMillis()),
									flushBackoffPolicy.getMaxRetryCount());
			}
		} else {
			backoffPolicy = BackoffPolicy.noBackoff();
		}

		builder.setBackoffPolicy(backoffPolicy);
	}

	public RequestIndexer createRequestIndex(
					BulkProcessor bulkProcessor,
					boolean flushOnCheckpoint,
					AtomicLong numPendingRequests) {
		return new BulkProcessorIndexer(bulkProcessor, flushOnCheckpoint, numPendingRequests);
	}
}