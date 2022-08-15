import { WebPlugin } from '@capacitor/core';

import type { RequestCameraPlugin } from './definitions';

export class RequestCameraWeb extends WebPlugin implements RequestCameraPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
