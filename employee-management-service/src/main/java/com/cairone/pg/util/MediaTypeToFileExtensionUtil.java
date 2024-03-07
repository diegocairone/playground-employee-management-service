package com.cairone.pg.util;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MediaTypeToFileExtensionUtil {

    private MediaTypeToFileExtensionUtil() {
        // private constructor to prevent instantiation
    }

    private static final Map<MediaType, String> mediaTypeToFileExtensionMap = new HashMap<>();

    static {
        mediaTypeToFileExtensionMap.put(MediaType.IMAGE_JPEG, ".jpeg");
        mediaTypeToFileExtensionMap.put(MediaType.IMAGE_PNG, ".png");
        mediaTypeToFileExtensionMap.put(MediaType.IMAGE_GIF, ".gif");
    }

    public static String getExtension(MediaType mediaType) {
        // default to .dat if media type is not recognized
        return mediaTypeToFileExtensionMap.getOrDefault(mediaType, ".dat");
    }
}
