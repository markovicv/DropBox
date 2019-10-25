package model;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CopyBuilder;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.MoveBuilder;
import exception.*;
import utilities.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class DropboxFile implements FileOrdinary {
    DbxClientV2 client;
    public DropboxFile(){
        Utils utils=new Utils();
        client=utils.getClient();

    }

    @Override
    public void create(String path, String name) throws CreateFileException {
            String putanja=System.getProperty("user.dir");
            Path p=Paths.get(putanja+File.separator+name);
        try {
            Files.createFile(p);

            upload(p.toString(),path);
            Files.delete(p);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreateFileException("Nije uspjelo kreiranje");
        }


    }

    @Override
    public void delete(String path) throws DeleteFileException {

        if(path.equals("")){
            throw new DeleteFileException("Fajl ne postoji");
        }
        try
        {
            Metadata metadata = client.files().delete(path);
        }
        catch (Exception e)
        {   throw new DeleteFileException("Neuspjesno brisanje fajla");

        }
    }

    @Override
    public void move(String fromPath, String toPath) throws MoveFileException {
        File file=new File(fromPath);
        if(fromPath.equals("")){
            throw new MoveFileException("Fajl ne postoji");
        }
        try {
            Metadata metadata = client.files().move(fromPath,toPath+"/"+file.getName());

        }catch (Exception e){
            throw new MoveFileException("Neuspjesno pomjeranje fajla");
        }
    }

    @Override
    public void rename(String fromPath, String newName) throws RenameException {
        if(fromPath==null){
            throw new RenameException("Fajl ne postoji");
        }

        try {
            File file=new File(fromPath);
            if(!file.exists()){
                throw new RenameException("File does not exist");
            }
            String path2=fromPath.substring(0,fromPath.length()-file.getName().length());
            path2+=newName;
            System.out.println(path2);
            Metadata metadata =client.files().copy(fromPath,path2);
            delete(fromPath);


        }catch (Exception e){
            throw new RenameException("Neuspjesno pomjeranje fajla");
        }


    }

    @Override
    public void download(String path, String s1) throws DownloadFileException {

        try {
            DbxDownloader<FileMetadata> downloader=client.files().download(path);
            FileOutputStream file=new FileOutputStream(client.files().getMetadata(path).getName());
            downloader.download(file);
            file.close();

        }catch (Exception e){
            e.printStackTrace();


        }
    }

    @Override
    public void upload(String path, String toPath) throws UploadFileException {
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

    @Override
    public void copy(String fromPath, String toPath) throws CopyFileException {
            File file=new File(fromPath);
        try {
            Metadata metadata =client.files().copy(fromPath,toPath+"/"+file.getName());


        }catch (Exception e){
            throw new CopyFileException("Neuspjesno pomjeranje fajla");
        }

    }

    @Override
    public void uploadZip(File filee, String s1) throws UploadFileException {

        String path = filee.getPath();
        File file=new File(path);
        if(path.equals(""))
            throw new UploadFileException("fajl ne postoji");
        try  {
            InputStream in = new FileInputStream(path);

            FileMetadata metadata = client.files().uploadBuilder("/"+file.getName())
                    .uploadAndFinish(in);
        }
        catch(Exception e){
            IOException exception = (IOException) e;
            throw new UploadFileException("Upload nije uspjesan");

        }

    }

    @Override
    public void uploadMultipleFile(List<File> list, String desPath) throws UploadMultipleFileException {

        if(list.size()==0)
            throw new UploadMultipleFileException("Lista je prazna");
        try  {
            for(int i=0;i<=list.size();i++){
                InputStream in = new FileInputStream(list.get(i));

                FileMetadata metadata = client.files().uploadBuilder(desPath+"/"+list.get(i).getName())
                        .uploadAndFinish(in);
            }

        }
        catch(Exception e){
            IOException exception = (IOException) e;
            throw new UploadMultipleFileException("Upload nije uspjesan");

        }


    }

    @Override
    public void uploadMultipleFilesToZipFiles(List<File> direkotirjum, String desPath) throws UploadMultipleFileException {
            Zipper zip=new Zipper();
        try {
            File file=new File(System.getProperty("user.dir")+java.io.File.separator+"proba");
            file.mkdir();

            String path =System.getProperty("user.dir")+java.io.File.separator+"proba"+java.io.File.separator ;
            for(File fajl : direkotirjum) {
                Files.copy(fajl.toPath(),
                        (new File(path + fajl.getName())).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            zip.zipDir(file,System.getProperty("user.dir")+java.io.File.separator,file.getName());
            System.out.println(System.getProperty("user.dir")+java.io.File.separator+file.getName());
            File f = new File(System.getProperty("user.dir")+java.io.File.separator+file.getName()+".zip");
            uploadZip(f,"aa");




        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
