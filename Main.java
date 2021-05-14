import java.util.*;

/**
 =================TODO=====================

    - make a hard medium and easy mode

 =================TODO=====================
 */
class Main {
    public static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        final Random RAND = new Random();
        final String[] HANG_IMAGES = HangmanTextImages.getImages();
        final String[] EASY_WORDS = {
            "Baloon",
            "Shoe",
            "Lemon",
            "Airplane",
            "Bone",
            "Zigzag",
            "Wheel",
            "Ice Cream Cone"
        };
        final String[] HARD_WORDS = {
            "Twelfth",
            "Rickshaw",
            "Jazz",
            "Squush",
        };
        final Terminal TERMINAL = new Terminal();
        TERMINAL.clear();
        adjustScreen(TERMINAL);
        String word;
        int imageIndex;
        int indexOf;
        int usedCharRow;
        int usedCharColumn;
        int placeHolderIndex;
        char[] answer;
        char[] placeHolders;
        char guess;
        boolean usedChar;
        boolean foundChar;
        boolean canExit;
        HashSet<Character> usedChars = new HashSet<Character>();
        do {
            usedChars.clear();
            imageIndex = 0;
            placeHolderIndex = 0;

            TERMINAL.clear();
            intro(HANG_IMAGES, TERMINAL);
            word = EASY_WORDS[RAND.nextInt(EASY_WORDS.length)].toUpperCase();
            answer = word.toCharArray();
            placeHolders = new char[answer.length];
            Arrays.fill(placeHolders, '_');
            for (int i = 0; i < answer.length; i++) {
                indexOf = word.indexOf(' ', i);
                if (indexOf > 0)
                    placeHolders[indexOf] = ' ';
                TERMINAL.putString(7, i + 35 + placeHolderIndex, placeHolders[i] + " ");
                placeHolderIndex++;
            }
            do {
                canExit = true;
                foundChar = false;
                usedChar = false;
                usedCharRow = 6;
                usedCharColumn = 45;
                TERMINAL.putStringWithBreaks(1, 2, HANG_IMAGES[imageIndex]);
                TERMINAL.setCursorPosition(usedCharRow, usedCharColumn);
                int column = usedCharColumn;
                for (Character ch : usedChars) {
                    TERMINAL.append(ch + " ");
                    if (column > 6 + usedCharColumn) {
                        usedCharRow++;
                        TERMINAL.setCursorPosition(usedCharRow, usedCharColumn);
                    }
                    column++;
                }
                TERMINAL.showCursor();
                TERMINAL.setCursorPosition(5, 35);
                TERMINAL.append("\033[0K");
                TERMINAL.slowPrintString(5, 35, 15, "Please enter your guess --> ");
                guess = sc.next().toUpperCase().charAt(0);
                sc.nextLine();
                for (char ch : usedChars) {
                    if (ch == guess)
                        usedChar = true;
                }
                placeHolderIndex = 0;
                for (int i = 0; i < answer.length; i++) {
                    if (answer[i] == guess) {
                        placeHolders[i] = guess;
                        foundChar = true;
                    }
                    TERMINAL.putString(7, i + 35 + placeHolderIndex, placeHolders[i] + " ");
                    placeHolderIndex++;
                }
                TERMINAL.flush();
                usedChars.add(guess);
                if (!usedChar && !foundChar)
                    imageIndex++;
                for (int i = 0; i < answer.length; i++) {
                    if (answer[i] != placeHolders[i])
                        canExit = false;
                }
            } while (imageIndex < HANG_IMAGES.length - 1 && !canExit);
            if (canExit) {
                // If won 27
                TERMINAL.clear();
                TERMINAL.putStringWithBreaks(1, 1, HangmanTextImages.WIN_IMAGE);
                TERMINAL.hideCursor();
                Runnable r1 = () -> {
                    for (int i = 0; i <= 10; i++) {
                        TERMINAL.putStringWithBreaks(i, 31, HangmanTextImages.WIN_TITLE_FIRST_HALF);
                        TERMINAL.flush();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                };
                Runnable r2 = () -> {
                    for (int i = 20; i > 10; i--) {
                        TERMINAL.putStringWithBreaks(i, 60, HangmanTextImages.WIN_TITLE_SECOND_HALF);
                        TERMINAL.flush();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                };
                Runnable r3 = () -> {
                    try {
                        TERMINAL.slowPrintStringT(20, 38, 15, "Please press enter to continue...");
                    } catch(InterruptedException e) {
                    return;
                    }
                };
                Thread t1 = new Thread(r1);
                Thread t2 = new Thread(r2);
                Thread t3 = new Thread(r3);
                t1.start();
                t2.start();
                t3.start();
                sc.nextLine();
                t1.interrupt();
                t2.interrupt();
                t3.interrupt();
            } else {
                // If lost 
                TERMINAL.clear();
                TERMINAL.putStringWithBreaks(1, 2, HANG_IMAGES[HANG_IMAGES.length - 1]);
                TERMINAL.flush();
                TERMINAL.fadeIn(1, 26, 13, HangmanTextImages.LOSE_TITLE);
                TERMINAL.slowPrintString(10, 35, 15, "Please press enter to continue...");
                sc.nextLine();
            }
        } while (true);
    }

    private static void adjustScreen(Terminal terminal) {
        String arrow = String.format("%106s➔", " ").replaceAll(" ", "━");
        Runnable r1 = () -> {
            terminal.showCursor();
            try {
                terminal.putString(2, 2, "\033[2mPress enter to continue\033[0m");
                terminal.putString(4, 0, arrow);
                terminal.slowPrintStringT(1, 1, 15, "Please make the arrow completely straight by adjusting your screen.");
                Thread.sleep(5);
            } catch (InterruptedException e) { 
                return;
            }
        };
        Thread t1 = new Thread(r1);
        t1.start();
        sc.nextLine();
        t1.interrupt();
        terminal.hideCursor();
        terminal.clear();
    }

    private static void intro(String[] hangImages, Terminal terminal) {
        terminal.clear();
        String startMessage = "Would you like to start? (Yes/No)";
        terminal.putStringWithBreaks(1, 2, hangImages[hangImages.length - 1]);
        terminal.putStringWithBreaks(2, 30, "\033[1m" + HangmanTextImages.getHangManTitle() + "\033[0m");
        terminal.flush();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        terminal.append("\033[1m");
        terminal.showCursor();
        terminal.slowPrintString(16, 30, 15, startMessage);
        terminal.append("\033[0m");
        terminal.putCharacter(17, 30, '>');
        terminal.setCursorPosition(17, 32);

        boolean canExit = false;
        do {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("no")) {
                terminal.hideCursor();
                terminal.swipeUp(23, 10);
                terminal.slowPrintString(1, 1, 100, "Okay... :((");
                terminal.close();
                System.exit(0);
            } else if (input.equalsIgnoreCase("yes")) {
                terminal.hideCursor();
                terminal.clear();
                canExit = true;
            }
            else {
                terminal.setCursorPosition(17, 32);
                terminal.clearFront();
                terminal.setCursorPosition(16, 32 + startMessage.length());
                terminal.clearFront();
                terminal.setForegroundRGB(157, 2, 8);
                terminal.append("\033[1m");
                terminal.slowPrintString(16, 32 + startMessage.length(), 15, "ERROR: Invalid input, Try again.");
                terminal.append("\033[0m");
                terminal.setCursorPosition(17, 32);
            }
        } while (!canExit);
    }
}