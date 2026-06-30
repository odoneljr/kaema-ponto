import { useAuth } from "../context/AuthContext";

function Home() {
    const { usuario, logout } = useAuth();

    return (
        <div>
            <h1>Bem-vindo, {usuario?.name}!</h1>
            <p>Você está logado no Kaema Ponto.</p>
            <button onClick={logout}>Sair</button>
        </div>
    );
}

export default Home;