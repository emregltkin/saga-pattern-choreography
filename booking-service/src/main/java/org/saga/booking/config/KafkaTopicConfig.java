package org.saga.booking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.booking.saga.constants.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic bookingCreatedEventsTopic() {
        return TopicBuilder.name(KafkaConstants.Topics.BOOKING_CREATED)
                .partitions(3).replicas(1).build();
        //return new NewTopic(KafkaConstants.Topics.BOOKING_CREATED_TOPIC, 3, (short) 1);
    }
}
