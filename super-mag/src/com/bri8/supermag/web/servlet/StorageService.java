package com.bri8.supermag.web.servlet;
import java.util.logging.Logger;

import com.bri8.supermag.util.WebConstants;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("deprecation")

public class StorageService {

private static final Logger log = Logger.getLogger(StorageService.class.getName());
private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();  


public BlobKey getBlobkey(String filename) {
    BlobKey bk = blobstoreService.createGsBlobKey(String.format("/gs/%s/%s",WebConstants.BUCKETNAME, filename));
    return bk;
}

}