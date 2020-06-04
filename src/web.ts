import { WebPlugin } from '@capacitor/core';
import { FFMPEGPlugin } from './definitions';

export class FFMPEGWeb extends WebPlugin implements FFMPEGPlugin {
  constructor() {
    super({
      name: 'FFMPEG',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const FFMPEG = new FFMPEGWeb();

export { FFMPEG };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(FFMPEG);
