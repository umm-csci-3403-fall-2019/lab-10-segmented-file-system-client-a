package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static Map<Byte, PacketFile> files;

    static int numDone = 0;
    static int numNeeded;

    public static void main(String[] args) throws IOException {
        String addr = args.length > 0 ? args[0] : "csci-4409.morris.umn.edu";
        numNeeded = args.length > 1 ? Integer.parseInt(args[1]) : 3;
        int port = 6014;
        System.out.println("Connecting to " + addr);

        DatagramSocket socket = new DatagramSocket();

        byte[] buf = new byte[1028];
        InetAddress address = InetAddress.getByName(addr);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);

        files = new HashMap<Byte, PacketFile>();

        //int i = 0;
        while(numNeeded > numDone) {
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            handlePacket(packet);
        }
    }

    public static void handlePacket(DatagramPacket packet) {
        byte[] data = packet.getData().clone();
        int length = packet.getLength();
        byte status = data[0];
        byte fileID = data[1];
        
        files.putIfAbsent(fileID, new PacketFile(fileID));
        boolean fileDone = files.get(fileID).addPacket(status, fileID, data, length);
        if(fileDone) {
            files.get(fileID).writeFile();
            numDone++;
        }
    }

}
