import java.io.*;

public class Terminal {
    private final PrintWriter writer;
    
    public Terminal() {
        writer = new PrintWriter(System.out);
    }

    public void setCursorPosition(int row, int column) {
        writer.append("\033[" + row + ";" + column + "f");
        writer.flush();
    }

    public void hideCursor() {
        writer.append("\033[?25l");
        writer.flush();
    }

    public void showCursor() {
        writer.append("\033[?25h");
        writer.flush();
    }

    public void clear() {
        writer.append("\033[2J\033[0;0f").flush();
    }

    public void clearFront() {
        writer.append("\033[0K");
        writer.flush();
    }

    public void clearBehind() {
        writer.append("\033[1K");
        writer.flush();
    }

    public void setForegroundRGB(int n1, int n2, int n3) {
        writer.print("\033[38;2;" + n1 + ";" + n2 + ";" + n3 + "m");
    }

    public void setBackgroundRGB(int n1, int n2, int n3) {
        writer.print("\033[48;2;" + n1 + ";" + n2 + ";" + n3 + "m");
    }

    public void putString(int row, int column, String s) {
        writer.format("\033[" + row + ";" + column + "f%s", s);
    }

    public void putCharacter(int row, int column, char ch) {
        writer.format("\033[" + row + ";" + column + "f%c", ch);
    }

    public void putStringWithBreaks(int startRow, int startColumn, String s) {
        int row = startRow;
        String[] lines = s.split("\n");
        for (; row < lines.length + startRow; row++) {
            putString(row, startColumn, lines[row - startRow]);
        }
    }

    public void fadeIn(int row, int column, int delay, String s) {
        for (int i = 0; i <= 255; i++) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {}
            setForegroundRGB(i, i, i);
            putStringWithBreaks(row, column, s);
            flush();
        }
    }

    public void slowPrintString(int row, int column, int milBetweenChars, String s) {
        for (int i = 0; i < s.length(); i++) {
            putCharacter(row, column, s.charAt(i));
            writer.flush();
            try {
                Thread.sleep(milBetweenChars);
            } catch (InterruptedException ignored) {}
            column++;
        }
    }

    public void slowPrintStringT(int row, int column, int milBetweenChars, String s) throws InterruptedException {
        for (int i = 0; i < s.length(); i++) {
            putCharacter(row, column, s.charAt(i));
            writer.flush();
            Thread.sleep(milBetweenChars);
            column++;
        }
    }


    public void slowPrintStringWithBreaks(int row, int startColumn, int milBetweenChars, String s) {
        int column;
        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; i++) {
            row++;
            column = startColumn;
            for (int j = 0; j < lines[i].length(); j++) {
                putCharacter(row, column, lines[i].charAt(j));
                writer.flush();
                try {
                    Thread.sleep(milBetweenChars);
                } catch (InterruptedException ignored) {}
                column++;
            }
        }
        
    }

    public void swipeUp(int startRow, int sleepTime) {
        for (int i = startRow; i >= 0; i--) {
            setCursorPosition(i, 0);
            clearLine();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {}
        }
    }

    public PrintWriter append(String s) {
        return writer.append(s);
    }

    public void clearLine() {
        writer.append("\033[2K");
        writer.flush();
    }

    public void saveScreen() {
        writer.append("\033[?47h");
    }

    public void restoreScreen() {
        writer.append("\033[?47l");
    }

    public void flush() {
        writer.flush();
    }

    public void close() {
        writer.close();
    }
}
