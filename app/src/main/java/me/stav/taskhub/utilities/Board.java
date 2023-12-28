package me.stav.taskhub.utilities;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private String boardName, boardDescription;
    private String owner;
    private ArrayList<Integer> participants;
    private ArrayList<Item> items;
    private byte[] background;
    private int boardId;
    private FirebaseHandler firebaseHandler;

    public Board(String boardName, String boardDescription, String owner, Context context) {
        this.boardName = boardName;
        this.boardDescription = boardDescription;
        this.owner = owner;
        this.participants = new ArrayList<>();
        this.items = new ArrayList<>();
        this.background = null;
        this.firebaseHandler = new FirebaseHandler(context);
        this.boardId = generateBoardId();
     }

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Integer> getParticipants() {
        return this.participants;
    }

    public void setParticipants(ArrayList<Integer> participants) {
        this.participants = participants;
    }

    public void addParticipant(Integer participant) {
        this.participants.add(participant);
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public byte[] getBackground() {
        return this.background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public String getBoardDescription() {
        return boardDescription;
    }

    public void setBoardDescription(String boardDescription) {
        this.boardDescription = boardDescription;
    }

    public int getBoardId() {
        return boardId;
    }

    private boolean found = false; // Boolean to be used inside lambada
    private int generateBoardId() { // Gets random number with 9 digits.
        Random rand = new Random();
        int id = 100000000 + rand.nextInt(900000000);
        firebaseHandler.findBoardById(id, temp -> found = temp);

        if (found) {
            return generateBoardId();
        }

        return id;
    }
}
