package main;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class SharingRepository {

  //path -> hash
  private Map<String, String> pathToHash;
  
  //hash -> path
  private Map<String, String> hashToPath;
  
  //hash -> epoch creation time
  private Map<String, Long> hashToCreationTime;
  
  //TODO: move to config
  //in millis
  private final Long liveTime = 7*24*60*60*1000l; 
  
  @PostConstruct
  public void init() {
    pathToHash = new HashMap<>();
    hashToPath = new HashMap<>();
    hashToCreationTime = new HashMap<>();
  }
 
  /**
   * Creates hash for the given path or just gets it back if it already exists
   * 
   * @param path
   * @return
   */
  public synchronized String getOrCreateHash(String path) {
    String hash = pathToHash.get(path);
    if(hash == null) {
      
      //create hash, put it in the map
      MessageDigest digest = null;
      try {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
      } catch (NoSuchAlgorithmException e) {
        //ignore
      }
      byte[] hashBytes = digest.digest(path.getBytes(StandardCharsets.UTF_8));
      hash = String.format("%X", ByteBuffer.wrap(hashBytes).getLong()).toLowerCase();
      
      pathToHash.put(path, hash);
      hashToPath.put(hash, path);
      hashToCreationTime.put(hash, Instant.now().toEpochMilli());
    }
    return hash;
  }
  
  public synchronized String getHash(String path) {
    return pathToHash.get(path);
  }
  
  public synchronized String getPath(String hash) {
    return hashToPath.get(hash);
  }
  
  public synchronized void clearExpired() {
    List<String> expiredHashes = 
        hashToCreationTime.entrySet()
                          .stream()
                          .filter(en -> en.getValue() + liveTime < Instant.now().toEpochMilli())
                          .map(en -> en.getKey())
                          .collect(Collectors.toList());
    
    for(String h: expiredHashes) {
      String p = hashToPath.get(h);
      
      pathToHash.remove(p);
      hashToPath.remove(h);
      hashToCreationTime.remove(h);
    }
  }
  
}
