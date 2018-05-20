package rudra.cron;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import rudra.service.store.AwsS3Store;

@Component
public class ThumbnailGenerator {

    private static final Logger logger = LoggerFactory.getLogger( ThumbnailGenerator.class );

    private static final String DELIMITER = "/";

    private static final String TEMP_STORAGE_DIR = "/tmp/";

    private static final String THUMBNAIL_GROUP = "Thumbnail";

    @Autowired
    private AwsS3Store awsS3Store;

    @Autowired
    private AmazonS3 amazonS3;

    @Scheduled(fixedRate = 3600000)
    public void generate() {

        logger.info( "Starting Cron Excecution..." );
        
        for ( String bucketName : awsS3Store.loadClientNames() ) {
            logger.info( "Generating thumbnail for " + bucketName );
            generateForBucket( bucketName );
        }

        logger.info( "Completed Cron Excecution." );
    }


    private void generateForBucket( String bucketName ) {
        Set<String> thumbnails = awsS3Store.loadThumbnails( bucketName );

        String groupName = "Videos";
        ListObjectsRequest listObjectRequest = new ListObjectsRequest().withBucketName( bucketName )
            .withPrefix( groupName );

        ObjectListing objectListing = amazonS3.listObjects( listObjectRequest );

        for ( S3ObjectSummary objectSummary : objectListing.getObjectSummaries() ) {
            logger.debug( objectSummary.getKey() );

            String revisedKey = objectSummary.getKey().replace( groupName + DELIMITER, "" );
            if ( revisedKey.isEmpty() || revisedKey.endsWith( DELIMITER ) )
                continue;

            if ( thumbnails.contains( objectSummary.getETag() ) ) {
                logger.debug( "Already generated for " + objectSummary.getETag() );
                continue;
            }

            try {
                createAndUploadThumbnail( objectSummary, groupName );
            }
            catch ( IOException | InterruptedException e ) {
                logger.error( "Thumbnail generation failed for " + objectSummary.getKey(), e );
            }
        }
    }

    private void createAndUploadThumbnail(S3ObjectSummary objectSummary, String groupName) throws IOException, InterruptedException {
        String revisedKey = objectSummary.getKey().replace( groupName + DELIMITER, "" );
        if ( revisedKey.isEmpty() || revisedKey.endsWith( DELIMITER ) )
            return;

        String fileName = objectSummary.getETag() + ".png";
        String filePath = TEMP_STORAGE_DIR + fileName;
        logger.debug( filePath );

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
            objectSummary.getBucketName(), objectSummary.getKey() );
        generatePresignedUrlRequest.setMethod( HttpMethod.GET );

        URL url = amazonS3.generatePresignedUrl( generatePresignedUrlRequest );
        String urlString = url.toString();

        String command = "ffmpeg -y -i " + urlString + " -ss 00:00:05.000 -vframes 1 -filter:v scale=640:-1 " + filePath;
        Runtime run = Runtime.getRuntime();
        Process proc = run.exec( command );
        proc.waitFor();

        logger.debug( "ffmpeg exit code: " + proc.exitValue() );

        String amazonFileUploadLocationOriginal = objectSummary.getBucketName() + DELIMITER + THUMBNAIL_GROUP;

        FileInputStream stream = new FileInputStream( filePath );
        ObjectMetadata objectMetadata = new ObjectMetadata();
        PutObjectRequest putObjectRequest = new PutObjectRequest(
            amazonFileUploadLocationOriginal, fileName, stream, objectMetadata )
            .withCannedAcl( CannedAccessControlList.PublicRead );
        PutObjectResult s3result = amazonS3.putObject( putObjectRequest );
        
        File file = new File( filePath );
        Files.deleteIfExists( file.toPath() );
    }

}
