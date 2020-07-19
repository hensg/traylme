package me.trayl.streams;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;

@EnableKafkaStreams
@SpringBootApplication
@EntityScan(basePackages = {"me.trayl.common.dao"})
@EnableJpaRepositories(basePackages = {"me.trayl.common.dao"})
public class RedirectProcessor {

    private final static String TOPIC = "redirect-log";

    @Autowired
    private UrlRecentHitsService urlRecentHitsService;

    public static void main(String[] args) {
        SpringApplication.run(RedirectProcessor.class, args);
    }

    @Bean
    KStream<Windowed<Long>, Long> countAgg(final StreamsBuilder builder) {
        final KStream<Long, Long> stream = builder.stream(TOPIC);
        final KStream<Windowed<Long>, Long> countAgg = stream
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofSeconds(1)))
                .count()
                .toStream();

        countAgg.foreach((window, aggCountWin) -> {
            urlRecentHitsService.updateHitCount(window.key(), window.window().startTime(), aggCountWin, 1);
        });
        countAgg.print(Printed.<Windowed<Long>, Long>toSysOut().withLabel("Running count"));
        return countAgg;
    }
}
