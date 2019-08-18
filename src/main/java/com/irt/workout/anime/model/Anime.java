package com.irt.workout.anime.model;

public class Anime {
    private String name;
    private int maxEpisode;
    private int currentEpisode;
    private String animePage;
    private String dayOfWeekRelease;
    private String status;

    public Anime() { this.currentEpisode = 0; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxEpisode() {
        return maxEpisode;
    }

    public void setMaxEpisode(int maxEpisode) {
        this.maxEpisode = maxEpisode;
    }

    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public String getAnimePage() {
        return animePage;
    }

    public void setAnimePage(String animePage) {
        this.animePage = animePage;
    }

    public String getDayOfWeekRelease() {
        return dayOfWeekRelease;
    }

    public void setDayOfWeekRelease(String dayOfWeekRelease) {
        this.dayOfWeekRelease = dayOfWeekRelease;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return String.format("Anime={name:%s, max-episode:%d, current-episode:%d, base-link:%s, release:%s, status:%s}",
                name, maxEpisode, currentEpisode, animePage, dayOfWeekRelease, status);
    }

    public String toCurrentEpisodeJson() {
        return String.format("{\"name\":\"%s\", \"currentEpisode\":%s}", name, currentEpisode);
    }
}
