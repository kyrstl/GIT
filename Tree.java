import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

public class Tree {
    private StringBuilder treeContents;
    public Tree(){
        treeContents = new StringBuilder();
    }

    public void add(String entry) throws FileNotFoundException {
        //only add if no duplicates
        //String convertedStr = treeContents.toString();
        //if(convertedStr.contains(entry)) {

        //}
        //else {
            String[] entryComponents = entry.split(" : ");
            String hash = entryComponents[1];
            String fileName = "";
            if(entryComponents.length > 2){
                fileName = entryComponents[2];
            }
            String[] lines = treeContents.toString().split("\n");
            Boolean duplicate = false;
            for(String line: lines){
                String[] components = line.split(" : ");
                if(components.length > 2 && components[2].equals(fileName)){
                    duplicate = true;
                }
                else if(components.length == 2 && components[1].equals(hash)){
                    duplicate = true;
                }
            }
            if(!duplicate){
                treeContents.append(entry+"\n"); //ADDED NEW LINE
            }
        //}
        writeToFile();//???????????
        
    }

    public void removeBlob(String fileToRemove) throws FileNotFoundException{
        StringBuilder newContents = new StringBuilder();
        String[] lines = treeContents.toString().split("\\n");
        for(int i = 0; i < lines.length; i++){
            String[] components = lines[i].split(" : ");
            if(components.length < 3 || !components[2].equals(fileToRemove)){
                newContents.append("\n" + lines[i]);
            }
        }
        treeContents = newContents;
        writeToFile();
    }

    public void removeTree(String hashToRemove) throws FileNotFoundException{
        StringBuilder newContents = new StringBuilder();
        String[] lines = treeContents.toString().split("\\n");
        for(int i = 0; i < lines.length; i++){
            String[] components = lines[i].split(" : ");
            if(components.length > 2 || !components[1].equals(hashToRemove)){
                newContents.append("\n" + lines[i]);
            }
        }
        treeContents = newContents;
        writeToFile();
    }

    public void writeToFile() throws FileNotFoundException{
        String convertedStr = treeContents.toString();
        String addedContents = convertedStr.substring(0,convertedStr.length()-1);
        String sha = Blob.encryptPassword(addedContents);
        File tree = new File("./objects/" + sha);
        PrintWriter pw = new PrintWriter(tree);
        pw.print(treeContents.toString());
        pw.close();
    }

    public String getSha() {
        String convertedStr = treeContents.toString();
        //String addedContents = convertedStr.substring(0,convertedStr.length()-1);
        String addedContents = convertedStr.trim();
        return Blob.encryptPassword(addedContents);
    }

    public String getTreeContents(){
        String convertedStr = treeContents.toString();
        String addedContents = convertedStr.substring(0,convertedStr.length()-1);
        return addedContents;
    }

    public String addDirectory(String directoryPath) throws NoSuchAlgorithmException, IOException {
        //ADD directoryPath??
        File dir = new File(directoryPath);
        //showFiles(dir.listFiles());

        //SHOULD add inout directoryPath?
        //File[] files = dir.listFiles();
        String[] files = dir.list();
        //addDirectory(files);
        for (String fileName : files) {
            File file = new File(directoryPath,fileName);
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getAbsolutePath());
                String treePath = file.getAbsolutePath();
                String treeName = file.getName();
                //String treeName = file.getName();
                //add(treeName);//is this right????
                Tree childTree = new Tree();
                String sha = childTree.addDirectory(treePath);//????????

                if(!treeName.equals("")) { //how do i know theres a tree name??
                    String entry = "tree : " + sha + " : " + treeName;
                    add(entry);
                    //treeContents.append(entry+"\n");
                }
                else {
                    String entry = "tree : " + sha;
                    add(entry);
                    //treeContents.append(entry+"\n");
                }
                
                addDirectory(treePath); // Calls same method again.
            } else {
                String blobName = file.getName();
                String input = directoryPath + blobName;
                Blob blob = new Blob(input);//FILE NOT FOUND
                String entry = "blob : " + blob.getSha1() + " : " + blobName;
                add(entry);
                //treeContents.append(entry+"\n");
                //System.out.println("blob : " + blob.getSha1() + " : " + blobName);
            }
        }

        //String convertedStr = treeContents.toString();
        //String addedContents = convertedStr.substring(0,convertedStr.length()-1);
        //add(addedContents);

        String sha1 = getSha();
        String hi = "hi!";

        return sha1;
    }

    /*public void main(String... args) {
        File dir = new File("/path/to/dir");
        //showFiles(dir.listFiles());

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //print folder sha
                addDirectory(files);
            } else {
                String blobName = file.getName();
                add(blobName);
            }
        }

    }*/

    /*public void addDirectory(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getAbsolutePath());
                String treeName = file.getName();
                //add(treeName);//is this right????
                Tree childTree = new Tree();
                String sha = childTree.printToFile(treeName);
                add(sha);
                addDirectory(file.listFiles()); // Calls same method again.
            } else {
                //System.out.println("File: " + file.getAbsolutePath());
                String blobName = file.getName();
                add(blobName);
            }
        }
    }*/
}
