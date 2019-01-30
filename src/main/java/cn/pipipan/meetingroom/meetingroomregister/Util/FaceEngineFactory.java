package cn.pipipan.meetingroom.meetingroomregister.Util;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;

public class FaceEngineFactory {
    private static FaceEngine faceEngine;
    public static synchronized FaceEngine getInstance(){
        if (faceEngine == null) {
            faceEngine = new FaceEngine();
            faceEngine.active("3G4cuARsSzRB1yqCdb81k9BYg9REisXrUEvpa9f2Mhz8", "2NLXmxWqt6FGT1v79DWcJaGorM3qxZu4H3ezWXt34r9M");
            EngineConfiguration engineConfiguration = EngineConfiguration.builder().functionConfiguration(
                    FunctionConfiguration.builder()
                            .supportAge(true)
                            .supportFace3dAngle(true)
                            .supportFaceDetect(true)
                            .supportFaceRecognition(true)
                            .supportGender(true)
                            .build()).build();
            faceEngine.init(engineConfiguration);
        }
        return faceEngine;
    }
}
