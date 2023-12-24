import { defineConfig } from 'vite'
import { resolve } from 'path'

export default defineConfig({
  root: './',
  optimizeDeps: {},
  build: {
    emptyOutDir: true,
    outDir: 'dist',
    assetsDir: 'src/assets',
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
        lehrer: resolve(__dirname, 'src/pages/teacher.html'),
        login: resolve(__dirname, 'src/pages/login.html'),
        vertretungsplan: resolve(__dirname, 'src/pages/substitution_plan.html'),
        verwaltung: resolve(__dirname, 'src/pages/administrator.html')
      },
    }
  }
})