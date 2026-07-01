import axios from "axios";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
});

/**
 * Interceptor: antes de cada requisicao, pega o token salvo no
 * localStorage e o adiciona no cabecalho Authorization.
 * Assim, nao precisamos repetir isso em cada chamada.
 */
api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;