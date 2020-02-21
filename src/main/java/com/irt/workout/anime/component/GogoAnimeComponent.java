package com.irt.workout.anime.component;

import com.irt.workout.anime.model.Anime;
import com.irt.workout.anime.utils.FileUtils;
import com.irt.workout.anime.utils.HtmlParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@Component
public class GogoAnimeComponent implements VideoComponent {
    private static final Logger LOG = LoggerFactory.getLogger(GogoAnimeComponent.class);

    @Value("${localPath}")
    private String localPath;

    @Value("${animeInfoFilePath}")
    private String animeInfoFilePath;

    public boolean getEpisode(String animePage, String episodePath) {
        String downloadPage = HtmlParserUtils.getGogoDownloadPageLink(animePage);
        String episodeDownloadLink = HtmlParserUtils.getGogoDownloadLinkFromGogoAnime(downloadPage);
        return FileUtils.saveTo(episodeDownloadLink, episodePath);
    }

    public void getEpisodes() {
        if (!Paths.get(animeInfoFilePath).toFile().exists()) {
            LOG.info("The configuration file not found.");
            return;
        }

        List<Anime> animeInformations = FileUtils.loadAnimeInfoFromJsonFile(animeInfoFilePath);

        animeInformations.forEach(processingAnime -> {
            LOG.debug("Gogoanime processing the episode {}", processingAnime);
            if (processingAnime.getCurrentEpisode() > processingAnime.getMaxEpisode()) {
                LOG.info("The anime {} was completed. Nothing to do.", processingAnime.getName());
                return;
            }

            String episodeName = String.format("%s-ep-%s", processingAnime.getName(), processingAnime.getCurrentEpisode());
            LOG.info("Downloading {} ...", episodeName);
            boolean downloaded = getEpisode(processingAnime.getAnimePage() + processingAnime.getCurrentEpisode(),
                    FileUtils.buildFilePathString(localPath, episodeName));

            if (downloaded) {
                LOG.info("{} was downloaded.", episodeName);
                processingAnime.setCurrentEpisode(processingAnime.getCurrentEpisode() + 1);
            }
        });

        FileUtils.createAnimeInfoJsonFile(animeInfoFilePath, animeInformations);
    }
}
