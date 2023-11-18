package io.github.reconsolidated.tempowaiter.ntag_decryption;

import lombok.AllArgsConstructor;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NtagDecryptionService {

    public static void main(String[] args) {
        NtagDecryptionService ntagDecryptionService = new NtagDecryptionService();
        ntagDecryptionService.decryptNtag("A5C610504B514177DC2448B7BFA321A4");
    }

    public NtagInfo decryptNtag(String e) {
        String IV = "00000000000000000000000000000000";
        //String key = "00000000000000000000000000000000";
        String key = "12398098548076132587601325870623";


        byte[] ivBytes = Hex.decode(IV);
        byte[] keyBytes = Hex.decode(key);
        byte[] encryptedData = Hex.decode(e);

        CipherParameters keyParam = new KeyParameter(keyBytes);
        CBCBlockCipher blockCipher = new CBCBlockCipher(new AESEngine());
        blockCipher.init(false, new ParametersWithIV(keyParam, ivBytes));

        byte[] PICCData = new byte[encryptedData.length];
        for (int i = 0; i < encryptedData.length; i += 16) {
            blockCipher.processBlock(encryptedData, i, PICCData, i);
        }

        byte[] PICCDataTag = new byte[1];
        byte[] UID = new byte[7];
        byte[] SDMReadCtr = new byte[3];
        System.arraycopy(PICCData, 0, PICCDataTag, 0, 1);
        System.arraycopy(PICCData, 1, UID, 0, 7);
        System.arraycopy(PICCData, 8, SDMReadCtr, 0, 3);
        String piccData = Hex.toHexString(PICCData);
        String piccDataTag = Hex.toHexString(PICCDataTag);
        String uid = Hex.toHexString(UID);
        byte[] reversedSDMReadCtr = new byte[3];
        for (int i = 0; i < SDMReadCtr.length; i++) {
            reversedSDMReadCtr[i] = SDMReadCtr[SDMReadCtr.length - i - 1];
        }
        String stringCtr = Hex.toHexString(reversedSDMReadCtr);
        Long ctr = Long.parseLong(stringCtr, 16);

        return new NtagInfo(piccDataTag, uid, ctr);
    }
}
