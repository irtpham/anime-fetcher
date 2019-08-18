package com.irt.workout.anime.component;

import com.irt.workout.anime.config.AnimeProperties;
import com.irt.workout.anime.model.Anime;
import com.irt.workout.anime.utils.FileUtils;
import com.irt.workout.anime.utils.HtmlParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GogoAnimeComponent implements VideoComponent {
    public static Logger LOG = LoggerFactory.getLogger(GogoAnimeComponent.class);

    @Autowired
    private AnimeProperties animeProperties;

    @Value("${localPath}")
    private String localPath;

    @Value("${currentEpisodeJsonFile}")
    private String currentEpisodeJsonFile;

    @Value("${dayOfWeek}")
    String dayOfWeek;

    public boolean getEpisode(String animePage, String episodePath) {
        String downloadPage = HtmlParserUtils.getGogoDownloadPageLink(animePage);
        String episodeDownloadLink = HtmlParserUtils.getGogoDownloadLinkFromGogoAnime(downloadPage);

        if (episodeDownloadLink == null) {
            LOG.warn("Cannot get download link from page {}", downloadPage);
            return false;
        }

        return FileUtils.saveTo(episodeDownloadLink, episodePath);
    }

    public void getEpisodes() {
        if (animeProperties == null) {
            LOG.warn("There is no configuration properties for animes. Please check!");
        }

        if (Paths.get(currentEpisodeJsonFile).toFile().exists()) {
            FileUtils.updateCurrentEpisodeJsonFile(currentEpisodeJsonFile, animeProperties.getAnimes(),
                    FileUtils.loadCurrentEpisodeFromJsonFile(currentEpisodeJsonFile));
        }
        else {
            FileUtils.createEpisodeJsonFile(currentEpisodeJsonFile, animeProperties.getAnimes()); //NOSONAR
        }

        Map<String, Integer> currentEpisodeMap = FileUtils.loadCurrentEpisodeFromJsonFile(currentEpisodeJsonFile);
        List<Anime> processAnimes = animeProperties.getAnimes().stream() //NOSONAR
                .filter(anime -> getShortNameDayOfWeekNow().equals(anime.getDayOfWeekRelease())
                        || "NONE".equals(anime.getDayOfWeekRelease()))
                .collect(Collectors.toList());

        boolean downloaded;
        String episodeName;
        int currentEpisode;
        for (Anime anime : processAnimes) {
            LOG.debug("Gogoanime processing the episode {}", anime);
            if (anime.getMaxEpisode() == currentEpisodeMap.get(anime.getName())) {
                LOG.info("The anime {} was completed. Nothing to do.", anime.getName());
                continue;
            }

            currentEpisode = currentEpisodeMap.get(anime.getName());
            episodeName = new StringBuilder(anime.getName()).append("-ep-").append(currentEpisode).toString();

            LOG.info("Downloading {} ...", episodeName);
            downloaded = getEpisode(anime.getAnimePage() + currentEpisode,
                    FileUtils.buildFilePathString(localPath, episodeName));

            if (downloaded) {
                LOG.info("{} was downloaded.", episodeName);
                currentEpisodeMap.put(anime.getName(), currentEpisode + 1);
            }
        }

        FileUtils.updateCurrentEpisodeJsonFile(currentEpisodeJsonFile, animeProperties.getAnimes(), currentEpisodeMap);
    }

    private String getShortNameDayOfWeekNow() {
        return dayOfWeek == null ?
                LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase() : dayOfWeek;
    }
}
