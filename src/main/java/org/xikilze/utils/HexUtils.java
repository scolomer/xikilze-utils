package org.xikilze.utils;
import java.util.BitSet;

public class HexUtils {
	public static String toHexString(byte[] tab) {
		return toHexString(tab, 0, tab.length);
	}
	
	public static String toHexString(byte[] tab, int off, int len) {
		if (tab == null) return "null";
		
		StringBuffer buf = new StringBuffer();
		
		for (int i=off; i<off+len; i++) {
			int value = tab[i] >= 0 ? tab[i] : 256+tab[i];
			if (i != 0) buf.append(' ');
			if (value < 16) buf.append('0');
			buf.append(Integer.toHexString(value));
		}
		
		return buf.toString();
	}

	public static String toHexString2(byte[] tab) {
		if (tab == null) return "null";
		
		StringBuffer buf = new StringBuffer();
		
		for (int i=0; i<tab.length; i++) {
			int value = tab[i] >= 0 ? tab[i] : 256+tab[i];
			if (value < 16) buf.append('0');
			buf.append(Integer.toHexString(value));
		}
		
		return buf.toString();
	}
	
	public static String toHexString(byte x) {
		StringBuilder buf = new StringBuilder();
		int value = x >= 0 ? x : 256+x;
		if (value < 16) buf.append('0');
		buf.append(Integer.toHexString(value));
		
		return buf.toString();
	}
	
	public static String toBinaryString(byte[] tab) {
		return toBinaryString(tab, 0, tab.length);
	}
		
	public static String toBinaryString(byte[] tab, int off, int len) {
		if (tab == null) return "null";
		
		StringBuffer buf = new StringBuffer();

		for (int i=off; i<off+len; i++) {
			StringBuffer buf2 = new StringBuffer();
			for (int j=7; j>=0; j--) {
				buf2.append((1<<j & tab[i]) != 0 ? "1" : "0");
			}
			if (i != 0) buf.append(' ');
			buf.append(buf2);
		}
		
		return buf.toString();
	}
	
	public static String toASCIIPrintableString(byte[] tab) {
		if (tab == null) return "null";
		
		StringBuffer buf = new StringBuffer();
		
		for (int i=0; i<tab.length; i++) {
			if (tab[i] >= 0 && tab[i]<32) {
				buf.append(".");
			} else {
				buf.append((char)tab[i]);
			}
		}
		
		return buf.toString();
	}
	
	public static String decodeShiftedDCB(byte[] tab) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<tab.length-1; i++) {
			int n1, n2;
			n1 = (tab[i] & 0x3C) >> 2;
			n2 = ((tab[i] & 3) << 2) | ((tab[i+1] >> 6) & 3);
			if (n1 >= 10 || n2 >= 10) throw new RuntimeException("Erreur DCB");
			buf.append(n1).append(n2);
		}
		int n = (tab[tab.length-1] & 0x3C) >> 2; 
		buf.append(n);
				
		return buf.toString();
	}
	
	public static String decodeDCB(byte[] tab) {
		StringBuffer buf = new StringBuffer();
		
		for (int i=0; i<tab.length; i++) {
			int n1 = (tab[i] >> 4) & 0xF;
			int n2 = (tab[i] & 0xF);
			if (n1 >= 10 || n2 >= 10) throw new RuntimeException("Erreur DCB");
			buf.append(n1).append(n2);
		}
		
		return buf.toString();
	}
	
	public static String decodeComp3AsString(byte[] tab) {
		StringBuilder bld = new StringBuilder();
		
		for (int i=0; i<tab.length; i++) {
			int n1 = (tab[i] >> 4) & 0xF;
			int n2 = (tab[i] & 0xF);
			if (i < tab.length - 1) {
				if (n1 >= 10 || n2 >= 10) throw new NumberFormatException("Computational-3 error");
				if (bld.length() == 0) {
					if (n1 != 0) bld.append(n1);
					bld.append(n2);
				} else {
					bld.append(n1).append(n2);
				}
			} else {
				if (n1 >= 10) throw new NumberFormatException("Computational-3 error");
				bld.append(n1);
				if (n2 < 10) bld.append(n2);
				else if (n2 == 0xC) bld.insert(0, '+');
				else if (n2 == 0xD) bld.insert(0, '-');
				else if (n2 == 0xF);
				else throw new NumberFormatException("Computational-3 error");
			}
		}
		
		return bld.toString();
		
	}
	
	public static byte[] subArray(byte[] tab, int offset, int len) {
		byte[] subtab = new byte[len];
		for (int i=0; i<len; i++) {
			subtab[i] = tab[offset+i];
		}
		
		return subtab;
	}

	public static byte[] subArray(byte[] tab, int offset) {
		return subArray(tab, offset, tab.length-offset);
	}

	
	public static String decode5bitsString(byte[] tab, int start) {
		StringBuffer buf = new StringBuffer();
		
		int i = 0;
		int off = start;
		for (;;) {
			int c = decodeChar(subArray(tab, i, 2), off); 
			buf.append(Character.valueOf((char)(c-1+'A')));
			
			off += 5;
			if (off >= 8) {
				off %= 8;
				i++;
				if (i == tab.length-1) break;
			}
		}
		
		return buf.toString();
	}
	
	public static int decodeChar(byte[] tab, int offset) {
		if (offset >= 8) throw new RuntimeException("Offset " + offset + " invalide");
		if (offset < 3) {
			return (tab[0] >> 3-offset) & 0x1F;
		} else {
			int p1 = (tab[0] << offset-3) & 0x1F;
			int p2 = (tab[1] >> (8 - offset + 3)) & ((1<<(offset-3))-1);
			return p1 | p2;  
		}
	}	
	
	public static int countSetBits(byte b) {
		int count = 0;
		for (int i=0;i<8;i++) {
			if ((b & (1 << i)) != 0) {
				count++;
			}
		}
		return count;
	}

	public static int countSetBits(byte[] bs) {
		int count = 0;
		for (byte b: bs) {
			count += countSetBits(b);
		}
		
		return count;
	}
	
	public static long getLong(byte[] data) {
		long val = 0;
		
		for (byte b: data) {
			val <<= 8;
			val += b >= 0 ? b : (256+b);
		}
		
		return val;
	}
	
	public static BitSet toBitSet(byte[] bytes) {
		BitSet bits = new BitSet(); 
		for (int i=0; i<bytes.length*8; i++) { 
			if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) { 
				bits.set(i); 
			} 
		} 
		return bits; 
	}

	public static byte[] toByteArray(BitSet bits) { 
		byte[] bytes;
		if (bits.length() %8 == 0) {
			bytes = new byte[bits.length()/8]; 
		} else {
			bytes = new byte[bits.length()/8+1]; 
		}
		for (int i=0; i<bits.length(); i++) { 
			if (bits.get(i)) { 
				bytes[bytes.length-i/8-1] |= 1<<(i%8); 
			} 
		} 
		return bytes; 
	}
	
	public static byte hexValue(char c) {
		byte v;
		if (c >= '0' && c <= '9') {
			v = (byte) (c - '0');
		} else {
			v = (byte) (c - 'A' + 10);
		}
		
		return v;
	}

	public static byte[] parseHexString(String str) {
		str = str.toUpperCase();
		byte[] data = new byte[str.length()/2];
		for (int i=0; i<str.length()/2; i++) {

			char c;
			
			c = str.charAt(i*2);
			
			data[i] = (byte) (HexUtils.hexValue(c) << 4);

			c = str.charAt(i*2+1);
			data[i] |= HexUtils.hexValue(c);
		}
		
		return data;
	}

	public static int toUnsigned(short s) {
		if (s >= 0) {
			return s;
		} else {
			return 0x10000 + s;
		}
	}
}
