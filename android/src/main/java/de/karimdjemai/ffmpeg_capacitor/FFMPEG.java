package de.karimdjemai.ffmpeg_capacitor;

import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.FFprobe;
import com.arthenica.mobileffmpeg.MediaInformation;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.PluginMethod;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

@NativePlugin()
public class FFMPEG extends Plugin {
    
    @PluginMethod()
    public void execute(PluginCall call) {
        String command = call.getString("command");
        
        int rc = FFmpeg.execute(command);
        
        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "ffmpegdebug Command execution completed successfully.");
            
            call.success();
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
            
            call.reject("Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            
            call.reject(String.format("Command execution failed with rc=%d and the output below.", rc));
        }
    }
    
    @PluginMethod()
    public void cancel(PluginCall call) {
        FFmpeg.cancel();
        
        call.success();
    }
    
    @PluginMethod()
    public void getMediaInformation(PluginCall call) {
        MediaInformation info = FFprobe.getMediaInformation(call.getString("path"));
        
        if (info != null) {
            JSObject ret = new JSObject();
            ret.put("bitrate", info.getBitrate());
            ret.put("duration", info.getDuration());
            ret.put("format", info.getFormat());
            ret.put("width", info.getStreams().get(0).getWidth());
            ret.put("height", info.getStreams().get(0).getWidth());
            ret.put("fps", info.getStreams().get(0).getRealFrameRate());
            ret.put("raw", info.getRawInformation());
            
            call.resolve(ret);
        } else {
            call.reject("Error");
        }
    }
    
    @PluginMethod()
    public void enableStatisticsCallback() {
        Config.enableStatisticsCallback(newStatistics -> {
            Log.d(Config.TAG, String.format(
                    "bitrate: %s, " +
                    "size: %d, " +
                    "speed: %s, " +
                    "time: %d, " +
                    "fps: %s, " +
                    "frame: %d, " +
                    "quality: %s ",
                    newStatistics.getBitrate(),
                    newStatistics.getSize(),
                    newStatistics.getSpeed(),
                    newStatistics.getTime(),
                    newStatistics.getVideoFps(),
                    newStatistics.getVideoFrameNumber(),
                    newStatistics.getVideoQuality()
            ));
            
            JSObject ret = new JSObject();
            ret.put("bitrate", newStatistics.getBitrate());
            ret.put("size", newStatistics.getSize());
            ret.put("speed", newStatistics.getSpeed());
            ret.put("time", newStatistics.getTime());
            ret.put("fps", newStatistics.getVideoFps());
            ret.put("frame", newStatistics.getVideoFrameNumber());
            ret.put("quality", newStatistics.getVideoQuality());
            
            notifyListeners("statistics", ret);
        });
    }
    
    @PluginMethod()
    public void enableLogCallback() {
        Config.enableLogCallback(message -> {
            Log.d(Config.TAG, message.getText());
            
            JSObject ret = new JSObject();
            ret.put("message", message.getText());
            
            notifyListeners("log", ret);
        });
    }
}
