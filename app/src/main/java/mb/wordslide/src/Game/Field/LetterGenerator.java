package mb.wordslide.src.Game.Field;

import java.util.Random;

/**
 * Created by mbolg on 21.09.2017.
 */

public class LetterGenerator {
    private static Random random = new Random();

    public static char getRandomLetter() {
        float point = 100 * random.nextFloat() + 0.01f;

        if (point <= 10.97) return 'О';
        if (point <= 19.45) return 'Е';
        if (point <= 27.43) return 'А';
        if (point <= 34.78) return 'И';
        if (point <= 41.48) return 'Н';
        if (point <= 47.74) return 'Т';
        if (point <= 53.21) return 'С';
        if (point <= 57.94) return 'Р';
        if (point <= 62.48) return 'В';
        if (point <= 66.88) return 'Л';
        if (point <= 70.37) return 'К';
        if (point <= 73.58) return 'М';
        if (point <= 76.56) return 'Д';
        if (point <= 79.37) return 'П';
        if (point <= 81.99) return 'У';
        if (point <= 84.00) return 'Я';
        if (point <= 85.9) return 'Ы';
        if (point <= 87.64) return 'Ь';
        if (point <= 89.34) return 'Г';
        if (point <= 90.99) return 'З';
        if (point <= 92.58) return 'Б';
        if (point <= 94.02) return 'Ч';
        if (point <= 95.23) return 'Й';
        if (point <= 96.2) return 'Х';
        if (point <= 97.14) return 'Ж';
        if (point <= 97.87) return 'Ш';
        if (point <= 98.51) return 'Ю';
        if (point <= 98.99) return 'Ц';
        if (point <= 99.35) return 'Щ';
        if (point <= 99.67) return 'Э';
        if (point <= 99.93) return 'Ф';
        if (point <= 99.97) return 'Ъ';
        if (point <= 100.1) return 'Е';

        return (char) (1040 + random.nextInt(32));
    }
}
