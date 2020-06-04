declare module "@capacitor/core" {
  interface PluginRegistry {
    FFMPEG: FFMPEGPlugin;
  }
}

export interface FFMPEGPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}
