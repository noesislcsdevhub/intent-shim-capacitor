export interface IntentShimPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
