package com.irt.workout.anime.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlParserUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HtmlParserUtils.class);

    private HtmlParserUtils() {
        throw new IllegalStateException("HtmlParserUtils is utility class");
    }

    public static String getGogoDownloadLinkFromGogoAnime(String episodeUrl) {
        InputStream iStream = FileUtils.getStreamData(episodeUrl);
        if (iStream == null) {
            return null;
        }

        List<Pattern> patterns = Stream.of(
                "href=\"(.+title=\\(1080 - mp4\\).+?)\"",
                "href=\"(.+title=\\(720P - mp4\\).+?)\"",
                "href=\"(.+title=\\(480P - mp4\\).+?)\"",
                "href=\"(.+title=\\(360P - mp4\\).+?)\"")
                .map(Pattern::compile).collect(Collectors.toList());

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
        } catch (Exception e) {
            LOG.error("HtmlParserUtils cannot get download link: {}", e.getMessage());
        }

        return null;
    }

    public static String getGogoDownloadPageLink(String animePage) {
        final String patternString = "<a href=\"(.+?)\"\\s*target=\"_blank\">.+Download.+</a>";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(FileUtils.getStreamData(animePage))))) {
            String pageContent = br.lines().collect(Collectors.joining(System.lineSeparator()));
            Matcher matcher = Pattern.compile(patternString).matcher(pageContent);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        catch (Exception e) {
            LOG.error("HtmlParserUtils cannot get download page: {}", e.getMessage());
        }
        return null;
    }
}
