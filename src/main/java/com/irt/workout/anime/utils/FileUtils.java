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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://www.baeldung.com/java-download-file
 * The transferFrom() methods are more efficient than simply reading from a stream using a buffer.
 * Depending on the underlying operating system, the data can be transferred directly from the filesystem cache
 * to our file without copying any bytes into the application memory.
 */

public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
        throw new IllegalStateException("FileUtils is utility class");
    }

    public static boolean saveTo(String fromUrlStr, String toLocalPath) {
        try (ReadableByteChannel readableChannel = Channels.newChannel(Objects.requireNonNull(getStreamData(fromUrlStr)));
             FileOutputStream fileOutput = new FileOutputStream(toLocalPath);
             FileChannel fChannel = fileOutput.getChannel())
        {
            fChannel.transferFrom(readableChannel, 0, Long.MAX_VALUE);
            return true;
        } catch (Exception e) {
            LOG.error("FileUtils got the error while create chanel: {}", e.getMessage());
        }
        return false;
    }

    public static InputStream getStreamData(String urlStr) {
        if (urlStr == null) {
            LOG.warn("Cannot get stream data from null URL.");
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
        } catch (Exception e) {
            LOG.error(String.format("FileUtils got the error while open connection to url %s.", urlStr), e);
        }

        return null;
    }

    public static String buildFilePathString(String localPath, String fileName) {
        return String.format("%s%s%s.mp4", localPath, File.separator, fileName);
    }

    public static void createAnimeInfoJsonFile(String path, List<Anime> animes) {
        try (FileWriter fWriter = new FileWriter(path)){
            String animeJsonFile = animes.stream()
                    .map(Anime::toAnimeInfoJson)
                    .collect(Collectors.joining("," + System.lineSeparator(),"[" + System.lineSeparator(), System.lineSeparator() + "]"));
            fWriter.write(animeJsonFile);
        } catch (IOException e) {
            LOG.error("FileUtils cannot create anime information file.", e);
        }
    }

    public static List<Anime> loadAnimeInfoFromJsonFile(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return Stream.of(mapper.readValue(new File(path), Anime[].class))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("FileUtils got error while load data from json file.", e);
        }
        return Collections.emptyList();
    }
}
