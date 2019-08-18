package com.irt.workout.anime;

import com.irt.workout.anime.component.GogoAnimeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class AnimeFetcher {
    @Autowired
    private GogoAnimeComponent gogoAnimeComponent;

    public static void main(String[] args) {
        SpringApplication.run(AnimeFetcher.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> gogoAnimeComponent.getEpisodes();
    }
}