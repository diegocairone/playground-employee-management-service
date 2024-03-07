package com.cairone.pg.storage;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.base.exception.AppServerException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class ContentStorage {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String packageName;
    private final String bucketName;

    public record ContentMetadata(long contentLength, String contentType){}
    public record ContentHolder(ContentMetadata contentMetadata, Resource resource){}

    public ContentStorage(
            S3Client s3Client,
            S3Presigner s3Presigner,
            @Value("${app.store.aws-bucket-name}") String bucketName,
            @Value("${app.store.package-name:}") String packageName) {

        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.packageName = packageName;
    }

    public boolean nonExistsById(UUID objectKey) {
        return !existsById(objectKey);
    }

    public boolean existsById(UUID objectKey) {
        try {
            HeadObjectResponse headObjectResponse = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(getObjectKey(objectKey))
                    .build());

            return headObjectResponse != null;
        } catch (S3Exception e) {
            if (e.awsErrorDetails().errorCode().equals("NoSuchKey")) {
                return false;
            }
            throw new AppServerException.Builder()
                    .withMessage("Error verifying existence post content into Store")
                    .withTechnicalMessage(e.getMessage())
                    .withCause(e)
                    .build();
        }
    }

    public URL uploadContent(UUID contentId, byte[] avatar, Consumer<Tag.Builder>... var1) {

        ContentMetadata metadata = getMetadata(avatar);
        InputStream content = new ByteArrayInputStream(avatar);

        Tagging tagging = Tagging.builder()
                .tagSet(var1)
                .build();

        s3Client.putObject(
                builder -> builder
                        .bucket(bucketName)
                        .key(getObjectKey(contentId))
                        .tagging(tagging)
                        .contentType(metadata.contentType),
                RequestBody.fromInputStream(content, metadata.contentLength));

        return getPresignedUrl(contentId);
    }

    public Optional<ContentHolder> downloadContent(UUID contentId) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(getObjectKey(contentId))
                .build();

        try {
            ResponseInputStream<GetObjectResponse> is = s3Client.getObject(request);
            GetObjectResponse response = is.response();

            InputStreamResource isr = new InputStreamResource(is);
            ContentHolder contentHolder = new ContentHolder(
                    new ContentMetadata(response.contentLength(), response.contentType()), isr);

            return Optional.of(contentHolder);

        } catch (S3Exception e) {
            if (e.awsErrorDetails().errorCode().equals("NoSuchKey")) {
                return Optional.empty();
            }
            throw new AppServerException.Builder()
                    .withMessage("Error trying to find by ID post content into Store")
                    .withTechnicalMessage(e.getMessage())
                    .withCause(e)
                    .build();
        }
    }

    public void removeById(UUID objectKey) {

        if (!existsById(objectKey)) {
            throw new AppClientException("Post with ID %s not found", objectKey);
        }

        try {
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(getObjectKey(objectKey)));
        } catch (S3Exception e) {
            throw new AppServerException.Builder()
                    .withMessage("Error deleting post content from Store")
                    .withTechnicalMessage(e.getMessage())
                    .withCause(e)
                    .build();
        }
    }

    public URL getPresignedUrl(UUID contentId) {
        return getPresignedUrl(contentId, Duration.ofMinutes(1));
    }

    public URL getPresignedUrl(UUID contentId, Duration signatureDuration) {

        if (!existsById(contentId)) {
            return null;
        }

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(signatureDuration)
                .getObjectRequest(builder -> builder
                        .bucket(bucketName)
                        .key(getObjectKey(contentId)))
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedGetObjectRequest.url();
    }

    private ContentMetadata getMetadata(byte[] data) {

        try (TikaInputStream tis = TikaInputStream.get(data)) {

            AutoDetectParser parser = new AutoDetectParser();
            ContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            parser.parse(tis, handler, metadata);

            long lengthInBytes = tis.getLength();
            String contentType = metadata.get(HttpHeaders.CONTENT_TYPE);

            return new ContentMetadata(lengthInBytes, contentType);

        } catch (IOException | SAXException | TikaException e) {
            throw new AppServerException.Builder()
                    .withMessage("Error when trying to process uploaded image")
                    .withTechnicalMessage(e.getMessage())
                    .withCause(e)
                    .build();
        }
    }

    private String getObjectKey(UUID contentId) {
        if (packageName == null || packageName.isBlank()) {
            return contentId.toString();
        }
        return String.format("%s/%s", packageName, contentId.toString());
    }
}
