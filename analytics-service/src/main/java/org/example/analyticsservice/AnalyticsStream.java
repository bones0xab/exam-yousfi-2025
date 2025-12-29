package org.example.analyticsservice;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;

;

@Configuration
@EnableKafkaStreams
public class AnalyticsStream {

    @Bean
    public KStream<String, OrderAnalyticsDTO> kStream(StreamsBuilder builder) {

        // 1. Serdes: Simple one-liners (No helper method needed in Boot 3.2.5)
        Serde<OrderAnalyticsDTO> orderSerde = jsonSerde(OrderAnalyticsDTO.class);
        Serde<AnalyticsResult> resultSerde = new JsonSerde<>(AnalyticsResult.class);

        // 2. Consume Stream
        KStream<String, OrderAnalyticsDTO> stream = builder.stream(
                "orders-topic",
                Consumed.with(Serdes.String(), orderSerde)
        );

        // 3. Process Stream
        stream
                .map((k, v) -> new KeyValue<>("GLOBAL_STATS", v))
                .groupByKey(Grouped.with(Serdes.String(), orderSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(5))) // Stable Window API
                .aggregate(
                        AnalyticsResult::new,
                        (key, value, aggregate) -> {
                            aggregate.setCount(aggregate.getCount() + 1);
                            aggregate.setTotalRevenue(aggregate.getTotalRevenue() + value.getPrice());
                            return aggregate;
                        },
                        Materialized.with(Serdes.String(), resultSerde)
                )
                .toStream()
                .foreach((key, value) -> {
                    System.out.println("== WINDOW: " + key.window().start() + " to " + key.window().end() + " ==");
                    System.out.println("Count: " + value.getCount() + " | Revenue: " + value.getTotalRevenue());
                });

        return stream;
    }
    public <T> Serde<T> jsonSerde(Class<T> targetType) {
        JsonSerializer<T> serializer = new JsonSerializer<>();
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(targetType);
        deserializer.addTrustedPackages("*"); // Important: Trusts your DTOs
        deserializer.setUseTypeHeaders(false); // Avoids header conflicts
        return Serdes.serdeFrom(serializer, deserializer);
    }
}}}