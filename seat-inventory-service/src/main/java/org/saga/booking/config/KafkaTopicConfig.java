package org.saga.booking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.booking.saga.constants.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic seatLockingEventsTopic() {
        return TopicBuilder.name(KafkaConstants.Topics.SEAT_LOCKING)
                .partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic seatReleasedEventsTopic() {
        return TopicBuilder.name(KafkaConstants.Topics.SEAT_RELEASED)
                .partitions(3).replicas(1).build();
    }
}
