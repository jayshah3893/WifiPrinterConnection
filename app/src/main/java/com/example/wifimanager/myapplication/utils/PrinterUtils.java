package com.example.wifimanager.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;



public class PrinterUtils {

    private Context mContext;

    public static final byte[] RESET = {0x1b, 0x40};

    public static final byte[] CHECK_PAPER = new byte[]{0x10, 0x04, 0x04};

    public static final byte[] CUT = new byte[]{0x1b, 0x69};

    public static final byte[] zouzhi = new byte[]{0x1d, 0x56, 0x42, 0x00};

    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x00};

    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01};

    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x02};

    public static final byte[] BOLD = {0x1b, 0x45, 0x01};

    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};

    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};

    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};

    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};

    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};
    public static final byte[] MIDDLE = {0x1d, 0x21, 0x02};

    public static final byte[] LARGE = {0x1d, 0x21, 0x11};

    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32};

    private static final int LINE_BYTE_SIZE = 32;


    private static final int LINE_BYTE_SIZE_80 = 48;


    private static final int LEFT_LENGTH = 18;

    private static final int RIGHT_LENGTH = 14;

    private static final int LEFT_LENGTH_80 = 28;

    private static final int RIGHT_LENGTH_80 = 20;

    private static final int LEFT_TEXT_MAX_LENGTH = 7;

    private static final byte ESC = 27;

    private static int printerSize = 0;

    public void setContext(Context context) {
        mContext = context;
    }

    private OutputStream outputStream;

    public boolean isconnected() {
        return isconnected;
    }

    public void setIsconnected(boolean isconnected) {
        this.isconnected = isconnected;
    }

    private boolean isconnected;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    public static void feedAndCut(OutputStream outputStream) {
        try {
            outputStream.write(0x1D);
            outputStream.write(86);
            outputStream.write(65);
            outputStream.write(85);
            outputStream.flush();
            byte[] bytes = {29, 86, 0};
            outputStream.write(bytes);
        } catch (Exception e) {
            Log.e("", "feedAndCut" + e);
        }
    }

    public static byte[] getCutPaperByte() {
        byte[] buffer = new byte[5];
        buffer[0] = '\n';
        buffer[1] = 29;
        buffer[2] = 86;
        buffer[3] = 66;
        buffer[4] = 1;
        return buffer;
    }

    public int doCheckPaperState(Socket socket) {
        int flag = 0;
        try {
            InputStream bis = socket.getInputStream();
//            outputStream.write(CHECK_PAPER);
//            outputStream.flush();
            int tmp = bis.read();
            if (tmp == 18) {
                flag = 1;
            } else {
                flag = 0;
            }
        } catch (Exception e) {
            flag = -1;
            e.printStackTrace();
        }
        return flag;
    }


    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }


    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }


    public static byte[] set_HT_position(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return result;
    }


    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);
        int marginBetweenMiddleAndRight;
        if (printerSize == 0) {
            marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;
        } else {
            marginBetweenMiddleAndRight = LINE_BYTE_SIZE_80 - leftTextLength - rightTextLength;
        }
        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        int marginBetweenLeftAndMiddle;
        if (printerSize == 0) {
            marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;
        } else {
            marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;
        }
        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);
        Log.e("", "printThreeData sb left " + sb.toString());
        int marginBetweenMiddleAndRight;
        if (printerSize == 0) {
            marginBetweenMiddleAndRight = (RIGHT_LENGTH - middleTextLength / 2 - rightTextLength) + 1;
        } else {
            marginBetweenMiddleAndRight = RIGHT_LENGTH_80 - middleTextLength / 2 - rightTextLength;
        }
        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        Log.e("", "printThreeData sb right " + sb.toString());
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        Log.e("", "printThreeData sb right =" + sb.toString());
        return sb.toString();
    }



    public static void selectCommand(OutputStream outputStream, byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("", "selectCommand " + e);
        }
    }

    public void selectCommand(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {

        }
    }


    public String getLine58() {
        return "--------------------------------";
    }

    public String getLine80() {
        return "------------------------------------------------";
    }


    public void printText(String text) {
        try {
            if (outputStream == null) {
                Toast.makeText(mContext, "请先连接上打印机", Toast.LENGTH_SHORT).show();
                return;
            }
            selectCommand(outputStream, PrinterUtils.RESET);
            selectCommand(outputStream, PrinterUtils.LINE_SPACING_DEFAULT);
            selectCommand(outputStream, PrinterUtils.ALIGN_LEFT);
            //默认使用小号
            selectCommand(outputStream, PrinterUtils.NORMAL);
            byte[] data = text.getBytes("GB2312");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("", "printText " + e);
        }
    }


    public void printText(OutputStream outputStream, String text) {
        try {
            selectCommand(outputStream, PrinterUtils.RESET);
            selectCommand(outputStream, PrinterUtils.LINE_SPACING_DEFAULT);
            selectCommand(outputStream, PrinterUtils.ALIGN_LEFT);
            selectCommand(outputStream, PrinterUtils.NORMAL);
            byte[] data = text.getBytes("GB2312");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("", "printText " + e);
        }
    }


    public int getPrinterSize() {
        return printerSize;
    }

    public void setPrinterSize(int Size) {
        printerSize = Size;
    }
}
