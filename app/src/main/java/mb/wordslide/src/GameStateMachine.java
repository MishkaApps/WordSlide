package mb.wordslide.src;

import android.util.Log;

/**
 * Created by mbolg on 19.06.2017.
 */
public class GameStateMachine {
    public State getState() {
        return state;
    }

    public enum State {
        A, B, C,
    }

    public interface StateListener{
        void notice();
    }

    private StateListener stateListener;


    private State state;

    public GameStateMachine(StateListener listener){
        state = State.A;
        printState();
        this.stateListener = listener;
    }

    /**
     * (A) -> (B)
     * (*) -> (*) otherwise
     */
    public void touch(){
        if(state == State.A)
            state = State.B;
        printState();
        stateListener.notice();
    }

    /**
     * (B) -> (C)
     * (*) -> (*) otherwise
     */
    public void swipeGetOutOfRange(){
        if(state == State.B)
            state = State.C;
        printState();
        stateListener.notice();
    }

    /**
     * (C) -> (B)
     * (*) -> (*) otherwise
     */
    public void shifted(){
        if(state == State.C)
            state = State.B;
        printState();
        stateListener.notice();
    }

    /**
     * (B) -> (A)
     * (C) -> (A)
     * (*) -> (*) otherwise
     */
    public void touchEnds(){
        state = State.A;
        printState();
        stateListener.notice();
    }

    private void printState() {
//        L.l("Current state: " + state.toString());
    }

}
