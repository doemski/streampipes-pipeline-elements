<!--

  Copyright 2018 FZI Forschungszentrum Informatik

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

-->

## Name finder

<p align="center"> 
    <img src="icon.png" width="150px;" class="pe-image-documentation"/>
</p>

***

## Description

Loads a trained model which finds names like locations or organizations.

A list of trained models can be found [here](http://opennlp.sourceforge.net/models-1.5/).\
A guide on how to train a new model can be found [here](https://opennlp.apache.org/docs/1.9.1/manual/opennlp.html#tools.namefind.training).

***

## Required input

A stream with a list of tokens from a text.

***

## Configuration

Configure the Name finder so that the tokens are assigned to the "List of Tokens" property

#### Model parameter

The trained model which should be used to find the names.

## Output

Appends a string list property to the stream which contains all found names.

**Example (with an loaded english person-name-model):**

Input: `(tokens: ["Hi", "John", "Doe", "is", "here"])`

Output: `(tokens: ["Hi", "John", "Doe", "is", "here"], foundNames: ["John Doe"])`
