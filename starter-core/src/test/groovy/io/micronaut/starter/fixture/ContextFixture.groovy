package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.starter.ConsoleOutput
import io.micronaut.starter.Options
import io.micronaut.starter.application.DefaultAvailableFeatures
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.AvailableFeatures
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.FeatureContext
import io.micronaut.starter.ContextFactory
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.validation.FeatureValidator
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo

trait ContextFixture {

    abstract BeanContext getBeanContext()

    Features getFeatures(List<String> features,
                         Language language = null,
                         TestFramework testFramework = null,
                         BuildTool buildTool = BuildTool.gradle) {
        Options options = new Options(language, testFramework, buildTool)
        FeatureContext featureContext = buildFeatureContext(features, options)
        featureContext.processSelectedFeatures()
        List<Feature> finalFeatures = featureContext.getFinalFeatures(ConsoleOutput.NOOP)
        beanContext.getBean(FeatureValidator).validate(featureContext.getOptions(), finalFeatures)
        return new Features(finalFeatures, options)
    }

    FeatureContext buildFeatureContext(List<String> selectedFeatures,
                                       Options options = new Options(null, null, BuildTool.gradle, VersionInfo.getJavaVersion())) {

        AvailableFeatures availableFeatures = beanContext.getBean(DefaultAvailableFeatures)
        ContextFactory factory = beanContext.getBean(ContextFactory)

        factory.createFeatureContext(availableFeatures,
                selectedFeatures,
                ApplicationType.DEFAULT,
                options)
    }

    GeneratorContext buildCommandContext(List<String> selectedFeatures,
                                         Options options = new Options(null, null, BuildTool.gradle, VersionInfo.getJavaVersion())) {
        if (this instanceof ProjectFixture) {
            ContextFactory factory = beanContext.getBean(ContextFactory)
            FeatureContext featureContext = buildFeatureContext(selectedFeatures, options)
            GeneratorContext commandContext = factory.createGeneratorContext(((ProjectFixture) this).buildProject(), featureContext, ConsoleOutput.NOOP)
            commandContext.applyFeatures()
            return commandContext
        } else {
            throw new IllegalStateException("Cannot get command context without implementing ProjectFixture")
        }
    }

}
