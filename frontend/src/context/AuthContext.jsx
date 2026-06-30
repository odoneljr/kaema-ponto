import { createContext, useContext, useState } from "react";

/**
 * Contexto de autenticacao: guarda o token JWT e os dados do usuario
 * logado, acessiveis por qualquer tela da aplicacao.
 */
const AuthContext = createContext();

export function AuthProvider({ children }) {
    // Tenta recuperar o token salvo no navegador (para manter o login
    // apos recarregar a pagina).
    const [token, setToken] = useState(() => localStorage.getItem("token"));
    const [usuario, setUsuario] = useState(() => {
        const salvo = localStorage.getItem("usuario");
        return salvo ? JSON.parse(salvo) : null;
    });

    // Chamado apos login bem-sucedido: guarda token e usuario.
    function login(novoToken, dadosUsuario) {
        setToken(novoToken);
        setUsuario(dadosUsuario);
        localStorage.setItem("token", novoToken);
        localStorage.setItem("usuario", JSON.stringify(dadosUsuario));
    }

    // Limpa tudo (logout).
    function logout() {
        setToken(null);
        setUsuario(null);
        localStorage.removeItem("token");
        localStorage.removeItem("usuario");
    }

    // 'isAutenticado' e true se houver token.
    const isAutenticado = Boolean(token);

    return (
        <AuthContext.Provider
            value={{ token, usuario, login, logout, isAutenticado }}
        >
            {children}
        </AuthContext.Provider>
    );
}

// Hook para acessar o contexto facilmente em qualquer componente.
export function useAuth() {
    return useContext(AuthContext);
}