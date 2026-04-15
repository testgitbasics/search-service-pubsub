package com.searchplatform.searchservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.searchplatform.searchservice.model.ProductCreatedEvent;
import com.searchplatform.searchservice.model.SearchProductDocument;
import com.searchplatform.searchservice.service.SearchEngine;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductEventConsumer {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pubsub.subscription}")
    private String subscriptionId;

    @Value("${gcp.pubsub.emulator-host}")
    private String emulatorHost;

    private final SearchEngine searchEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Subscriber subscriber;
    private ManagedChannel channel;

    public ProductEventConsumer(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    @PostConstruct
    public void startSubscriber() {
        try {
            System.out.println("===== SEARCH SERVICE PUBSUB CONFIG =====");
            System.out.println("Project ID: " + projectId);
            System.out.println("Subscription ID: " + subscriptionId);
            System.out.println("Emulator Host: " + emulatorHost);
            System.out.println("========================================");

            ProjectSubscriptionName subscriptionName =
                    ProjectSubscriptionName.of(projectId, subscriptionId);

            MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
                try {
                    String payload = message.getData().toStringUtf8();
                    System.out.println("Received Pub/Sub message: " + payload);

                    ProductCreatedEvent event =
                            objectMapper.readValue(payload, ProductCreatedEvent.class);

                    consume(event);

                    consumer.ack();
                    System.out.println("ACK sent for productId: " + event.getData().getId());

                } catch (Exception e) {
                    System.out.println("Failed to process Pub/Sub message");
                    e.printStackTrace();
                    consumer.nack();
                    System.out.println("NACK sent. Message will be retried.");
                }
            };

            channel = ManagedChannelBuilder
                    .forTarget(emulatorHost)
                    .usePlaintext()
                    .build();

            subscriber = Subscriber.newBuilder(subscriptionName, receiver)
                    .setChannelProvider(
                            FixedTransportChannelProvider.create(
                                    GrpcTransportChannel.create(channel)
                            )
                    )
                    .setCredentialsProvider(NoCredentialsProvider.create())
                    .build();

            subscriber.startAsync().awaitRunning();

            System.out.println("Pub/Sub subscriber started for: " + subscriptionId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to start Pub/Sub subscriber", e);
        }
    }

    public void consume(ProductCreatedEvent event) {
        SearchProductDocument doc = new SearchProductDocument(
                event.getData().getId().toString(),
                event.getData().getSku(),
                event.getData().getName(),
                event.getData().getDescription(),
                event.getData().getBrand(),
                event.getData().getCategory(),
                event.getData().getPrice(),
                event.getData().getRating()
        );

        searchEngine.indexProduct(doc);
        System.out.println("Indexed into Elasticsearch: " + doc.getId());
    }

    @PreDestroy
    public void stopSubscriber() {
        try {
            if (subscriber != null) {
                subscriber.stopAsync().awaitTerminated();
            }
            if (channel != null) {
                channel.shutdownNow();
            }
            System.out.println("Pub/Sub subscriber stopped.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}