package model;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import exception.*;
import utilities.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipFile;


public class DropboxDirectory implements Directory {

    DbxClientV2 client;

    public DropboxDirectory() {
        Utils utils = new Utils();
        client = utils.getClient();
    }


    @Override
    public void create(String path, String name) throws CreateDirException {
        if(path.isEmpty()){
            throw new CreateDirException("Nije unesen path");
        }

        try {
            FolderMetadata folder = client.files().createFolder(path+name);
            System.out.println(folder.getName());
        } catch (CreateFolderErrorException err) {
            if (err.errorValue.isPath() && err.errorValue.getPathValue().isConflict()) {
                throw new CreateDirException("Neuspjesno kreiranje dira,vec posotji nesto na putanji");
            } else {
                throw new CreateDirException("Neuspjesno kreiranje dira");
            }
        } catch (Exception err) {
            throw new CreateDirException("Neuspjesno kreiranje dira");
        }

    }

    @Override
    public void delete(String path) throws DeleteDirException {
        if (path.equals("")) {
            throw new DeleteDirException("Dir ne postoji");
        }
        try {
            Metadata metadata = client.files().delete(path);
        } catch (Exception e) {
            throw new DeleteDirException("Neuspjesno brisanje direktorijuma");

        }
    }

    @Override
    public void move(String fromPath, String toPath) throws MoveDirException {
        File file = new File(fromPath);
        if (fromPath.equals("")) {
            throw new MoveDirException("Direkotrijum ne postoji");
        }
        try {
            Metadata metadata = client.files().move(fromPath, toPath + "/" + file.getName());

        } catch (Exception e) {
            throw new MoveDirException("Neuspjesno pomjeranje direktorijuma");
        }


    }

    @Override
    public void rename(String fromPath, String newName) throws RenameException {
        if (fromPath == null) {
            throw new RenameException("Direktorijum ne postoji");
        }

        try {
            File file = new File(fromPath);
            String path2 = fromPath.substring(0, fromPath.length() - file.getName().length());
            path2 += newName;

            Metadata metadata = client.files().copy(fromPath, path2);
            delete(fromPath);


        } catch (Exception e) {
            throw new RenameException("Neuspjesno pomjeranje fajla");
        }

    }

    @Override
    public void download(String path, String s1) throws DownloadDirException {
        if (path == null) {
            throw new DownloadDirException("Direktorijuim ne posotji");
        }


        try {
            DbxDownloader<DownloadZipResult> downloader = client.files().downloadZip(path);
            FileOutputStream file = new FileOutputStream(client.files().getMetadata(path).getName()+".zip");
            downloader.download(file);
            file.close();

        } catch (Exception e) {
            throw new DownloadDirException("Neuspjesno skidanje direktorijuma");


        }

    }

    @Override
    public void upload(String path, String toPath) throws UploadDirException {

            File file=new File(path);

            if(file.isDirectory()){

                try{
                    if(toPath.isEmpty()){
                        create(file.getName(),"/");
                    }else{

                        create(file.getName(),toPath+"/");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                String files[]=file.list();
                for(String name:files){
                    File aFile=new File(file,name);
                    if(aFile.isDirectory()){
                        System.out.println(file.getPath()+File.separator+name);
                        System.out.println("/"+file.getName());

                        upload(file.getPath()+File.separator+name,toPath+"/"+file.getName());
                    }else{
                        if(toPath.isEmpty()){
                            try {
                                uploadSingle(file.getPath()+File.separator+name,"/"+file.getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                uploadSingle(file.getPath()+File.separator+name,toPath+"/"+file.getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }
            }else{
                try {
                    uploadSingle(path,toPath);
                } catch (UploadFileException e) {
                    e.printStackTrace();
                }
            }


    }

    @Override
    public void uploadDirs(List<File> list, String desPath) throws UploadMultipleFileException {
        if(list.size()==0)
            throw new UploadMultipleFileException("Lista je prazna");

        try  {
            for(int i=0;i<list.size();i++){
                upload(list.get(i).getPath(),desPath);
            }

        }
        catch(Exception e){
            IOException exception = (IOException) e;
            throw new UploadMultipleFileException("Upload nije uspjesan");

        }




    }

    @Override
    public void uploadZipDirs(List<File> list, String dest, String zipName) throws ZipException {
        //za root u dropboxu path mora biti prazan bez kose crte
        if(list.size()==0){
            throw new ZipException("Lista je prazna");

        }
        for(File file:list){
            try {
                uploadZip(file.getPath(),"aa");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void zipDir(File dir, String path, String zipName) throws ZipException {

    }

    @Override
    public List<File> getByExtension(String path, String[] strings, boolean toSort) throws FileListException {

        if(strings.length==0){
            throw new FileListException("Lista ekstenzija je prazna");
        }
        if(path.isEmpty()){
            throw new FileListException("Putanja je prazna");
        }
        List<File> toReturn=new ArrayList<>();
        try {
            List<String> files = new ArrayList<String>();

            ListFolderResult folderi = client.files().listFolder(path);
            for (Metadata metadata : folderi.getEntries()) {
                String name = metadata.getName();
                String spliter[] = name.split("\\.");
                boolean ima = false;
                if (spliter.length ==2) {
                    for (String sadrzaj : strings) {

                        if (sadrzaj.equals(spliter[1])) {

                            ima = true;
                        }
                    }
                    if (ima) {
                        File file = new File(name);
                        toReturn.add(file);
                    }


                    files.add(name);
                }
            }

            if(toSort)
                Collections.sort(toReturn);
            if(toReturn.size()==0){
                System.out.println("Ne postoji ni jedan fajl sa zadatim ekstenzijama");
            }

            System.out.println(toReturn);


        } catch (ListFolderErrorException e) {
            e.printStackTrace();
            throw new FileListException("Neuspjesno izlistavanje");
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public List<File> getFileNamesInDir(String path, boolean toSort) throws FileListException {
        List<File> toReturn=new ArrayList<>();
        try {
            List<String> files = new ArrayList<String>();

            ListFolderResult folderi = client.files().listFolder(path);
            for (Metadata metadata : folderi.getEntries()) {
                String name = metadata.getName();

                File file=new File(name);
                    toReturn.add(file);
                    files.add(name);
            }

            System.out.println(files);

            if(toSort)
                Collections.sort(toReturn);

            System.out.println(toReturn);


        } catch (ListFolderErrorException e) {
            e.printStackTrace();
            throw new FileListException("Neuspjesno izlistavanje");
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return toReturn;
    }


    @Override
    public List<File> getAllDirs(String path, boolean sortTrue) throws DirecrotyListException {
        //ako hoces root stavis za path prazan string
        List<File> toReturn=new ArrayList<>();
        try {
            List<String> files = new ArrayList<String>();

            ListFolderResult folderi = client.files().listFolder(path);
            for (Metadata metadata : folderi.getEntries()) {
                String name = metadata.getName();

                File file=new File(name);

                int a=name.indexOf('.');
                if(a<0) {
                    file.mkdir();
                    toReturn.add(file);
                    files.add(name);
                }
                }

            System.out.println(files);

            if(sortTrue)
                Collections.sort(toReturn);

            System.out.println(toReturn);


        } catch (ListFolderErrorException e) {
            e.printStackTrace();
            throw new DirecrotyListException("Neuspjesno izlistavanje");
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private void uploadSingle(String path, String toPath) throws UploadFileException {
        File file=new File(path);
        if(path.equals(""))
            throw new UploadFileException("fajl ne postoji");
        try  {
            InputStream in = new FileInputStream(file);

            FileMetadata metadata = client.files().uploadBuilder(toPath+"/"+file.getName())
                    .uploadAndFinish(in);
        }
        catch(Exception e){
            IOException exception = (IOException) e;
            throw new UploadFileException("Upload nije uspjesan");

        }

    }
    public void uploadZip(String path, String s1) throws CopyFileException {


        File file=new File(path);
        if(path.equals(""))
            throw new CopyFileException("fajl ne postoji");
        try  {
            InputStream in = new FileInputStream(path);

            FileMetadata metadata = client.files().uploadBuilder("/"+file.getName())
                    .uploadAndFinish(in);
        }
        catch(Exception e){
            IOException exception = (IOException) e;
            throw new CopyFileException("Upload nije uspjesan");

        }

    }

    @Override
    public void setPath(String s) {

    }

    @Override
    public String getPath() {
        return null;
    }
}
