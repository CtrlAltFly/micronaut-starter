/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.other;

import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class HttpClient implements DefaultFeature {

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, List<Feature> selectedFeatures) {
        return applicationType == ApplicationType.DEFAULT &&
                selectedFeatures.stream().anyMatch(feature -> feature.getName().equals(getName())) ||
                selectedFeatures.stream().noneMatch(feature -> feature instanceof FunctionFeature);
    }

    @Override
    public String getName() {
        return "http-client";
    }

    @Override
    public String getTitle() {
        return "Micronaut HTTP Client";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Micronaut HTTP client";
    }
}
