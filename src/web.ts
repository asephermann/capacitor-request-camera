import { WebPlugin } from '@capacitor/core';

import type { RequestCameraPlugin } from './definitions';

export class RequestCameraWeb extends WebPlugin implements RequestCameraPlugin {
  async checkAndRequestPermissions(): Promise<void> {
    throw new Error('Method not implemented.');
  }
}
