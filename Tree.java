import java.io.File;
import java.util.ArrayList;

public class Tree {
    private StringBuilder treeContents;
    public Tree(){
        treeContents = new StringBuilder();
    }

    public void add(String entry){
        treeContents.append("\n" + entry);
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
}
