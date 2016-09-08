package com.peekaboo.transformation;

/**
 * Created by Oleksii on 06.09.2016.
 */

public interface EncoderProgressListener {
    void sourceInfo(MultimediaInfo var1);

    void progress(int var1);

    void message(String var1);
}
