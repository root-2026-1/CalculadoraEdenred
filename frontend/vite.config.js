import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(() => {
  const backendUrl = process.env.VITE_API_URL || 'http://localhost:8080';

  return {
  plugins: [react()],
  server: {
    proxy: {
      '/api':        backendUrl,
      '/calculos':   backendUrl,
      '/simulacoes': backendUrl,
      // '/cenarios' é endpoint do backend E rota do app. O bypass faz a
      // navegação do browser (F5/deep-link) cair no app (index.html), enquanto
      // as chamadas fetch/XHR continuam sendo proxiadas pro backend.
      '/cenarios': {
        target: backendUrl,
        changeOrigin: true,
        bypass(req) {
          if (req.headers.accept && req.headers.accept.includes('text/html')) {
            return '/index.html'
          }
        },
      },
    },
  },
  }
})
