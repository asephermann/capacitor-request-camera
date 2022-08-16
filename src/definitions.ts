export interface RequestCameraPlugin {
  checkAndRequestPermissions(): Promise<void>;
}