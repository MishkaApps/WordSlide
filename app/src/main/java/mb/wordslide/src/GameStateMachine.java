package mb.wordslide.src;

import android.util.Log;

/**
 * Created by mbolg on 19.06.2017.
 */
public class GameStateMachine {
    private enum State {
        A, B, C, D
    }

    private State state;

    public GameStateMachine(){
        state = State.A;
        printState();
    }

    /**
     * (A) -> (B)
     * (*) -> (*) otherwise
     */
    public void touch(){
        if(state == State.A)
            state = State.B;
        printState();
    }

    /**
     * (B) -> (C)
     * (*) -> (*) otherwise
     */
    public void swipeGetOutOfRange(){
        if(state == State.B)
            state = State.C;
        printState();
    }

    /**
     * (C) -> (B)
     * (*) -> (*) otherwise
     */
    public void shifted(){
        if(state == State.C)
            state = State.B;
        printState();
    }

    /**
     * (B) -> (A)
     * (C) -> (A)
     * (*) -> (*) otherwise
     */
    public void touchEnds(){
        state = State.A;
        printState();
    }

    private void printState(){
        L.l("Current state: " + state.toString());
    }

}
