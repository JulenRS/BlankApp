package com.jross.blankapp.interfaces;

import com.jross.blankapp.domains.Post;

import java.util.ArrayList;

public interface OnDataReceived {

    void sendDataToClassicFragment(ArrayList<Post> myList);
    void sendDataToSwipeFragment(ArrayList<Post> myList);
}
