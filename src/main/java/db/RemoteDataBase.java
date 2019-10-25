package db;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FolderMetadata;
import exception.CreateDirException;
import exception.DataBaseException;
import model.Storage;
import utilities.Utils;

public class RemoteDataBase implements Storage {



        DbxClientV2 client;
        private String src;
        private String name;

        public RemoteDataBase(String srch,String name){
            Utils utils=new Utils();
            client=utils.getClient();
            this.src=srch;
            this.name=name;


        }
    @Override
    public void iniStorage() throws DataBaseException {
        if(this.src.isEmpty()){
            throw new DataBaseException("Nije unesen path");
        }

        try {
            FolderMetadata folder = client.files().createFolder(src+name);
            System.out.println(folder.getName());
        } catch (CreateFolderErrorException err) {
            if (err.errorValue.isPath() && err.errorValue.getPathValue().isConflict()) {
                throw new DataBaseException("Neuspjesno kreiranje dira,vec posotji nesto na putanji");
            } else {
                throw new DataBaseException("Neuspjesno kreiranje dira");
            }
        } catch (Exception err) {
            throw new DataBaseException("Neuspjesno kreiranje dira");
        }



    }

    @Override
    public String getStoragePath() {
        return null;
    }

    @Override
    public String getStorageName() {
        return null;
    }
}
