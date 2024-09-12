package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {

    private int shaderProgramID;
    private String vertexSrc;
    private String fragmentSrc;
    private String filePath;

    public Shader(String filePath){
        this.filePath = filePath;
        try{

//          NOTE: Separating #type vertex and #type fragment
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");
            int index = source.indexOf("#type")+6;
            int eol = source.indexOf("\r\n", index);

//          trim after the first #type and get the keyword
            String firstPattern = source.substring(index, eol).trim();

//          Do the same for the second #type
            index = source.indexOf("#type", eol)+6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equals("vertex")){
                vertexSrc = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSrc = splitString[1];
            }else{
                throw new IOException("Unexpected token " + firstPattern);
            }

            if(secondPattern.equals("vertex")){
                vertexSrc = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSrc = splitString[2];
            }else{
                throw new IOException("Unexpected token " + secondPattern);
            }

            System.out.println(vertexSrc);
            System.out.println(fragmentSrc);
        }catch(IOException e){
            e.printStackTrace();
            assert false : "Error: Couldnt open shader files" + filePath + "!";
        }
    }

    public void compile(){

    }

    public void use(){

    }

    public void detach(){

    }
}
