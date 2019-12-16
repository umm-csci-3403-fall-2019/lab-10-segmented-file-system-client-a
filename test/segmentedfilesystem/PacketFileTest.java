package segmentedfilesystem;

import static org.junit.Assert.*;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Shorts;

import org.junit.Test;

/**
 * This is just a stub test file. You should rename it to
 * something meaningful in your context and populate it with
 * useful tests.
 */
public class PacketFileTest {

    @Test
    public void testnewPacketFile() {
        byte fileID = 12;
        PacketFile testFile = new PacketFile(fileID);
        assertEquals("File id not correct", fileID, testFile.fileID);
        assertFalse("hasHeader should not be set", testFile.hasHeader);
        assertFalse("hasEndPacket should not be set", testFile.hasEndPacket);
    }

    @Test
    public void testHeaderPacket() {
        byte fileID = 12;
        PacketFile testFile = new PacketFile(fileID);
        boolean result = testFile.addPacket((byte) 0, fileID, new byte[] {0, fileID, 'T', 'e', 's', 't'}, 6);
        assertFalse("result of addPacket should be false", result);
        assertTrue("hasHeader should be set", testFile.hasHeader);
        assertEquals("filename not correct", "Test", testFile.fileName);
        assertFalse("hasEndPacket should not be set", testFile.hasEndPacket);
    }

    @Test
    public void testEndPacket() {
        byte fileID = 12;
        PacketFile testFile = new PacketFile(fileID);
        boolean result = testFile.addPacket((byte) 11, fileID, Bytes.concat(new byte[] {(byte) 11, fileID}, Shorts.toByteArray((short) 5), new byte[] {1,5,3}), 7);
        assertFalse("result of addPacket should be false", result);
        assertTrue("hasEndPacket should be set", testFile.hasEndPacket);
        assertFalse("hasHeader should not be set", testFile.hasHeader);
        assertEquals("file length should be correct", 6, testFile.fileLength);
        assertEquals("parts size should be 1", 1, testFile.parts.size());
        assertArrayEquals(new byte[] {1,5,3}, testFile.parts.get((short) 5));
    }

}
