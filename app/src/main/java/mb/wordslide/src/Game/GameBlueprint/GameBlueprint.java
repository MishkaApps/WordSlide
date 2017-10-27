package mb.wordslide.src.Game.GameBlueprint;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.games.Game;
import com.google.common.collect.Table;
import com.google.gson.Gson;

import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.Field.LetterGenerator;
import mb.wordslide.src.Game.OnClearWordListener;

/**
 * Created by mbolg on 25.10.2017.
 */

public abstract class GameBlueprint implements OnClearWordListener {
    private static final String PREFERENCES_NAME = "preferences", SAVED_GAME_NAME = "saved_game", SAVED_GAME_TYPE = "saved_game_type";
    private char[][] gameArea;
    private int score;
    private int progress;
    private transient Context context;

    public GameBlueprint(){}

    public GameBlueprint(Context context) {
        score = 0;
        gameArea = new char[Configurations.GAME_AREA_DIMENSION][Configurations.GAME_AREA_DIMENSION];
        progress = getStartProgress();
        this.context = context;
        fillGameAreaWithRandomLetters();
    }

    protected abstract int getStartProgress();

    public void setProgress(int progress) {
        this.progress = progress;
        saveGame();
    }

    private void fillGameAreaWithRandomLetters() {
        for (int row = 0; row < Configurations.GAME_AREA_DIMENSION; row++)
            for (int col = 0; col < Configurations.GAME_AREA_DIMENSION; col++)
                gameArea[row][col] = LetterGenerator.getRandomLetter();
    }

    public static GameBlueprint retrieveSavedGame(Context context, Class<? extends GameBlueprint> gameBlueprintClass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String gsonedGame = sharedPreferences.getString(SAVED_GAME_NAME, null);
        GameBlueprint retrievedGameBlueprint = gson.fromJson(gsonedGame, gameBlueprintClass);
        retrievedGameBlueprint.context = context;
        return retrievedGameBlueprint;
    }

    public static boolean isGameSaved(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(SAVED_GAME_NAME);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void updateGameAreaAndSave(Table<Integer, Integer, Character> gameArea) {
        for (Table.Cell field : gameArea.cellSet())
            this.gameArea[(Integer) field.getRowKey()][(Integer) field.getColumnKey()] = (Character) field.getValue();
        saveGame();
    }
//todo: возможно сохранение игры слишком затратная операция. рефакторнуть
    private void saveGame() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String gsonedGame = gson.toJson(this);
        preferencesEditor.putString(SAVED_GAME_NAME, gsonedGame);
        preferencesEditor.putInt(SAVED_GAME_TYPE, getGameType().toInt());
        preferencesEditor.commit();
    }

    protected abstract GameType getGameType();

    public static void deleteGame(Context context) {
        (context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)).edit().remove(SAVED_GAME_NAME).commit();
    }

    public char getLetterInField(int row, int col) {
        return gameArea[row][col];
    }

    @Override
    public void notifyWordCleared() {

    }

    @Override
    public void notifyWordUsed() {

    }

    public int getScore() {
        return score;
    }

    public int getProgress() {
        return progress;
    }

    public static GameType getGameTypeOfSavedGame(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        int gameType = sharedPreferences.getInt(SAVED_GAME_TYPE, 3);
        return GameType.getTypeByIndex(gameType);
    }

    //todo: Зарефакторить. Сделать определение типа по индексу более лучшим

    public enum GameType {
        MOVES(0), TIME(1);
        private int index;

        GameType(int i) {
            index = i;
        }

        public int toInt() {
            return index;
        }

        public static GameType getTypeByIndex(int index){
            if(index == 0)
                return MOVES;
            else return TIME;
        }
    }
}