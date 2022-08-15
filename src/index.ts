import { registerPlugin } from '@capacitor/core';

import type { RequestCameraPlugin } from './definitions';

const RequestCamera = registerPlugin<RequestCameraPlugin>('RequestCamera', {
  web: () => import('./web').then(m => new m.RequestCameraWeb()),
});

export * from './definitions';
export { RequestCamera };
