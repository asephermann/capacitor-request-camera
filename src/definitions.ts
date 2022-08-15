export interface RequestCameraPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
