import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Funcionarios() {
    const navigate = useNavigate();

    const [funcionarios, setFuncionarios] = useState([]);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState("");

    // Ao abrir a tela, busca a lista no backend.
    useEffect(() => {
        async function carregar() {
            try {
                const resposta = await api.get("/funcionarios");
                setFuncionarios(resposta.data);
            } catch (err) {
                setErro("Não foi possível carregar os funcionários.");
            } finally {
                setCarregando(false);
            }
        }
        carregar();
    }, []);

    return (
        <div className="min-h-screen bg-neutral-100">
            {/* Cabecalho */}
            <header className="bg-neutral-900 text-white">
                <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
                    <h1 className="text-xl font-bold">
                        Kaema <span className="text-orange-500">Ponto</span>
                    </h1>
                    <button
                        onClick={() => navigate("/home")}
                        className="text-sm bg-neutral-700 hover:bg-neutral-600 px-4 py-2 rounded-lg transition"
                    >
                        Voltar
                    </button>
                </div>
            </header>

            {/* Conteudo */}
            <main className="max-w-6xl mx-auto px-6 py-10">
                <div className="flex items-center justify-between mb-6">
                    <div>
                        <h2 className="text-2xl font-bold text-neutral-900">Funcionários</h2>
                        <p className="text-neutral-500">
                            {funcionarios.length} colaborador(es) cadastrado(s)
                        </p>
                    </div>
                </div>

                {/* Estados: carregando / erro / lista */}
                {carregando ? (
                    <p className="text-neutral-500">Carregando...</p>
                ) : erro ? (
                    <p className="text-red-600 bg-red-50 rounded-lg px-4 py-3">{erro}</p>
                ) : (
                    <div className="bg-white rounded-xl shadow-sm border border-neutral-200 overflow-hidden">
                        <table className="w-full text-left">
                            <thead className="bg-neutral-50 text-neutral-600 text-sm">
                            <tr>
                                <th className="px-6 py-3 font-medium">Nome</th>
                                <th className="px-6 py-3 font-medium">Email</th>
                                <th className="px-6 py-3 font-medium">Tag RFID</th>
                                <th className="px-6 py-3 font-medium">Perfil</th>
                            </tr>
                            </thead>
                            <tbody className="divide-y divide-neutral-100">
                            {funcionarios.map((f) => (
                                <tr key={f.id} className="hover:bg-neutral-50 transition">
                                    <td className="px-6 py-4 font-medium text-neutral-900">
                                        {f.name}
                                    </td>
                                    <td className="px-6 py-4 text-neutral-600">{f.email}</td>
                                    <td className="px-6 py-4 text-neutral-600">
                                        {f.tagRfid ? (
                                            <span className="font-mono text-sm">{f.tagRfid}</span>
                                        ) : (
                                            <span className="text-neutral-400 text-sm">sem tag</span>
                                        )}
                                    </td>
                                    <td className="px-6 py-4">
                                        {f.isAdmin ? (
                                            <span className="text-xs bg-orange-100 text-orange-700 px-2.5 py-1 rounded-full font-medium">
                          Admin
                        </span>
                                        ) : (
                                            <span className="text-xs bg-neutral-100 text-neutral-600 px-2.5 py-1 rounded-full font-medium">
                          Comum
                        </span>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </main>
        </div>
    );
}

export default Funcionarios;