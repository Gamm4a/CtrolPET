package com.example.ctrolpet.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class Config {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "CtrolPET";

    private static MongoDatabase database;

    static {
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new com.mongodb.ConnectionString(URI))
                .codecRegistry(pojoCodecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(DB_NAME);
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    @ReadingConverter
    public class StringALocalTime implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String hora) {
            if (hora == null || hora.isEmpty()) {
                return null;
            }

            return LocalTime.parse(hora, DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(new StringALocalTime()));
    }
}
