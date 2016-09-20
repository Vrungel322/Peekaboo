package com.peekaboo.transformation;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import com.peekaboo.miscellaneous.JavaPropertiesParser;

import java.util.List;

/**
 * Created by Oleksii on 02.08.2016.
 */
public class MessageTone extends ToneAnalyzer {
    private List<ToneScore> tonesList;
    private String UnknownEmotion="unknown";
    public MessageTone() {
        super(JavaPropertiesParser.PARSER.getValue("WatsonToneAnalyzerVersionDate"));
        this.setEndPoint(JavaPropertiesParser.PARSER.getValue("WatsonToneAnalyzerEndPoint"));
        this.setUsernameAndPassword(JavaPropertiesParser.PARSER.getValue("WatsonToneAnalyzerLogin"), JavaPropertiesParser.PARSER.getValue("WatsonToneAnalyzerPassword"));
    }

    public MessageTone SetMessage(String messageText) {
        ToneAnalysis tone = this.getTone(messageText, null).execute();
        tonesList = tone.getDocumentTone().getTones().get(0).getTones();
        return this;
    }

    public String GetMessageTone() {
        ToneScore tScore=tonesList
                .stream().max((t1, t2) -> Double.compare(t1.getScore(), t2.getScore()))
                .get();
        if (tScore.getScore()>0.5 ){
            return tScore.getName();
        }
        else{
            return UnknownEmotion;
        }
    }
}