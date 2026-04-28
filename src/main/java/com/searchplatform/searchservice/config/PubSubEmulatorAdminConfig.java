package com.searchplatform.searchservice.config;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Topic;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
public class PubSubEmulatorAdminConfig {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pubsub.emulator-host}")
    private String emulatorHost;

    @Value("${gcp.pubsub.topic}")
    private String topicId;

    @Value("${gcp.pubsub.subscription}")
    private String subscriptionId;

    private ManagedChannel topicChannel;
    private ManagedChannel subscriptionChannel;

    @PostConstruct
    public void setupPubSub() {
        try {
            System.out.println("===== SEARCH SERVICE ADMIN CONFIG =====");
            System.out.println("Project ID: " + projectId);
            System.out.println("Topic ID: " + topicId);
            System.out.println("Subscription ID: " + subscriptionId);
            System.out.println("Emulator Host: " + emulatorHost);
            System.out.println("=======================================");

            // Topic Admin Client
            topicChannel = ManagedChannelBuilder
                    .forTarget(emulatorHost)
                    .usePlaintext()
                    .build();

            TopicAdminSettings topicAdminSettings =
                    TopicAdminSettings.newBuilder()
                            .setTransportChannelProvider(
                                    FixedTransportChannelProvider.create(
                                            GrpcTransportChannel.create(topicChannel)
                                    )
                            )
                            .setCredentialsProvider(NoCredentialsProvider.create())
                            .build();

            try (TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings)) {
                ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

                try {
                    topicAdminClient.createTopic(topicName);
                    System.out.println("Created topic: " + topicId);
                } catch (AlreadyExistsException e) {
                    System.out.println("Topic already exists: " + topicId);
                }

                Topic topic = topicAdminClient.getTopic(topicName);
                System.out.println("Verified topic exists: " + topic.getName());
            }

            // Subscription Admin Client
            subscriptionChannel = ManagedChannelBuilder
                    .forTarget(emulatorHost)
                    .usePlaintext()
                    .build();

            SubscriptionAdminSettings subscriptionAdminSettings =
                    SubscriptionAdminSettings.newBuilder()
                            .setTransportChannelProvider(
                                    FixedTransportChannelProvider.create(
                                            GrpcTransportChannel.create(subscriptionChannel)
                                    )
                            )
                            .setCredentialsProvider(NoCredentialsProvider.create())
                            .build();

            try (SubscriptionAdminClient subscriptionAdminClient =
                         SubscriptionAdminClient.create(subscriptionAdminSettings)) {

                ProjectSubscriptionName subscriptionName =
                        ProjectSubscriptionName.of(projectId, subscriptionId);
                ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

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

                System.out.println("Verified subscription exists: " + subscriptionName.toString());
            }

        } catch (Exception e) {
            System.out.println("FAILED to setup Pub/Sub emulator resources");
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void shutdown() {
        if (topicChannel != null) {
            topicChannel.shutdownNow();
        }
        if (subscriptionChannel != null) {
            subscriptionChannel.shutdownNow();
        }
    }
}