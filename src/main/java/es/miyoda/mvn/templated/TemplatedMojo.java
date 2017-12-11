package es.miyoda.mvn.templated;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

@Mojo( name = "templated")
public class TemplatedMojo extends AbstractMojo {

    @Parameter
    private String[] sources;

    public void execute() throws MojoExecutionException {
        getLog().info( "Running templated" );
        for (String source : sources) {
            try {
                runTemplated(source, "./");
            } catch (IOException e) {
                getLog().error(e);
                throw new MojoExecutionException(e, "Error executing templated of "+source, e.getMessage());
            }
        }
    }

    private void runTemplated(String moveFrom, String moveTo) throws IOException {
        String[] files = new File(moveFrom).list();
        for (String file : files) {
            String fromPath = new File(moveFrom, file).getAbsolutePath();
            String toPath = new File(moveTo, file).getAbsolutePath();
            runTemplatedFile(fromPath, toPath);
        }
    }
    
    public void runTemplatedFile (String fromPath, String toPath) throws IOException {
        if (!isIgnored(fromPath)) {
            if (new File(fromPath).isFile()) {
                if (fromPath.endsWith(".part")) {
                    getLog().info(".part "+ fromPath + " " + toPath);
                    int divisor = fromPath.indexOf("|");
                    String tag = fromPath.substring(divisor + 1, fromPath.length() - ".part".length());
                    runTemplatedPart(fromPath, toPath.substring(0, toPath.lastIndexOf("|")), tag);
                } else {
                    getLog().info("link " + fromPath + " " + toPath);
                    try {
                        Files.delete(Paths.get(toPath));
                    } catch (Exception e) {
                        // unnecesary
                    }
                    Files.createLink(Paths.get(toPath), Paths.get(fromPath));
                }
            } else {
                try {
                    new File(toPath).mkdir();
                } catch (Exception e) {
                    // unnecesary
                }
                runTemplated(fromPath, toPath);
            }
        }
    }
    
    public void runTemplatedPart (String fromPath, String toPath, String tag) throws IOException {
    // const fileExt = fromPath.substring(fromPath.lastIndexOf(".")+1)
    // TODO diferent by fileExt
    replacePart(fromPath, toPath, "<!-- start|" + tag + " -->", "<!-- end|" + tag + " -->", tag);
    }
    
    public void replacePart (String fromPath, String toPath, String startTag, String endTag, String tagName) throws IOException {
    String fromData = FileUtils.fileRead(fromPath);
    String toData = FileUtils.fileRead(toPath);
    int startIndex = toData.indexOf(startTag) + startTag.length();
    int endIndex = toData.indexOf(endTag, startIndex);
    String toDataResult = null;
    if (startIndex != -1 && endIndex != -1) {
        toDataResult = toData.substring(0, startIndex) + fromData + toData.substring(endIndex);
    } else if ("start".equals(tagName)) {
        toDataResult = startTag + fromData + endTag + toData;
    } else if ("end".equals(tagName)) {
        toDataResult = toData + startTag + fromData + endTag;
    } else {
        getLog().error("tag '" + tagName + "' not found on file " + toPath);
    }
    if (toDataResult != null) {
        FileUtils.fileWrite(toPath, toDataResult);
    }
    }

    public boolean isIgnored (String filePath) {
      return filePath.endsWith(".git");
    }
}