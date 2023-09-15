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

    public void remove(String entry){
        StringBuilder newContents = new StringBuilder();
        String[] lines = treeContents.toString().split("\\n");
        for(int i = 0; i < lines.length; i++){
            if(!lines[i].equals(entry)){
                newContents.append("\n" + lines[i]);
            }
        }
        treeContents = newContents;
    }



}
