export interface AccountManagerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
