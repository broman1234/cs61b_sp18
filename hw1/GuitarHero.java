import java.util.ArrayList;

public class GuitarHero {
    private static boolean hasKey(String keyboard, char key) {
        return keyboard.indexOf(key) >= 0;
    }

    private static ArrayList<synthesizer.GuitarString> createKeyboard(String keyboard) {
        ArrayList<synthesizer.GuitarString> strings = new ArrayList<>();
        for (int i = 0; i < keyboard.length(); i += 1) {
            double freq = 440 * Math.pow(2, (i - 24) / (double) 12);
            strings.add(new synthesizer.GuitarString(freq));
        }
        return strings;
    }

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        ArrayList<synthesizer.GuitarString> strings = GuitarHero.createKeyboard(keyboard);

        ArrayList<Character> inputs = new ArrayList<>();
        inputs.add('v');
        inputs.add('b');
        inputs.add('k');

        for (char input : inputs) {
            if (!GuitarHero.hasKey(keyboard, input)) {
                throw new IndexOutOfBoundsException("there is no such key in the keyboard");
            }
            int index = keyboard.indexOf(input);
            synthesizer.GuitarString stringIndex = strings.get(index);
            stringIndex.pluck();

            for (int i = 0; i < 50000; i += 1) {
                double sample = 0;
                for (synthesizer.GuitarString string : strings) {
                    sample += string.sample();
                }
                StdAudio.play(sample);

                for (synthesizer.GuitarString string : strings) {
                    string.tic();
                }
            }
        }
    }
}
