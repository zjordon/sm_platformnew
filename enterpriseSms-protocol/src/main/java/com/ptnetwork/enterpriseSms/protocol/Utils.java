/**
 * Project Name:enterpriseSms-protocol
 * File Name:Utils.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-23����11:07:12
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.protocol;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * ������ ClassName:Utils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2013-1-23 ����11:07:12 <br/>
 * 
 * @author JasonZhang
 * @version
 * @since JDK 1.6
 * @see
 */
public final class Utils {

	public final static void int2bytes(int integer, byte[] dest, int destStart) {
//		dest[destStart] = (byte) (0xff & integer);
//		dest[destStart + 1] = (byte) ((0xff00 & integer) >> 8);
//		dest[destStart + 2] = (byte) ((0xff0000 & integer) >> 16);
//		dest[destStart + 3] = (byte) ((0xff000000 & integer) >> 24);
		//cmpp协议用的是相反的大端协议？
		dest[destStart] = (byte) ((0xff000000 & integer) >> 24);
		dest[destStart + 1] = (byte) ((0xff0000 & integer) >> 16);
		dest[destStart + 2] = (byte) ((0xff00 & integer) >> 8);
		dest[destStart + 3] = (byte) (0xff & integer);
	}
	
	public final static void longtobytes(long longer , byte[] dest, int destStart) {
		dest[destStart] = (byte) ((0xff000000 & longer) >> 56);
		dest[destStart + 1] = (byte) ((0xff000000 & longer) >> 48);
		dest[destStart + 2] = (byte) ((0xff000000 & longer) >> 40);
		dest[destStart + 3] = (byte) ((0xff000000 & longer) >> 32);
		dest[destStart + 4] = (byte) ((0xff000000 & longer) >> 24);
		dest[destStart + 5] = (byte) ((0xff0000 & longer) >> 16);
		dest[destStart + 6] = (byte) ((0xff00 & longer) >> 8);
		dest[destStart + 7] = (byte) (0xff & longer);
	}
	
	public final static int bytes2int(InputStream in) throws IOException {
//		int integer = 0;
//		byte b = (byte) in.read();
//		integer = (b & 0xff) << 8;
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 8);
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 8);
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 8);
		
//		byte b = (byte) in.read();
//		integer = (b & 0xff);
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 8);
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 16);
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 24);
		//cmpp协议用的是相反的大端协议？
//		byte b = (byte) in.read();
//		integer = ((b & 0xff) << 24);
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 16);
//		b = (byte) in.read();
//		integer = integer | ((b & 0xff) << 8);
//		b = (byte) in.read();
//		integer = integer | (b & 0xff);
//		return integer;
		int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
	public final static int bytes2int(byte[] data, int start) throws IOException {
		//int integer = 0;
		//cmpp协议用的是相反的大端协议？
//		byte b = data[0];
//		integer = ((b & 0xff) << 24);
//		b = data[1];
//		integer = integer | ((b & 0xff) << 16);
//		b =data[2];
//		integer = integer | ((b & 0xff) << 8);
//		b = data[3];
//		integer = integer | (b & 0xff);
//		
//		return integer;
		return ((data[start] & 0xff) << 24) | ((data[start+1] & 0xff) << 16) | ((data[start + 2] & 0xff) << 8) | (data[start + 3] & 0xff);
	}
	
	public final static int bytes2int(InputStream in, byte firstByte) throws IOException {
		int integer = 0;
		byte b = firstByte;
		integer = ((b & 0xff) << 24);
		b = (byte) in.read();
		integer = integer | ((b & 0xff) << 16);
		b = (byte) in.read();
		integer = integer | ((b & 0xff) << 8);
		b = (byte) in.read();
		integer = integer | (b & 0xff);
		return integer;
	}
	
	public final static long bytes2long(InputStream in) throws IOException {
		byte[] readBuffer = new byte[8];
		in.read(readBuffer);
		return (((long)readBuffer[0] << 56) +
                ((long)(readBuffer[1] & 255) << 48) +
                ((long)(readBuffer[2] & 255) << 40) +
                ((long)(readBuffer[3] & 255) << 32) +
                ((long)(readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) <<  8) +
                ((readBuffer[7] & 255) <<  0));
	}
	
	public final static long bytes2long(byte[] data, int start) throws IOException {
		//byte[] readBuffer = new byte[8];
		//in.read(readBuffer);
		return (((long)data[start] << 56) +
                ((long)(data[start + 1] & 255) << 48) +
                ((long)(data[start + 2] & 255) << 40) +
                ((long)(data[start + 3] & 255) << 32) +
                ((long)(data[start + 4] & 255) << 24) +
                ((data[start + 5] & 255) << 16) +
                ((data[start + 6] & 255) <<  8) +
                ((data[start + 7] & 255) <<  0));
	}
	
	public final static void string2bytes(String str, byte[] dest, int destStart, int len, byte defaultFill) {
		if (str != null) {
			byte[] bs = str.getBytes();
			int actualLen = bs.length;
			if (actualLen > len) {
				actualLen = len;
			}
			int start = destStart;
			
			for (int i=0; i<actualLen; i++) {
				dest[start++] = bs[i];
			}
			
			if (actualLen < len) {
				int diff = len - actualLen;
				for (int i=0; i<diff; i++) {
					dest[start++] = defaultFill;
				}
			}
		} else {
			for (int i=destStart; i<destStart + len; i++) {
				dest[i] = defaultFill;
			}
		}
		
	}
	
	public final static void string2bytes(String str, byte[] dest, int destStart, int len) {
		string2bytes(str, dest, destStart, len, (byte)0x00);
	}
	
	public final static String bytes2string(InputStream in, int len) throws IOException {
		byte[]  bs= new byte[len];
		in.read(bs);
		return new String(bs);
	}
	
	public final static String bytes2string(byte[] data, int start, int len) throws IOException {
		byte[] bs= new byte[len];
		for (int i=0; i<len; i++) {
			bs[i] = data[start++];
		}
		return new String(bs);
	}
	
	public final static String bytes2string(InputStream in, int len, String charset) throws IOException {
		byte[]  bs= new byte[len];
		in.read(bs);
		return new String(bs, charset);
	}
	
	public final static String bytes2string(byte[] data, int start, int len, String charset) throws IOException {
		byte[]  bs= new byte[len];
		for (int i=0; i<len; i++) {
			bs[i] = data[start++];
		}
		return new String(bs, charset);
	}
	
	public final static int copyBytes(byte[] src, byte[] dest, int destStart) {
		int destEnd = destStart;
		for (int i=0; i<src.length; i++) {
			dest[destEnd++] = src[i];
		}
		return destEnd;
	}
	
	public final static int getTimestamp() {
		Calendar cal = Calendar.getInstance();
		int timestamp = 0;
		timestamp = cal.get(Calendar.MONTH + 1) * 100000000 + cal.get(Calendar.DAY_OF_MONTH) * 1000000
				+ cal.get(Calendar.HOUR_OF_DAY) * 10000 + cal.get(Calendar.MINUTE) * 100 + cal.get(Calendar.SECOND);
		return timestamp;
	}
	
	public static byte[] longToByte(long number) { 
        long temp = number; 
        byte[] b = new byte[8]; 
        for (int i = b.length - 1; i >= 0; i--) { 
            b[i] = new Long(temp & 0xff).byteValue();
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    }
	
	public static byte[] anotherLongToByte(long longer) {
//		byte[] dest = new byte[8];
//		dest[0] = (byte) ((0xff000000 & longer) >> 56);
//		dest[1] = (byte) ((0xff000000 & longer) >> 48);
//		dest[2] = (byte) ((0xff000000 & longer) >> 40);
//		dest[3] = (byte) ((0xff000000 & longer) >> 32);
//		dest[4] = (byte) ((0xff000000 & longer) >> 24);
//		dest[5] = (byte) ((0xff0000 & longer) >> 16);
//		dest[6] = (byte) ((0xff00 & longer) >> 8);
//		dest[7] = (byte) (0xff & longer);
//		return dest;
		byte[] writeBuffer = new byte[8];
		 writeBuffer[0] = (byte)(longer >>> 56);
	        writeBuffer[1] = (byte)(longer >>> 48);
	        writeBuffer[2] = (byte)(longer >>> 40);
	        writeBuffer[3] = (byte)(longer >>> 32);
	        writeBuffer[4] = (byte)(longer >>> 24);
	        writeBuffer[5] = (byte)(longer >>> 16);
	        writeBuffer[6] = (byte)(longer >>>  8);
	        writeBuffer[7] = (byte)(longer >>>  0);
	    return writeBuffer;
		
	}
	
	public static byte[] intToByte(int number) { 
        int temp = number; 
        byte[] b = new byte[4]; 
        for (int i = b.length - 1; i >= 0; i--) { 
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位 
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    }
	
	public static byte[] anotherIntToByte(int integer) { 
		byte[] dest = new byte[4];
		dest[0] = (byte) ((0xff000000 & integer) >> 24);
		dest[1] = (byte) ((0xff0000 & integer) >> 16);
		dest[2] = (byte) ((0xff00 & integer) >> 8);
		dest[3] = (byte) (0xff & integer);
		return dest;
    }
	
	public final static void main(String[] args) {
		byte[] bs = longToByte(6246121876746731521L);
		for (byte b : bs) {
			System.out.println(b);
		}
		bs = anotherLongToByte(6246121876746731521L);
		System.out.println();
		for (byte b : bs) {
			System.out.println(b);
		}
		System.out.println();
		bs = intToByte(3456);
		for (byte b : bs) {
			System.out.println(b);
		}
		bs = anotherIntToByte(3456);
		System.out.println();
		for (byte b : bs) {
			System.out.println(b);
		}
	}
	
}
