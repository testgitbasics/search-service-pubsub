package com.searchplatform.searchservice.config;

import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PushConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PubSubAdminConfig {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pubsub.topic}")
    private String topicId;

    @Value("${gcp.pubsub.subscription}")
    private String subscriptionId;

    @PostConstruct
    public void setupPubSub() {
        try {
            System.out.println("===== SEARCH SERVICE ADMIN CONFIG =====");
            System.out.println("Project ID: " + projectId);
            System.out.println("Topic ID: " + topicId);
            System.out.println("Subscription ID: " + subscriptionId);
            System.out.println("=======================================");

            // Topic
            try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {

                ProjectTopicName topicName =
                        ProjectTopicName.of(projectId, topicId);

                try {
                    topicAdminClient.createTopic(topicName);
                    System.out.println("Created topic: " + topicId);
                } catch (AlreadyExistsException e) {
                    System.out.println("Topic already exists: " + topicId);
                }
            }

            // Subscription
            try (SubscriptionAdminClient subscriptionAdminClient =
                         SubscriptionAdminClient.create()) {

                ProjectSubscriptionName subscriptionName =
                        ProjectSubscriptionName.of(projectId, subscriptionId);

                ProjectTopicName topicName =
                        ProjectTopicName.of(projectId, topicId);

                try {
                    subscriptionAdminClient.createSubscription(
                            subscriptionName,
                            topicName,
                            PushConfig.getDefaultInstance(),
                            10
                    );
                    System.out.println("Created subscription: " + subscriptionId);
                } catch (AlreadyExistsException e) {
                    System.out.println("Subscription already exists: " + subscriptionId);
                }
            }

        } catch (Exception e) {
            System.out.println("FAILED to setup Pub/Sub");
            e.printStackTrace();
        }
    }
}