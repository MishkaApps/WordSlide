package mb.wordslide.src.Game;

import java.util.Random;

/**
 * Created by mbolg on 11.08.2017.
 */

public class Dictionary {

    public boolean wordExist(String word) {
        return new Random().nextInt(10) > 7;
    }
}
