/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.google.ce.media.functanalytics;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.TopicName;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created in gcs-uploader on 2/11/20.
 */
@Component
public class TopicPublishers {
  private final GcpProjectIdProvider projectIdProvider;
  private final EnvConfig envConfig;
  private final boolean isPublishing;

  private Publisher uploadNotificationPublisher;

  public TopicPublishers(GcpProjectIdProvider projectIdProvider, EnvConfig envConfig) {
    this.projectIdProvider = projectIdProvider;
    this.envConfig = envConfig;
    this.isPublishing = envConfig.getUploadNotificationTopic() != null;
  }

  @PostConstruct
  private void init() throws IOException {
    if(isPublishing) {
      TopicName uploadTopic = ProjectTopicName.of(projectIdProvider.getProjectId(),
                                                  envConfig.getUploadNotificationTopic());
      uploadNotificationPublisher = Publisher.newBuilder(uploadTopic).build();
    }
  }

  public Publisher getUploadNotificationPublisher() {
    return uploadNotificationPublisher;
  }

  public boolean isPublishing() {
    return isPublishing;
  }
}
