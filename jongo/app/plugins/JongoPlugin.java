package plugins;

/**
 * api metrics
 *
 * @author <a href="mailto:yongxiaozhao@gmail.com">zhaoxiaoyong</a>
 * @version Revision: 1.0
 * date 2016/4/18 9:44
 */


import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jongo.Jongo;
import play.Logger;
import play.Play;
import play.PlayPlugin;

public class JongoPlugin extends PlayPlugin {
    //mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
    public static String mongoUri = Play.configuration.getProperty("jongo.uri",
            "mongodb://127.0.0.1:27017/local");
    static DB db;
    static volatile Jongo jongo;
     @Override
     public void afterApplicationStart() {
         Logger.info("Starting JongoPlugin...");
         Logger.info("Connecting MongoDB %s",mongoUri);
        MongoClientURI mongoClientURI = new MongoClientURI(JongoPlugin.mongoUri);
        db = new MongoClient(mongoClientURI).getDB(mongoClientURI.getDatabase());
        jongo = new Jongo(db);
         Logger.info("JongoPlugin started.");
    }


    @Override
    public void onApplicationStop() {
        db.getMongo().close();
        Logger.info("JongoPlugin stopped.");
    }

    public static Jongo getJongo() {
        if (jongo != null)
        return jongo;
        else {
            throw new RuntimeException("JongoPlugin not ready");
        }
    }
}

