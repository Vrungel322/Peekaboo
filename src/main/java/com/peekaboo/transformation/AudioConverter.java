package com.peekaboo.transformation;

/**
 * Created by Oleksii on 06.09.2016.
 */
import java.io.File;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;


/*  required jars(jave-1.0.2.jar)
 *  Handles Encoding and Decoding
 *  Audio to different file formats
 */
public class AudioConverter {

    private static final Integer bitrate = 8;//Minimal bitrate only
    private static final Integer channels = 1; //2 for stereo, 1 for mono
    private static final Integer samplingRate = 8000;//For good quality.

    /* Data structures for the audio
     *  and Encoding attributes
     */
    private AudioAttributes audioAttr = new AudioAttributes();
    private EncodingAttributes encoAttrs = new EncodingAttributes();
    private Encoder encoder = new Encoder();

    /*
     * File formats that will be converted
     * Please Don't change!
     */
    private String oggFormat = "ogg";
    private String mp3Format = "mp3";
    private String wavFormat = "wav";

    /*
     * Codecs to be used
     */
    private String oggCodec = "vorbis";
    private String wavCodec = "adpcm_ima_wav";



    /* Set the default attributes
     * for encoding
     */
    public AudioConverter(){
        audioAttr.setBitRate(bitrate);
        audioAttr.setChannels(channels);
        audioAttr.setSamplingRate(samplingRate);
    }

    public void encodeAudio(File source, File target, String mimeType){
        //Change the hardcoded mime type later on
        if(mimeType.equals("audio/mp3")){
            this.wavToOgg(source, target);
        }
    }

    private void wavToOgg(File source, File target){
        //ADD CODE FOR CHANGING THE EXTENSION OF THE FILE
        encoAttrs.setFormat(wavFormat);
        audioAttr.setCodec(wavCodec);
        encoAttrs.setAudioAttributes(audioAttr);
        try{
            encoder.encode(source, target, encoAttrs);
            String[] a=encoder.getAudioEncoders();
            for (int i= 1;i<10;i++) {
                System.out.println(a[i]);
            }
        }catch(Exception e){
            System.out.println("Encoding Failed"+e.toString());
        }
    }

    //    private void oggToMp3(){
//        //ADD CODE FOR CHANGING THE EXTENSION OF THE FILE
//        encoAttrs.setFormat(mp3Format);
//        audioAttr.setCodec(mp3Codec);
//        encoAttrs.setAudioAttributes(audioAttr);
//        try{
//            encoder.encode(source, target, encoAttrs);
//        }catch(Exception e){
//            System.out.println("Encoding Failed");
//        }
//    }
//
    public static void main(String[] args){
        AudioConverter aed = new AudioConverter();
        File source = new  File("D:\\JAVA\\PEEKABOO\\Peekaboolasttest\\Peekaboo\\src\\test.ogg");
        File target = new File("D:\\JAVA\\PEEKABOO\\Peekaboolasttest\\Peekaboo\\src\\test.wav");

        //Test Mp3 To Ogg Convertion
        String mimeType = "audio/mp3";
        aed.encodeAudio(source, target, mimeType);

        //Test Ogg To Mp3 Convertion
    }
}
