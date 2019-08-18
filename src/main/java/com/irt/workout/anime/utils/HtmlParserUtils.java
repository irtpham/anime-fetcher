package com.irt.workout.anime.utils;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HtmlParserUtils {
    public static final Logger LOG = LoggerFactory.getLogger(HtmlParserUtils.class);

    public static String getGogoDownloadLinkFromGogoAnime(String episodeUrl) {
        InputStream iStream = FileUtils.getStreamData(episodeUrl);
        if (iStream == null) {
            return null;
        }

        List<Pattern> patterns = Arrays.asList(
                "href=\"(.+title=\\(orginalP - mp4\\).+?)\"",
                "href=\"(.+title=\\(720P - mp4\\).+?)\"")
                .stream().map(Pattern::compile).collect(Collectors.toList());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(iStream))) {
            String pageContent = br.lines().collect(Collectors.joining(System.lineSeparator()))
                    .replace("  ", "");

            for (Pattern pattern : patterns) {
                Matcher elementMatcher = pattern.matcher(pageContent);
                if (elementMatcher.find()) {
                    return elementMatcher.group(1)
                        .replace(" ", "%20").replace("amp;", "");
                }
            }
        } catch (IOException e) {
            LOG.error("HtmlParserUtils cannot get download link.", e);
        }

        return null;
    }

    public static String getGogoDownloadPageLink(String animePage) {
        final String patternString = "<a href=\"(.+?)\"\\s*target=\"_blank\">.+Download.+<\\/a>";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(FileUtils.getStreamData(animePage)))) {
            String pageContent = br.lines().collect(Collectors.joining(System.lineSeparator()));
            Matcher matcher = Pattern.compile(patternString).matcher(pageContent);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        catch (IOException e) {
            LOG.error("HtmlParserUtils cannot get download page.", e);
        }
        return null;
    }
}
