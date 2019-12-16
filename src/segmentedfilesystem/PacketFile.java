package segmentedfilesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.primitives.Shorts;
import com.google.common.primitives.UnsignedBytes;
import com.google.common.primitives.UnsignedInteger;

public class PacketFile {
    TreeMap<Short, byte[]> parts = new TreeMap<>();
    byte fileID;
    String fileName;

    boolean hasHeader = false;
    boolean hasEndPacket = false;

    int fileLength;

    public PacketFile(byte fileID) {
        this.fileID = fileID;
    }

    public void writeFile() {
        File file = new File(fileName);
        System.out.println("Writing " + fileName);
        OutputStream os;
        try {
            os = new FileOutputStream(file);

            for(byte[] bytes : parts.values()) {
                os.write(bytes);
            }

            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean addPacket(byte status, byte fileID, byte[] data, int length) {
        if((status & 1) == 0) { // is status even
            // header packet
            fileName = new String(data, 2, length - 2);
            hasHeader = true;
        } else {
            Short packetNumber = Shorts.fromBytes(data[2], data[3]);
            byte[] newData = Arrays.copyOfRange(data, 4, length);
            parts.put(packetNumber, newData);
            if(((status >> 1) & 1) == 1) {
                hasEndPacket = true;
                fileLength = packetNumber + 1;
            }
                

        }

        if(hasHeader && hasEndPacket && parts.size() == fileLength) { 
            writeFile();
            return true;
        } else {
            return false;
        }
    }

    
}