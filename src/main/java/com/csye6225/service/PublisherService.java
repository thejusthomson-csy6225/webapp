package com.csye6225.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
@Service
public class PublisherService {
    public PublisherService() {
    }
    final Logger logger = LoggerFactory.getLogger(PublisherService.class);
    String projectId = "test-cloud-csye6225";
    String topicId = "verify-email";
    public void publishVerificationLink(String username) throws IOException, ExecutionException, InterruptedException{

        logger.debug("Inside publishVerificationLink, username is: "+username);
        TopicName topicName = TopicName.of(projectId, topicId);
        logger.debug("Topic Name is: "+topicName);
        Publisher publisher = null;
        try {
            publisher = Publisher.newBuilder(topicName).build();
            logger.debug("Retrieved Publisher");
            ByteString data = ByteString.copyFromUtf8(username);
            logger.debug("ByteString data: "+data);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            logger.debug("pubsubMessage is: "+pubsubMessage);
            // Once published, returns a server-assigned message id (unique within the topic)
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            logger.debug("messageIdFuture is: "+messageIdFuture);
            String messageId = messageIdFuture.get();
            logger.debug("Published messageId is: "+messageId);
            System.out.println("Published message ID: " + messageId);
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                logger.info("Finally block...Waiting for shutdown");
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
                logger.info("Shutdown Completed!");
            }
        }
    }
}
