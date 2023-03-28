package com.home.server.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataStateMachine {

    public Map<String, State> stateMap = new HashMap<>();
    public State currentState;

    public void addState(State state) {
        if (stateMap.isEmpty()) {
            currentState = state;
            currentState.enter();
        }
        stateMap.put(state.getType() ,state);
    }

    public void transferTo(State state) {
        if (!stateMap.isEmpty()) {
            currentState.exit();
            currentState = stateMap.get(state.getType());
            currentState.enter();
        }
    }

    public void process(int msg) {
        currentState.process(msg);
    }

    public abstract class State {
        public String mType;

        public String getType() {
            return mType;
        }

        public abstract void process(int msg);

        public abstract void enter();

        public abstract void exit();
    }
}
