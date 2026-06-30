import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

/**
 * Tela de login. Envia email + senha ao backend (/auth/login).
 * Se as credenciais forem validas, guarda o token e vai para a Home.
 */
function Login() {
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [erro, setErro] = useState("");
    const [carregando, setCarregando] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    async function handleSubmit(e) {
        e.preventDefault(); // evita o recarregamento padrao do formulario
        setErro("");
        setCarregando(true);

        try {
            // Chama o endpoint POST /auth/login do backend.
            const resposta = await api.post("/auth/login", {
                email: email,
                password: senha,
            });

            // resposta.data = { token, name, email, isAdmin }
            const { token, name, email: emailUsuario, isAdmin } = resposta.data;

            // Guarda no contexto + localStorage.
            login(token, { name, email: emailUsuario, isAdmin });

            // Redireciona para a Home.
            navigate("/home");
        } catch (err) {
            // Credenciais invalidas ou erro de conexao.
            setErro("Email ou senha invalidos.");
        } finally {
            setCarregando(false);
        }
    }

    return (
        <div>
            <h1>Kaema Ponto</h1>
            <h2>Entrar</h2>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Senha</label>
                    <input
                        type="password"
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                        required
                    />
                </div>

                {erro && <p style={{ color: "red" }}>{erro}</p>}

                <button type="submit" disabled={carregando}>
                    {carregando ? "Entrando..." : "Entrar"}
                </button>
            </form>
        </div>
    );
}

export default Login;