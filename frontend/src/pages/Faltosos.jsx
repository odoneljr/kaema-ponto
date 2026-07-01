import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Faltosos() {
    const navigate = useNavigate();

    const [faltosos, setFaltosos] = useState([]);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState("");

    useEffect(() => {
        async function carregar() {
            try {
                const resposta = await api.get("/faltosos");
                setFaltosos(resposta.data);
            } catch (err) {
                setErro("Não foi possível carregar os faltosos.");
            } finally {
                setCarregando(false);
            }
        }
        carregar();
    }, []);

    // Data de hoje formatada em portugues.
    const hoje = new Date().toLocaleDateString("pt-BR", {
        weekday: "long",
        day: "2-digit",
        month: "long",
        year: "numeric",
    });

    return (
        <div className="min-h-screen bg-neutral-100">
            {/* Cabecalho */}
            <header className="bg-neutral-900 text-white">
                <div className="max-w-5xl mx-auto px-6 py-4 flex items-center justify-between">
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
            <main className="max-w-5xl mx-auto px-6 py-10">
                <h2 className="text-2xl font-bold text-neutral-900 mb-1">
                    Faltosos do Dia
                </h2>
                <p className="text-neutral-500 mb-6 capitalize">{hoje}</p>

                {carregando ? (
                    <p className="text-neutral-500">Carregando...</p>
                ) : erro ? (
                    <p className="text-red-600 bg-red-50 rounded-lg px-4 py-3">{erro}</p>
                ) : (
                    <>
                        {/* Card com o total */}
                        <div className="bg-orange-500 text-white rounded-xl p-6 mb-6 inline-block">
                            <p className="text-sm opacity-90">Sem registro de ponto hoje</p>
                            <p className="text-4xl font-bold">{faltosos.length}</p>
                        </div>

                        {/* Lista */}
                        {faltosos.length === 0 ? (
                            <p className="text-neutral-500 bg-white rounded-xl border border-neutral-200 p-6">
                                Todos os funcionários ativos já registraram ponto hoje. 🎉
                            </p>
                        ) : (
                            <div className="bg-white rounded-xl shadow-sm border border-neutral-200 overflow-hidden">
                                <ul className="divide-y divide-neutral-100">
                                    {faltosos.map((f) => (
                                        <li
                                            key={f.id}
                                            className="px-6 py-4 flex items-center justify-between hover:bg-neutral-50 transition"
                                        >
                                            <div>
                                                <p className="font-medium text-neutral-900">{f.name}</p>
                                                <p className="text-sm text-neutral-500">{f.email}</p>
                                            </div>
                                            {f.tagRfid ? (
                                                <span className="font-mono text-xs text-neutral-500">
                          {f.tagRfid}
                        </span>
                                            ) : (
                                                <span className="text-xs text-neutral-400">sem tag</span>
                                            )}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </>
                )}
            </main>
        </div>
    );
}

export default Faltosos;