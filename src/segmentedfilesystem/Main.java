package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        String addr = args.length > 0 ? args[0] : "csci-4409.morris.umn.edu";
        int port = 6014;
        System.out.println("Connecting to " + addr);

        DatagramSocket socket = new DatagramSocket();

        byte[] buf = new byte[1028];
        InetAddress address = InetAddress.getByName(addr);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);

        int i = 0;
        while(i < 3) {
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            //String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Packet " + i);
            System.out.println("length " + packet.getLength());
            for(byte b : packet.getData()) {
                System.out.print(Byte.toString(b) + " ");
            }
            System.out.println();
            //System.out.println(received);
            i++;
        }
    }

}
