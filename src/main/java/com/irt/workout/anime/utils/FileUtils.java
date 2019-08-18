package com.irt.workout.anime.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irt.workout.anime.model.Anime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * https://www.baeldung.com/java-download-file
 * The transferFrom() methods are more efficient than simply reading from a stream using a buffer.
 * Depending on the underlying operating system, the data can be transferred directly from the filesystem cache
 * to our file without copying any bytes into the application memory.
 */

public class FileUtils {
    public static Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    public static boolean saveTo(String fromUrlStr, String toLocalPath) {
        try (ReadableByteChannel readableChannel = Channels.newChannel(getStreamData(fromUrlStr));
             FileOutputStream fileOutput = new FileOutputStream(toLocalPath);
             FileChannel fChannel = fileOutput.getChannel())
        {
            fChannel.transferFrom(readableChannel, 0, Long.MAX_VALUE);
            return true;
        } catch (IOException e) {
            LOG.error("FileUtils got the error while create chanel.", e);
        }
        return false;
    }

    public static InputStream getStreamData(String urlStr) {
        if (urlStr == null) {
            LOG.warn("FileUtils tries getting stream data from null URL.");
            return null;
        }

        try {
            URL fromUrl = new URL(urlStr);
            HttpURLConnection httpURLConn = (HttpURLConnection) fromUrl.openConnection();
            httpURLConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            httpURLConn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            return httpURLConn.getInputStream();
        }
        catch (MalformedURLException e) {
            LOG.error(String.format("FileUtils got the error while init the URL %s.", urlStr), e);
        } catch (IOException e) {
            LOG.error(String.format("FileUtils got the error while open connection to url %s.", urlStr), e);
        }

        return null;
    }

    public static String buildFilePathString(String localPath, String fileName) {
        return String.format("%s%s%s.mp4", localPath, File.separator, fileName);
    }

    public static void createEpisodeJsonFile(String path, List<Anime> animes) {
        try (FileWriter fWriter = new FileWriter(path)){
            String currentEpisodeJson = animes.stream().map(Anime::toCurrentEpisodeJson)
                    .collect(Collectors.joining("," + System.lineSeparator(),"[" + System.lineSeparator(), System.lineSeparator() + "]"));
            fWriter.write(currentEpisodeJson);
        } catch (IOException e) {
            LOG.error("FileUtils cannot create current episode json file.", e);
        }
    }

    public static void updateCurrentEpisodeJsonFile(String path, List<Anime> animes, Map<String, Integer> currentEpisodes) {
        String backupFilePath = path + ".bk";
        Paths.get(path).toFile().renameTo(new File(backupFilePath));

        for (int i = 0; i < animes.size(); i++) {
            Anime animeInfo = animes.get(i);
            int currentEpisodeValue = currentEpisodes.get(animeInfo.getName()) == null ? animeInfo.getCurrentEpisode() : currentEpisodes.get(animeInfo.getName());
            animeInfo.setCurrentEpisode(currentEpisodeValue);
        }
        createEpisodeJsonFile(path, animes);
    }

    public static Map<String, Integer> loadCurrentEpisodeFromJsonFile(String path) {
        Map<String, Integer> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Anime[]animes = mapper.readValue(new File(path), Anime[].class);
            for (Anime anime : animes) {
                result.put(anime.getName(), anime.getCurrentEpisode());
            }
        } catch (IOException e) {
            LOG.error("FileUtils got error while load data from json file.", e);
        }
        return result;
    }
}
