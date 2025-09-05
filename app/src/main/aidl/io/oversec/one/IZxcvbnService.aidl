package io.oversec.one;

import io.oversec.one.ZxcvbnResult;

interface IZxcvbnService {

    ZxcvbnResult calcEntropy(String aString);
    void exit();
}
