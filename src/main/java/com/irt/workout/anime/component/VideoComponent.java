package com.irt.workout.anime.component;

import java.io.IOException;

public interface VideoComponent {
    boolean getEpisode(String downloadPage, String localPath) throws IOException;
}
