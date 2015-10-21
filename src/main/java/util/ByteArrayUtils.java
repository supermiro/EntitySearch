package util;

import java.util.ArrayList;
import java.util.List;

public class ByteArrayUtils {
	
	public static final byte[] intToByteArray(int i) {
		return new byte[] { (byte) (i >> 24), (byte) (i >> 16),
				(byte) (i >> 8), (byte) i };
	}
	
	public static byte[] encodeIntList(List<Integer> targets) {
		byte[] b = new byte[targets.size() * 4];
		for (int i = 0; i < targets.size(); i++) {
			byte[] byteVal;
			try {
				byteVal = intToByteArray(targets.get(i).intValue());
			} catch (Exception e) {
				byteVal = intToByteArray(-1);
			}
			System.arraycopy(byteVal, 0, b, i * 4, 4);
		}
		return b;
	}
	
	public static List<Integer> decodeIntArry(byte[] b) {
		int length = b.length / 4;
		List<Integer> links = new ArrayList<Integer>();
		byte[] b1 = new byte[4];
		for (int i = 0; i < length; i++) {
			System.arraycopy(b, i * 4, b1, 0, 4);
			links.add(byteArrayToInt(b1));
		}
		return links;
	}
	
	public static final int byteArrayToInt(byte[] b) {
		return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
				| (b[3] & 0xff);
	}

}
