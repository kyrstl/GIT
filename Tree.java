import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Tree {
    private StringBuilder treeContents;
    public Tree(){
        treeContents = new StringBuilder();
    }

    public void add(String entry){
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
            treeContents.append(entry);
        }
    }

    public void removeBlob(String fileToRemove){
        StringBuilder newContents = new StringBuilder();
        String[] lines = treeContents.toString().split("\\n");
        for(int i = 0; i < lines.length; i++){
            String[] components = lines[i].split(" : ");
            if(components.length < 3 || !components[2].equals(fileToRemove)){
                newContents.append("\n" + lines[i]);
            }
        }
        treeContents = newContents;
    }

    public void removeTree(String hashToRemove){
        StringBuilder newContents = new StringBuilder();
        String[] lines = treeContents.toString().split("\\n");
        for(int i = 0; i < lines.length; i++){
            String[] components = lines[i].split(" : ");
            if(components.length > 2 || !components[1].equals(hashToRemove)){
                newContents.append("\n" + lines[i]);
            }
        }
        treeContents = newContents;
    }

    public void writeToFile() throws FileNotFoundException{
        String sha = Blob.encryptPassword(treeContents.toString());
        File tree = new File("./objects/" + sha);
        PrintWriter pw = new PrintWriter(tree);
        pw.print(treeContents.toString());
        pw.close();
    }

    public String getTreeContents(){
        return treeContents.toString();
    }
}
