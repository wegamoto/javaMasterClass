package com.wewe;

public interface ISaveable {
    List write();
    void read(List savedValues);

}
