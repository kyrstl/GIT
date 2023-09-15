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
            String fileName = lines[i].split(" : ")[2];
            if(!fileName.equals(fileToRemove)){
                newContents.append("\n" + lines[i]);
            }
        }
        treeContents = newContents;
    }



}
