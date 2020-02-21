package com.irt.workout.anime.model;

public class Anime {
    private String name;
    private int maxEpisode;
    private int currentEpisode;
    private String animePage;
    private String dayOfWeekRelease;

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

    public String toString() {
        return String.format("{\"name\":\"%s\", \"maxEpisode\":%d, \"currentEpisode\":%d, \"animePage\":\"%s\", \"dayOfWeekRelease\":\"%s\"}",
                name, maxEpisode, currentEpisode, animePage, dayOfWeekRelease);
    }

    public String toAnimeInfoJson() {
        return String.format("\t{" +
                "\n\t\t\"name\":\"%s\"," +
                "\n\t\t\"maxEpisode\":%d," +
                "\n\t\t\"currentEpisode\":%d," +
                "\n\t\t\"animePage\":\"%s\"," +
                "\n\t\t\"dayOfWeekRelease\":\"%s\"" +
                "\n\t}", name, maxEpisode, currentEpisode, animePage, dayOfWeekRelease);
    }
}
