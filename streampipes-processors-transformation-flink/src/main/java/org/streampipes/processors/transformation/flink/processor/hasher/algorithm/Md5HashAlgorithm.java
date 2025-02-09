/*
 * Copyright 2018 FZI Forschungszentrum Informatik
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
 *
 */

package org.streampipes.processors.transformation.flink.processor.hasher.algorithm;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5HashAlgorithm implements HashAlgorithm {

	private static final long serialVersionUID = 1L;

	@Override
	public String toHashValue(Object value) {
		return DigestUtils.md5Hex(String.valueOf(value));
	}

}
