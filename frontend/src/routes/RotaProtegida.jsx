import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

/**
 * Componente "porteiro" para rotas que exigem autenticacao.
 *
 * Se o usuario estiver autenticado (tem token), mostra o conteudo (children).
 * Se nao, redireciona para a tela de login.
 *
 * Uso: envolver as paginas protegidas com <RotaProtegida>...</RotaProtegida>
 */
function RotaProtegida({ children }) {
    const { isAutenticado } = useAuth();

    // Sem autenticacao -> manda para o login.
    if (!isAutenticado) {
        return <Navigate to="/login" replace />;
    }

    // Autenticado -> mostra a pagina.
    return children;
}

export default RotaProtegida;