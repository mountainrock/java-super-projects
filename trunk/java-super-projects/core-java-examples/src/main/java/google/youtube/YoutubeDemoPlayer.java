package google.youtube;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.Person;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;

public class YoutubeDemoPlayer {
  public static void main(String[] args) {
    try {
        YouTubeService myService = new YouTubeService("mycompany-myapp-1");
        String myFeed = "http://gdata.youtube.com/feeds/videos?start-index=1&max-results=25&vq=lizards&oi=spell";
        printVideoFeed(myService, myFeed);
    } catch (IOException ex) {
        Logger.getLogger(YoutubeDemoPlayer.class.getName()).log(Level.FATAL, null, ex);
    } catch (ServiceException ex) {
        Logger.getLogger(YoutubeDemoPlayer.class.getName()).log(Level.FATAL, null, ex);
    }
}

private static void printVideoFeed(YouTubeService service, String feedUrl) throws IOException, ServiceException {
    VideoFeed videoFeed = service.getFeed(new URL(feedUrl), VideoFeed.class);
    List<VideoEntry> allVideos = videoFeed.getEntries();
    Iterator<VideoEntry> itAllVideos = allVideos.iterator();
    while (itAllVideos.hasNext()){
        VideoEntry oneVideo  = itAllVideos.next();
        TextConstruct oneVideoTitle = oneVideo.getTitle();
        String oneVideoTitleText = oneVideo.getPlainTextContent();
        //Print titles of all videos:
        System.out.print(oneVideoTitleText);
        List<Person> allAuthors = oneVideo.getAuthors();
        Iterator<Person> itAllAuthors = allAuthors.iterator();
        while (itAllAuthors.hasNext()){
            Person oneAuthor = itAllAuthors.next();
            //Print authors of current title:
            System.out.print(" (by " + oneAuthor.getName() +")\n");
        }
    }
}
}
