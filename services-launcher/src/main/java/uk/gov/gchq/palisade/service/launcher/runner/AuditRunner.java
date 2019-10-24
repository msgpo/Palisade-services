/*
 * Copyright 2019 Crown Copyright
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

package uk.gov.gchq.palisade.service.launcher.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import uk.gov.gchq.palisade.service.audit.AuditApplication;
import uk.gov.gchq.palisade.service.launcher.config.AuditConfiguration;

@Component
public class AuditRunner implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditRunner.class);

    @Autowired
    AuditConfiguration configuration;

    private static ConfigurableApplicationContext context;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        if (args.getOptionNames().contains(configuration.getName()) || configuration.isEnabled()) {
            Thread thread = new Thread(() -> {
                context.close();
                context = SpringApplication.run(AuditApplication.class, args.getSourceArgs());
            });
            thread.start();
        }
    }
}
