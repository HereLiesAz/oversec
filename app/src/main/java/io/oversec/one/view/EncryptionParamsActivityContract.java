package io.oversec.one.view;

import io.oversec.one.crypto.AbstractEncryptionParams;
import io.oversec.one.crypto.EncryptionMethod;

public interface EncryptionParamsActivityContract {
    int REQUEST_CODE_RECIPIENT_SELECTION = 1001;
    int REQUEST_CODE_OWNSIGNATUREKEY_SELECTION = 1002;
    int REQUEST_CODE_DOWNLOAD_KEY = 1003;
    int REQUEST_CODE__CREATE_NEW_KEY = 1004;

    void doEncrypt(AbstractEncryptionParams encryptionParams, boolean addLink);

    void finishWithResultOk();

    String getXCoderId(EncryptionMethod method, String packageName);

    String getPadderId(EncryptionMethod method, String packageName);

    void setXcoderAndPadder(EncryptionMethod method, String packageName, String coderId, String padderId);
}