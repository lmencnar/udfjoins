package udfjoins;

import org.json4s.FileInput;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 *
 * Performance stats on 1.5GB file from GeoNames with locations in all countries
 *
 * readUsingBufferedRandom
 * readUsingBufferedRandom lines=11902065 timeElapsed=2.551
 * readUsingBufferedRandomInSteps
 * readUsingBufferedRandomInSteps lines=11902065 timeElapsed=2.757
 * readUsingReader
 * readUsingReader lines=11902065 timeElapsed=7.078
 * readUsingScanner
 * readUsingScanner lines=11902065 timeElapsed=55.734
 * readUsingRandom
 * readUsingRandom lines=11902065 timeElapsed=2134.341
 *
 *  @see <a href="https://bugs.openjdk.java.net/browse/JDK-6480439">Open JDK readLine performance jira</a>
 *  @see <a href="https://www.javaworld.com/article/2077523/java-tip-26--how-to-improve-java-s-i-o-performance.html">Solution faster code</a>
 *
 */
public class TextFileReadTest {


    private static final String FILE_NAME = "../data/allCountries.txt";

    public static void main(String[] args) {
        TextFileReadTest app = new TextFileReadTest();

        try {
            System.out.println("readUsingBufferedRandom");
            long startTime = System.currentTimeMillis();
            long lineCount = app.readUsingBufferedRandom(FILE_NAME);
            long endTime = System.currentTimeMillis();
            System.out.println("readUsingBufferedRandom lines=" + lineCount + " timeElapsed=" + (endTime-startTime)/1000.0);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        try {
            System.out.println("readUsingBufferedRandomInSteps");
            long startTime = System.currentTimeMillis();
            long lineCount = app.readUsingBufferedRandomInSteps(FILE_NAME, 1000);
            long endTime = System.currentTimeMillis();
            System.out.println("readUsingBufferedRandomInSteps lines=" + lineCount + " timeElapsed=" + (endTime-startTime)/1000.0);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        try {
            System.out.println("readUsingReader");
            long startTime = System.currentTimeMillis();
            long lineCount = app.readUsingReader(FILE_NAME);
            long endTime = System.currentTimeMillis();
            System.out.println("readUsingReader lines=" + lineCount + " timeElapsed=" + (endTime-startTime)/1000.0);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        try {
            System.out.println("readUsingScanner");
            long startTime = System.currentTimeMillis();
            long lineCount = app.readUsingScanner(FILE_NAME);
            long endTime = System.currentTimeMillis();
            System.out.println("readUsingScanner lines=" + lineCount + " timeElapsed=" + (endTime-startTime)/1000.0);
        } catch(Exception exc) {
            exc.printStackTrace();
        }


        try {
            System.out.println("readUsingRandom");
            long startTime = System.currentTimeMillis();
            long lineCount = app.readUsingRandom(FILE_NAME);
            long endTime = System.currentTimeMillis();
            System.out.println("readUsingRandom lines=" + lineCount + " timeElapsed=" + (endTime-startTime)/1000.0);
        } catch(Exception exc) {
            exc.printStackTrace();
        }
    }


    public long readUsingScanner(String fileName) throws IOException, FileNotFoundException{

        long lineCount = 0;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                lineCount++;
                if(lineCount % 10000 == 0) {
                    // System.out.println(lineCount);
                }
            }
        }
        return lineCount;
    }

    public long readUsingReader(String fileName) throws IOException, FileNotFoundException{


        try (FileReader file = new FileReader(fileName);
             BufferedReader br = new BufferedReader(file))
        {
            long lineCount = 0;
            String line;
            while( (line = br.readLine()) != null) {
                lineCount++;
                if(lineCount % 10000 == 0) {
                    // System.out.println(lineCount);
                }
            }
            return lineCount;
        }

    }

    public long readUsingRandom(String fileName) throws IOException, FileNotFoundException{


        long lineCount = 0;
        try (RandomAccessFile rf = new RandomAccessFile(fileName, "r")){
            String line;
            while ( (line = rf.readLine()) != null ) {
                lineCount++;
                if(lineCount % 10000 == 0) {
                    // System.out.println(lineCount);
                }
            }

            return lineCount;
        }
    }

    public long readUsingBufferedRandom(String fileName) throws IOException, FileNotFoundException{


        Charset charSet = Charset.forName("UTF-8");
        long lineCount = 0;
        try (BufferedRandomAccessFile brf = new BufferedRandomAccessFile(fileName, "r", 100 * 1024)){
            String line;
            while ( (line = brf.getNextLine(charSet)) != null ) {
                lineCount++;
                if(lineCount % 10000 == 0) {
                    // System.out.println(lineCount);
                }
            }

            return lineCount;
        }
    }

    public long readUsingBufferedRandomInSteps(String fileName, long lineCountLimit) throws IOException, FileNotFoundException{


        Charset charSet = Charset.forName("UTF-8");
        long offset = 0;
        long lineCount = 0;
        String line = null;
        do {
            long lineCountStep = 0;
            try (BufferedRandomAccessFile brf = new BufferedRandomAccessFile(fileName, "r", 100 * 1024)) {
                brf.seek(offset);
                while (lineCountStep < lineCountLimit && (line = brf.getNextLine(charSet)) != null) {
                    lineCount++;
                    lineCountStep++;
                    if (lineCount % 10000 == 0) {
                        // System.out.println(lineCount);
                    }
                }

                if(line != null) {
                    offset = brf.getFilePointer();
                }
            }
        } while(line != null);
        return lineCount;
    }

}
