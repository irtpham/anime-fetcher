package com.irt.workout.anime.config;

import com.irt.workout.anime.model.Anime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "informations")
public class AnimeProperties {
    private List<Anime> animes = new ArrayList<>();

    public List<Anime> getAnimes() {
        return animes;
    }

    public void setAnimes(List<Anime> animes) {
        this.animes = animes;
    }
}
