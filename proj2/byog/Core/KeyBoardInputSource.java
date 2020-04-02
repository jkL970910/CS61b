package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

public class KeyBoardInputSource implements InputSource {

    private static final boolean PRINT_TYPED_KEYS = false;

    public KeyBoardInputSource() {
    }

    @Override
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.println(c);
                }
                return c;
            }
        }
    }

    @Override
    public boolean possibleNextInput() {
        return true;
    }
}
