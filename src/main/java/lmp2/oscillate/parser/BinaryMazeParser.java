package lmp2.oscillate.parser;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import lmp2.oscillate.Maze_InputFormat;

public class BinaryMazeParser extends MazeParser {
    public BinaryMazeParser() {
    }

    @Override
    public void parseInto(
        Maze_InputFormat maze_InputFormat,
        String inputFilePath
    ) throws IOException, IllegalStateException {
        DataInputStream stream = new DataInputStream(
            new FileInputStream(inputFilePath)
        );
        BinaryFormat.Header header;
        try {
            header = serializeHeader(stream);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                "error while parsing binary input format maze: " + e.getMessage()
            );
        }

        try {
            maze_InputFormat.initialise(header.COLS, header.ROWS);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "error initializing input format maze: " + e.getMessage()
            );
        }

        maze_InputFormat.mapCharAt(
            Maze_InputFormat.START,
            (header.SX - 1) + header.COLS * (header.SY - 1)
        );
        maze_InputFormat.mapCharAt(
            Maze_InputFormat.END,
            (header.EX - 1) + header.COLS * (header.EY - 1)
        );
        maze_InputFormat.setSolutionOffset(header.SOFF);
        
        int charsMappedCounter = 0;
        for (int i = 0; i < header.CNTR; i++) {
            BinaryFormat.Codeword codeword = serializeCodeword(stream);
            for (int j = 0; j < (codeword.CNT & 0xff) + 1; j++) {
                maze_InputFormat.mapCharAt(
                    (char) (codeword.VAL & 0xff),
                    charsMappedCounter++
                );
            }
        }
    }

    private byte readByte(DataInputStream stream) throws IOException {
        return stream.readByte();
    }

    private short readShortLittleEndian(DataInputStream stream)
    throws IOException {
        return Short.reverseBytes(stream.readShort());
    }

    private int readIntLittleEndian(DataInputStream stream) throws IOException {
        return Integer.reverseBytes(stream.readInt());
    }

    private BinaryFormat.Header serializeHeader(DataInputStream stream)
    throws IOException, IllegalStateException {
        BinaryFormat.Header header = new BinaryFormat.Header();
        header.FID = readIntLittleEndian(stream);
        header.ESC = readByte(stream);
        if(header.ESC != 27)
            throw new IllegalStateException();
        header.COLS = readShortLittleEndian(stream);
        header.ROWS = readShortLittleEndian(stream);
        header.SX = readShortLittleEndian(stream);
        header.SY = readShortLittleEndian(stream);
        header.EX = readShortLittleEndian(stream);
        header.EY = readShortLittleEndian(stream);
        header.RESERVED_1 = readIntLittleEndian(stream);
        header.RESERVED_2 = readIntLittleEndian(stream);
        header.RESERVED_3 = readIntLittleEndian(stream);
        header.CNTR = readIntLittleEndian(stream);
        header.SOFF = readIntLittleEndian(stream);
        header.SEP = readByte(stream);
        header.WALL = readByte(stream);
        header.PATH = readByte(stream);
        return header;
    }

    private BinaryFormat.Codeword serializeCodeword(DataInputStream stream)
    throws IOException {
        BinaryFormat.Codeword codeword = new BinaryFormat.Codeword();
        codeword.SEP = readByte(stream);
        codeword.VAL = readByte(stream);
        codeword.CNT = readByte(stream);
        return codeword;
    }

    private class BinaryFormat {
        protected static class Header {
            protected int FID;
            protected byte ESC;
            protected short COLS;
            protected short ROWS;
            protected short SX;
            protected short SY;
            protected short EX;
            protected short EY;
            protected int RESERVED_1;
            protected int RESERVED_2;
            protected int RESERVED_3;
            protected int CNTR;
            protected int SOFF;
            protected byte SEP;
            protected byte WALL;
            protected byte PATH;

            @Override
            public String toString() {
                return String.format("""
                    BinaryFormat.Header {
                        FID: %x,
                        ESC: %x,
                        COLS: %d,
                        ROWS: %d,
                        SX: %d,
                        SY: %d,
                        EX: %d,
                        EY: %d,
                        RESERVED_1: %x,
                        RESERVED_2: %x,
                        RESERVED_3: %x,
                        CNTR: %d,
                        SOFF: %x,
                        SEP: %x,
                        WALL: %x,
                        PATH: %x,
                    }\
                    """,
                    this.FID,
                    this.ESC,
                    this.COLS,
                    this.ROWS,
                    this.SX,
                    this.SY,
                    this.EX,
                    this.EY,
                    this.RESERVED_1,
                    this.RESERVED_2,
                    this.RESERVED_3,
                    this.CNTR,
                    this.SOFF,
                    this.SEP,
                    this.WALL,
                    this.PATH
                );
            }
        }
        protected static class Codeword {
            protected byte SEP;
            protected byte VAL;
            protected byte CNT;

            @Override
            public String toString() {
                return String.format("""
                    BinaryFormat.Codeword {
                        SEP: %x,
                        VAL: %x,
                        CNT: %x,
                    }\
                    """,
                    this.SEP,
                    this.VAL,
                    this.CNT
                );
            }
        }
        protected static class SolutionHeader {
            protected int SID;
            protected byte STEPS;
        }
        protected static class SolutionStep {
            protected byte DIR;
            protected byte CNT;
        }
    }
}
