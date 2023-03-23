/**
 * @file This File is Responsible For Creating 'FileHandler'
 *       Wrapper Over Some file to Make it Manipulable With
 *       Functions That Are Easy to Work With.
*/
package data.handlers;

/** Internal Imports */
import helpers.Wrappers;

/** External Imports */
import java.lang.Void;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * @implNote We Don't Use Try-Catch Statements For Exception Handling,
 *           Instead We Created a Wrapper Around it to Make it Cleaner
 *           And Easier to Work With And to Avoid Repetition.
 * 
 * @see helpers.Wrappers
*/
public class FileHandler {
    private String filePath;

    /**
     * @param filePath - String
    */
    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return File Content - String
     */
    public String read() {
        return Wrappers.useTryCatch(() -> {
            BufferedReader br = new BufferedReader(new FileReader(this.filePath));
            String line, content = "";

            while ((line = br.readLine()) != null) {
                content = content.concat(line + "\n");
            }

            br.close();
            return content;
        });
    }

    /**
     * @param text - String
     */
    public void write(String text) {
        Wrappers.<Void>useTryCatch(() -> {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.filePath));
            
            bw.write(text);
            bw.close();

            return null;
        });
    }

    /**
     * @param text - String
     */
    public void append(String text) {
        Wrappers.<Void>useTryCatch(() -> {
            String currentContent = this.read();
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.filePath));
            
            bw.write(currentContent.concat(text));
            bw.close();

            return null;
        });
    }

    /**
     * @return Number of Lines in The File - int
     */
    public int numOfLines() {
        return Wrappers.useTryCatch(() -> {
            BufferedReader br = new BufferedReader(new FileReader(this.filePath));
            int size = 0;

            while (br.readLine() != null) size++;

            br.close();
            return size;
        });
    }

    /**
     * @return File lines as a String Array - String[]
     */
    public String[] lines() {
        return Wrappers.useTryCatch(() -> {
            BufferedReader br = new BufferedReader(new FileReader(this.filePath));
            String[] lines = new String[this.numOfLines()];
            String line;
            int cnt = 0;

            while ((line = br.readLine()) != null) lines[cnt++] = line;

            br.close();
            return lines;
        });
    }

    /**
     * @return Copy of The Current Object - FileHandler
     */
    public FileHandler copy() {
        FileHandler clone = new FileHandler(this.filePath);
        return clone;
    }

    /**
     * @implNote Cleans The Calling Object File Content.
    */
    public void clear() {
        this.write("");
    }
}
