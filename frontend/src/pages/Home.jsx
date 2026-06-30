import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Home() {
    const { usuario, logout } = useAuth();
    const navigate = useNavigate();

    function handleLogout() {
        logout();              // limpa token e usuario
        navigate("/login");    // volta para a tela de login
    }

    return (
        <div>
            <h1>Bem-vindo, {usuario?.name}!</h1>
            <p>Você está logado no Kaema Ponto.</p>
            <button onClick={handleLogout}>Sair</button>
        </div>
    );
}

export default Home;