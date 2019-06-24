package com.irt.workout.anime;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnimeFetcher {
    public static final Logger logger = Logger.getLogger(AnimeFetcher.class.getClass().getName());
    public static void main(String[] args) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL("").openStream())) {
            FileOutputStream fileOutputStream = new FileOutputStream("");
            byte dataBuffer[] = new byte[1024];
            int byteReads;
            while ((byteReads = inputStream.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, byteReads);
            }
        } catch (MalformedURLException e) {
            logger.log(Level.FINER, "URL exception: {}", e.getCause());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception: {}", e.getCause());
        }
    }
}
