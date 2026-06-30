import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

function Login() {
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [erro, setErro] = useState("");
    const [carregando, setCarregando] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    async function handleSubmit(e) {
        e.preventDefault();
        setErro("");
        setCarregando(true);

        try {
            const resposta = await api.post("/auth/login", {
                email: email,
                password: senha,
            });

            const { token, name, email: emailUsuario, isAdmin } = resposta.data;
            login(token, { name, email: emailUsuario, isAdmin });
            navigate("/home");
        } catch (err) {
            setErro("Email ou senha inválidos.");
        } finally {
            setCarregando(false);
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-neutral-100 px-4">
            <div className="w-full max-w-md bg-white rounded-2xl shadow-xl p-8">
                {/* Cabecalho */}
                <div className="text-center mb-8">
                    <h1 className="text-3xl font-bold text-neutral-900">
                        Kaema <span className="text-orange-500">Ponto</span>
                    </h1>
                    <p className="text-neutral-500 mt-1">Sistema de ponto eletrônico</p>
                </div>

                {/* Formulario */}
                <form onSubmit={handleSubmit} className="space-y-5">
                    <div>
                        <label className="block text-sm font-medium text-neutral-700 mb-1">
                            Email
                        </label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            className="w-full px-4 py-2.5 rounded-lg border border-neutral-300 focus:border-orange-500 focus:ring-2 focus:ring-orange-200 outline-none transition"
                            placeholder="seu@email.com"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-neutral-700 mb-1">
                            Senha
                        </label>
                        <input
                            type="password"
                            value={senha}
                            onChange={(e) => setSenha(e.target.value)}
                            required
                            className="w-full px-4 py-2.5 rounded-lg border border-neutral-300 focus:border-orange-500 focus:ring-2 focus:ring-orange-200 outline-none transition"
                            placeholder="••••••••"
                        />
                    </div>

                    {erro && (
                        <p className="text-sm text-red-600 bg-red-50 rounded-lg px-3 py-2">
                            {erro}
                        </p>
                    )}

                    <button
                        type="submit"
                        disabled={carregando}
                        className="w-full bg-orange-500 hover:bg-orange-600 text-white font-semibold py-2.5 rounded-lg transition disabled:opacity-60 disabled:cursor-not-allowed"
                    >
                        {carregando ? "Entrando..." : "Entrar"}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Login;