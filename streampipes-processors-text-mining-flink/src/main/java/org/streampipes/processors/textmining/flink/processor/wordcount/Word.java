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

public class Word {

  private String word;
  private int count;

  public Word() {

  }

  public Word(String word, int count) {
    super();
    this.word = word;
    this.count = count;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getCount() {
    return count;
  }


  public void setCount(int count) {
    this.count = count;
  }


}
