package ru.ind.tgs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static ru.ind.tgs.TelnetStream.Commands.*;

/**
 * Author:      nik <br>
 * Date:        12.04.12, 14:41 <br>
 * Description: <br>
 */
public class TelnetStream {
    private TelnetInputStream inputStream;
    private TelnetOutputStream outputStream;

    public TelnetStream(final InputStream is, final OutputStream os) {
        this.inputStream = new TelnetInputStream(is);
        this.outputStream = new TelnetOutputStream(os);
    }

    public TelnetInputStream getInputStream() {
        return inputStream;
    }

    public TelnetOutputStream getOutputStream() {
        return outputStream;
    }

    public static class TelnetInputStream extends InputStream {
        private final InputStream is;

        public TelnetInputStream(final InputStream is) {
            this.is = is;
        }

        private void readTillIACSE() throws IOException {
            boolean gotIAC = false;

            while (true) {
                int x = is.read();

                switch (x) {
                    case IAC:
                        gotIAC = true;
                        break;
                    case SE:
                        if (gotIAC) {
                            return;
                        }
                    default:
                        gotIAC = false;
                }
            }
        }

        private void handleWILL() throws IOException {
            int opt = is.read();
        }

        private void handleWONT() throws IOException {
            int opt = is.read();
        }

        private void handleDO() throws IOException {
            int opt = is.read();
        }

        private void handleDONT() throws IOException {
            int opt = is.read();
        }

        private void handleSB() throws IOException {
            readTillIACSE();
        }

        @Override
        public int available() throws IOException {
            return is.available();
        }

        @Override
        public int read() throws IOException {
            while (true) {
                int x = is.read();


                if (x == IAC) {
                    int cmd = is.read();

                    switch (cmd) {
                        case IAC:
                            return IAC;
                        case WILL:
                            handleWILL();
                            break;
                        case WONT:
                            handleWONT();
                            break;
                        case DO:
                            handleDO();
                            break;
                        case DONT:
                            handleDONT();
                            break;
                        case SB:
                            handleSB();
                            break;
                        default:
                            // skip
                    }
                } else {
                    return x;
                }
            }
        }
    }

    public static class TelnetOutputStream extends OutputStream {
        private final OutputStream os;
        private int prev = -1;

        TelnetOutputStream(final OutputStream os) {
            this.os = os;
        }

        public void writeDO(final int opt) throws IOException {
            os.write(new byte[]{(byte) IAC, (byte) DO, (byte) opt});
        }

        public void writeDONT(final int opt) throws IOException {
            os.write(new byte[]{(byte) IAC, (byte) DONT, (byte) opt});
        }

        public void writeWILL(final int opt) throws IOException {
            os.write(new byte[]{(byte) IAC, (byte) WILL, (byte) opt});
        }

        public void writeWONT(final int opt) throws IOException {
            os.write(new byte[]{(byte) IAC, (byte) WONT, (byte) opt});
        }

        @Override
        public void write(final int b) throws IOException {
            switch (b) {
                case 0x0a:
                    if (prev != 0x0d) {
                        os.write(0x0d);
                    }
                default:
                    prev = b;
                    os.write(b);
            }
        }
    }

    /**
     * Apache-commons class
     */
    public static final class Commands {
        /**
         * Interpret As Command
         */
        public static final int IAC = 0xff;

        /**
         * Go Ahead <BR> Newer Telnets do not make use of this option
         * that allows a specific communication mode.
         */
        public static final int GA = 249;

        /**
         * Negotiation: Will do option
         */
        public static final int WILL = 251;

        /**
         * Negotiation: Wont do option
         */
        public static final int WONT = 252;

        /**
         * Negotiation: Do option
         */
        public static final int DO = 253;

        /**
         * Negotiation:  Dont do option
         */
        public static final int DONT = 254;

        /**
         * Marks start of a subnegotiation.
         */
        public static final int SB = 250;

        /**
         * Marks end of subnegotiation.
         */
        public static final int SE = 240;

        /**
         * No operation
         */
        public static final int NOP = 241;

        /**
         * Data mark its the data part of a SYNCH which helps to clean up the buffers between
         * Telnet Server &lt;-&gt; Telnet Client. <BR>
         * It should work like this we send a TCP urgent package and &lt;IAC&gt; &lt;DM&gt; the receiver
         * should get the urgent package (SYNCH) and just discard everything until he receives
         * our &lt;IAC&gt; &lt;DM&gt;.<BR>
         * <EM>Remark</EM>:
         * <OL>
         * <LI>can we send a TCP urgent package?
         * <LI>can we make use of the thing at all?
         * </OL>
         */
        public static final int DM = 242;

        /**
         * Break
         */
        public static final int BRK = 243;

        /**
         * The following implement the NVT (network virtual terminal) which offers the concept
         * of a simple "printer". They are the basical meanings of control possibilities
         * on a standard telnet implementation.
         */

        /**
         * Interrupt Process
         */
        public static final int IP = 244;

        /**
         * Abort Output
         */
        public static final int AO = 245;

        /**
         * Are You There
         */
        public static final int AYT = 246;

        /**
         * Erase Char
         */
        public static final int EC = 247;

        /**
         * Erase Line
         */
        public static final int EL = 248;

        private Commands() {
        }
    }
}