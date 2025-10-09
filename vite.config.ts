import { UserConfigFn } from 'vite';
import { overrideVaadinConfig } from './vite.generated';
import path from "node:path";
import tailwindcss from '@tailwindcss/vite'

const customConfig: UserConfigFn = (env) => ({
  resolve: {
  		alias: {
  			"@": path.resolve(__dirname, "./src/main/frontend"),
  		},
  	},
  plugins: [
      tailwindcss(),
    ],
});

export default overrideVaadinConfig(customConfig);
