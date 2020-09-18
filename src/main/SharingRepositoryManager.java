package main;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SharingRepositoryManager {

  @Autowired
  private SharingRepository sharingRepository;
  
  @PostConstruct
  public void init() {
    new Thread(() -> {
      for(;;) {
        try {
          sharingRepository.clearExpired();
          //check once in a minute
          Thread.sleep(60*1000l);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }).start();
  }
  
}
