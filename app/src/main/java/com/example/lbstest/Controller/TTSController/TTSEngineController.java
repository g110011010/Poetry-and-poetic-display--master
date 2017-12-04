package com.example.lbstest.Controller.TTSController;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;

import android.widget.Toast;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 天道 北云 on 2017/9/19.
 */

public class TTSEngineController implements SpeechSynthesizerListener {
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private Context context;
    private FloatingActionButton playFloatingActionButton;
    //以下为暂停、恢复、停止按钮引用接收位置，需要时可去除双重注释。
////    private FloatingActionButton pauseFloatingActionButton;
////   private FloatingActionButton resumeFloatingActionButton;
////    private FloatingActionButton stopFloatingActionButton;
    private SpeechSynthesizer mSpeechSynthesizer;
    private String mSampleDirPath;
    private String waitingTTS;

    /**
     *
     * @param context 此Controller的运行环境。
     * @param play play按钮。

     */
    /**
     * 以下为TTSEngineController函数中对应组件的接收参数，需要时可以去除注释并置于类参数末尾。
     FloatingActionButton pause, FloatingActionButton resume, FloatingActionButton stop
    */
    public TTSEngineController(Context context, FloatingActionButton play){
        this.context=context;
        this.playFloatingActionButton=play;
        //以下为暂停、恢复、停止按钮引用赋值位置，需要时可去除双重注释。
////        this.pauseFloatingActionButton=pause;
////        this.resumeFloatingActionButton=resume;
////        this.stopFloatingActionButton=stop;
        //初始化语音运行环境。
        initialEnv();
        //以下为暂停、恢复、停止按钮引用初始化位置，需要时可去除双重注释。
////        //初始化基础组件（不包含开始按键，开始按键需要专门初始化）。
////       initView();
        //初始化TTS引擎核心。
        initialTts();
    }

    /**
     * 初始化TTS基础模块（主要为语音包向SD的写入。）
     */
    private void initialEnv() {
        //判断基础路径是否已经赋值。
        if (mSampleDirPath == null) {
            //如果基础路径部分尚未赋值则读取SD路径并附加SAMPLE_DIR_NAME作为新文件夹储备名。
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        //创建基础路径。
        makeDir(mSampleDirPath);
        //将语音包（女性）写入SD。
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        //将语音包（男性）写入SD。
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        //将所需文本文件写入SD。
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        //将许可文件写入SD。
        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
        //以下为将3个英语语音包写入到SD中。
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);
    }

    /**
     * 用于对语音包的存储位置进行创建如果已创建则不再操作。
     * @param dirPath
     */
    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    /**
     * 将工程需要的资源文件（包含语音文件、txt文件、许可文件等）拷贝到SD卡中使用
     *
     * @param isCover 如果文件已存在是否进行覆盖操作。
     * @param source 需要写入的资源文件。
     * @param dest 需要写入文件存放的目标位置。
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        //如果允许覆盖或者文件并不存在情况下允许覆盖。
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                //由项目获取Assets文件夹下对应文件。
                is = context.getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化暂停、恢复、中止按键，开始按键需要专门赋值，需要时可去除下方代码注释。
     */
//    private void initView(){
//        initPauseFloatingActionButtonListener();
//        initResumeFloatingActionButtonListener();
//        initStopFloatingActionButtonListener();
//    }

    /**
     * 用于对需语音播报的字符串进行赋值。
     * @param waitingTTS 需语音播报的字符串。
     */
    public void fillWaitingTTS(String waitingTTS){
        this.waitingTTS=waitingTTS;
    }

    /**
     * 用于对语音播放按键的初始化，此初始化要求类的调用方对此初始化函数传入初始化监听器。
     * @param playListener 初始化监听器，用于控制播放。
     */
    public void initPlayFloatingActionButtonListener(View.OnClickListener playListener){
        playFloatingActionButton.setOnClickListener(playListener);
    }

    /**
     * 用于对语音暂停按键的初始化，需要时可去除下方代码注释。
     */
//    private void initPauseFloatingActionButtonListener(){
//        pauseFloatingActionButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                mSpeechSynthesizer.pause();
//            }
//        });
//    }

    /**
     * 用于对语音暂停后恢复按键的初始化，需要时可去除下方代码注释。
     */
//    private void initResumeFloatingActionButtonListener(){
//        resumeFloatingActionButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                mSpeechSynthesizer.resume();
//            }
//        });
//    }

    /**
     * 用于对语音停止按键的初始化，需要时可去除下方代码注释。
     */
//    private void initStopFloatingActionButtonListener(){
//        stopFloatingActionButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                mSpeechSynthesizer.stop();
//            }
//        });
//    }
    public int speak(String waitingForTTS) {
        int result = this.mSpeechSynthesizer.speak(waitingForTTS);
        return result;
    }
    private void initialTts(){
        //获取SpeechSynthesizer实例。
        this.mSpeechSynthesizer=SpeechSynthesizer.getInstance();
        //设置SpeechSynthesizer所需上下文。
        this.mSpeechSynthesizer.setContext(context);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 文本模型文件路径 (离线引擎状态)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎状态)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 本地授权文件路径,仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数,当完全采用离线引擎的时候删除。
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
                + LICENSE_FILE_NAME);
        //（权限设置部分）语音开发平台对应APP ID。
        this.mSpeechSynthesizer.setAppId("9448169");
        //(权限设置部分)语音开发平台对应Key与秘钥
        this.mSpeechSynthesizer.setApiKey("ouelYOYNvvj4sFResYrCXH98qxVv1Kzz",
                "slVvgXjsEsbzn7FOF4m9kIENi93TiF3W");
        // 发音人（在线引擎状态），可用参数为0,1,2,3等（取决于服务器端，各值含义参考：0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "3");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功，首次验证耗时较高。
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
        if (authInfo.isSuccess()) {
        } else {
            Toast.makeText(context,"权限验证失败",Toast.LENGTH_SHORT).show();
        }
        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
    }
    @Override
    public void onSynthesizeStart(String s) {

    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    @Override
    public void onSpeechFinish(String s) {

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onError(String s, SpeechError speechError) {

    }

}
