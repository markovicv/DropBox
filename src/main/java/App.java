import exception.*;
import model.DropboxDirectory;
import model.DropboxFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {



    public static void main(String[] args) throws UploadFileException {
        DropboxFile drop=new DropboxFile();
        DropboxDirectory dropdir=new DropboxDirectory();
      //  drop.upload("/Users/nesko/Desktop/DropboxStorage/voda.txt","/vukasinFolder");

        try {
            //drop.create("/vukasinFolder","bogdan.txt");
            //dropdir.create("aa","/");
            //String lista[]={"txt"};

           // dropdir.getByExtension("/vukasinFolder",lista,true);
           // dropdir.upload("/Users/nesko/Desktop/DropboxStorage/novitet","/vukasinFolder");
          /*  File fajl1=new File("/Users/nesko/Desktop/DropboxStorage/jedan");
            File fajl2=new File("/Users/nesko/Desktop/DropboxStorage/dva");
            List<File> lista=new ArrayList<>();
            lista.add(fajl1);
            lista.add(fajl2);
            dropdir.uploadDirs(lista,"/vukasinFolder");*/
         // dropdir.download();
            //dropdir.getFileNamesInDir("/vukasinFolder",true);
            //dropdir.getAllDirs("",true);
           // dropdir.upload("/Users/nesko/Desktop/DropboxStorage/proba","");
           File fajl1=new File("/Users/nesko/Desktop/DropboxStorage/proba.zip");
            File fajl2=new File("/Users/nesko/Desktop/DropboxStorage/test3.zip");


            List<File>list=new ArrayList<>();
            list.add(fajl1);
            list.add(fajl2);
           // drop.uploadZip(("/Users/nesko/Desktop/DropboxStorage/test3.zip"),"aa");
         //   dropdir.uploadZipDirs(list,"","aa");
            dropdir.download("/probaFolder","aa");

          //  drop.uploadListToZip(list,"aa","aa");
           // drop.upload("/Users/nesko/Desktop/DropboxStorage/proba.txt","/vukasinFolder");
           // drop.create("prolom.txt","/vukasinFolder");
            //drop.uploadZip("/Users/nesko/Desktop/DropboxStorage/test3.zip","a","s2");
           // dropdir.download("/neskovFolder","aa");
           // drop.create("/Users/nesko/Desktop/DropboxStorage/proba.txt","euro.txt");

          // /drop.delete("/test2.txt");
           //  drop.copy("/test3.zip","/nesko.zip");
            //drop.rename("/test3.zip","/marko.zip");
            //dropdir.rename("/markovFolder","/");
            //drop.download("/nesko.docx", "123");
            //drop.move("/markovFolder/nesko.docx","/neskovFolder");
           // drop.rename("/marko.zip","glupan.zip");
            //dropdir.rename("/neskovFolder","vukasinFolder");
            //drop.copy("/nesko.zip","/neskovFolder");
            //dropdir.move("/glupanFolder","/neskovFolder");
            //dropdir.download("/marko","sq");
           // dropdir.download("/vukasinFolder/glupanFolder/Document.docx","aa");


        } /*catch (UploadMultipleFileException e) {
            e.printStackTrace();
        }*/ /*catch (DirecrotyListException e) {
            e.printStackTrace();
        }*/ catch (Exception e) {
            e.printStackTrace();
        }
        /*catch (CopyFileException e) {
            e.printStackTrace();
        } *//*catch (RenameException e) {
            e.printStackTrace();
        }*/ /*catch (MoveFileException e) {
            e.printStackTrace();
        }*/ /*catch (RenameException e) {
            e.printStackTrace();
        }*//* catch (CopyFileException e) {
            e.printStackTrace();
        }*//* catch (MoveDirException e) {
            e.printStackTrace();
        } *//*catch (RenameException e) {
            e.printStackTrace();
        } *//*catch (DownloadDirException e) {
            e.printStackTrace();
        }*/ /*catch (CreateFileException e) {
            e.printStackTrace();
        }*/ /*catch (CreateFileException e) {
            e.printStackTrace();
        }*/
        /*catch (DownloadFileException e) {
            e.printStackTrace();
        }*//* catch (DeleteDirException e) {
            e.printStackTrace();
        }*/ /*catch (CreateDirException e) {
            e.printStackTrace();
        } *//*catch (DownloadDirException e) {
            e.printStackTrace();
        }*//* catch (MoveFileException e) {
            e.printStackTrace();
        }*/ /*catch (CopyFileException e) {
            e.printStackTrace();
        }*/ /*catch (DeleteFileException e) {
            e.printStackTrace();
        }*/ /*catch (CopyFileException e) {
            e.printStackTrace();
        }*/

    }
}
