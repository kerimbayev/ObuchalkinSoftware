package models.thread;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CopyFile implements Runnable{
    private String file1;
    private String file2;

    public CopyFile(String file1, String file2) {
        this.file1 = file1;
        this.file2 = file2;
    }

    @Override
    public void run() {
        try {
            FileInputStream fs = new FileInputStream(file1);
            int b;
            FileOutputStream os = new FileOutputStream(file2);
            while ((b = fs.read()) != -1) {
                os.write(b);
            }
            os.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
