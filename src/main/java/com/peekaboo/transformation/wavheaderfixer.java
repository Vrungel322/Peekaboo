package com.peekaboo.transformation;

import java.io.*;

public class WavHeaderFixer {
    private long fileSize;

    private short mFormat;
    private short mNumChannels;
    private int mSampleRate;
    private short mBitsPerSample;
    
    private InputStream inputStream;
    private OutputStream outputStream;

    public WavHeaderFixer(File inputFile, File outputFile) throws FileNotFoundException {
        fileSize = inputFile.length();
        this.inputStream = new FileInputStream(inputFile);
        this.outputStream = new FileOutputStream(outputFile);
    }

    public WavHeaderFixer(File inputFile) throws FileNotFoundException {
        fileSize = inputFile.length();
        this.inputStream = new FileInputStream(inputFile);
    }
    
    public void setOutputFile(File outputFile) throws FileNotFoundException {
        this.outputStream = new FileOutputStream(outputFile);
    }

    public void fix() throws IOException {
        this.read();
        this.write();
    }
    
    private void read() throws IOException {
        /* RIFF header */
        readId("RIFF");
        int numBytes = readInt() - 36;
        readId("WAVE");

        /* fmt chunk */
        readId("fmt ");
        if (16 != readInt()) throw new IOException("fmt chunk length not 16");
        mFormat = readShort();
        mNumChannels = readShort();
        mSampleRate = readInt();
        int byteRate = readInt();
        short blockAlign = readShort();
        mBitsPerSample = readShort();
        if (byteRate != mNumChannels * mSampleRate * mBitsPerSample / 8) {
            throw new IOException("fmt.ByteRate field inconsistent");
        }

        if (blockAlign != mNumChannels * mBitsPerSample / 8) {
            throw new IOException("fmt.BlockAlign field inconsistent");
        }

        // Read data fix
        byte[] buff = new byte[8];
        inputStream.read(buff, 0, 8);
    }

    private void readId(String id) throws IOException {
        for (int i = 0; i < id.length(); i++) {
            if (id.charAt(i) != inputStream.read()) throw new IOException( id + " tag not present");
        }
    }

    private int readInt() throws IOException {
        return inputStream.read() | (inputStream.read() << 8) | (inputStream.read() << 16) | (inputStream.read() << 24);
    }

    private short readShort() throws IOException {
        return (short)(inputStream.read() | (inputStream.read() << 8));
    }
    

    private void write() throws IOException {
        /* RIFF header */
        writeId("RIFF");

        writeInt((int)(fileSize - 8L));

        writeId("WAVE");

        /* fmt chunk */
        writeId("fmt ");
        writeInt(16);
        writeShort(mFormat);
        writeShort(mNumChannels);
        writeInt(mSampleRate);
        writeInt(mNumChannels * mSampleRate * mBitsPerSample / 8);
        writeShort((short)(mNumChannels * mBitsPerSample / 8));
        writeShort(mBitsPerSample);

        /* data chunk */
        writeId("data");
        writeInt((int)(fileSize - 44L));

        /* write data */

        rewriteData();
    }

    private void writeId(String id) throws IOException {
        for (int i = 0; i < id.length(); i++) outputStream.write(id.charAt(i));
    }

    private void writeInt(int val) throws IOException {
        outputStream.write(val >> 0);
        outputStream.write(val >> 8);
        outputStream.write(val >> 16);
        outputStream.write(val >> 24);
    }

    private void writeShort(short val) throws IOException {
        outputStream.write(val >> 0);
        outputStream.write(val >> 8);
    }

    private void rewriteData() throws IOException {
        byte[] buffer = new byte[1024];
        int len = inputStream.read(buffer);
        while (len != -1) {
            outputStream.write(buffer, 0, len);
            len = inputStream.read(buffer);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "WaveHeader format=%d numChannels=%d sampleRate=%d bitsPerSample=%d fileSize=%d",
                mFormat, mNumChannels, mSampleRate, mBitsPerSample, fileSize);
    }
}
