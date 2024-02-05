package com.TwitterClone.ProjectBackend.model.mustacheObjects;

import lombok.NoArgsConstructor;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;

@Component
@NoArgsConstructor
public class ImageManager {
    /**
     * Prepare the new image to be save it in the database
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Blob prepareImageFile(MultipartFile file) {
        try {
            return BlobProxy
                    .generateProxy(file
                            .getInputStream(), file
                            .getSize());
        } catch (IOException e) {
            return null;
        }
    }
}
