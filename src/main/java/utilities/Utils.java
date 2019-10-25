package utilities;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class Utils {

    private static final String ACCESS_TOKEN = "uRgYy-rSRRAAAAAAAAAAD7r_rFpeMdDiPd0xg5-KRhFnJxdBHhqnQxWkmhdKE8Sb";
    private DbxClientV2 client;

    public Utils(){
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
         client = new DbxClientV2(config, ACCESS_TOKEN);

    }

    public DbxClientV2 getClient() {
        return client;
    }
}
